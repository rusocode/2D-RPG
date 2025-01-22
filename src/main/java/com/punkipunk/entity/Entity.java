package com.punkipunk.entity;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.components.Flags;
import com.punkipunk.entity.components.Particle;
import com.punkipunk.entity.components.Stats;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.mob.MobCategory;
import com.punkipunk.entity.spells.Spell;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.physics.Mechanics;
import com.punkipunk.utils.Timer;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.Position;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

public abstract class Entity {

    public final Game game;
    public final World world;

    public final Stats stats;
    public final Flags flags;
    public final SpriteSheet sheet;
    public final Timer timer;
    public final Mechanics mechanics;
    public final Position position; // TODO Podria ir en World

    public String soundHit, soundDeath;
    public MobCategory mobCategory; // TODO Se podria mover a Mob?
    public Direction direction = Direction.DOWN;
    public Rectangle hitbox = new Rectangle(0, 0, tile, tile); // TODO Se crea aca?
    public Rectangle attackbox = new Rectangle(0, 0, 0, 0);
    public Dialogue dialogue;
    public double hitboxDefaultX, hitboxDefaultY;
    public Animation down, up, left, right;
    public Image currentFrame;
    public Spell spell;
    public Entity linkedEntity;

    // Si el boss esta o no dormido para la cutscene
    public boolean sleep;
    /* Para indicar si el player entro en el area del boss, entonces evita dibujarlo para simular el movimiento de la camara con
     * PlayerDummy (personaje ficticio). */
    public boolean drawing = true;
    /* Esta variable sirve para controlar la puerta de entrada al boss, es decir si morimos mientras peleamos con el
     * boss, la puerta temporal desaparece para que podemos entrar de nuevo al boss. */
    public boolean temp;

    // Variables temporales
    public int tempScreenX, tempScreenY;

    public Entity(Game game, World world, int... pos) {
        this.game = game;
        this.world = world;
        this.stats = new Stats();
        this.flags = new Flags();
        this.sheet = new SpriteSheet();
        this.timer = new Timer();
        this.mechanics = new Mechanics();
        this.position = new Position();
        position.set(pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
    }

    public void update() {
        if (!sleep) {
            if (flags.knockback) {
                checkCollisions();
                // If it doesn't collide, then update the position depending on the attacker's direction
                if (!flags.colliding) position.update(this, direction.knockbackDirection);
                else mechanics.stopKnockback(this); // If it collides, it stops the knockback
                timer.timerKnockback(this, INTERVAL_KNOCKBACK);
            } else if (flags.hitting) hit();
            else {
                // It is important that you perform the actions before checking for collisions
                doActions();
                checkCollisions();
                if (!flags.colliding) position.update(this, direction);
            }
            timer.checkTimers(this);
        }
    }

    public void render(GraphicsContext g2) {
        if (isOnCamera()) {
            tempScreenX = getScreenX();
            tempScreenY = getScreenY();

            // Si tiene la barra de vida activada y si no es un boss
            if (flags.hpBar && !flags.boss) game.gameSystem.ui.renderHpBar(this);

            if (flags.invincible) {
                // Without this, the bar disappears after 4 seconds, even if the player continues attacking the mob
                timer.hpBarCounter = 0;
                Utils.changeAlpha(g2, 0.4f);
            }
            if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            // Si es una animacion con dos frames para cada direccion
            if (sheet.movement != null || sheet.attack != null)
                g2.drawImage(sheet.getCurrentAnimationFrame(this), tempScreenX, tempScreenY);
                // Si es una animacion con mas de dos framas para cada direccion
            else if (sheet.down != null || sheet.up != null || sheet.left != null || sheet.right != null)
                g2.drawImage(sheet.getCurrentAnimationFrame2(this), tempScreenX, tempScreenY);
                // Si es una imagen estatica (item, interactive)
            else g2.drawImage(sheet.frame, getScreenX(), getScreenY());

            if (game.gameSystem.keyboard.isKeyToggled(Key.RECTS)) drawRects(g2);

            Utils.changeAlpha(g2, 1f);
        }
    }

    /**
     * Perform a sequence of actions.
     */
    protected void doActions() {
    }

    /**
     * Check if I drop an object.
     */
    public void checkDrop() {
    }

    public Color getParticleColor() {
        return null;
    }

    public int getParticleSize() {
        return 0;
    }

    public int getParticleSpeed() {
        return 0;
    }

    public int getParticleMaxLife() {
        return 0;
    }

    /**
     * Generates 4 particles on the target.
     *
     * @param generator entity that will generate the particles.
     * @param target    target where the particles will be generated.
     */
    protected void generateParticle(Entity generator, Entity target) {
        world.entitySystem.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, -1)); // Top left
        world.entitySystem.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, -1)); // Top right
        world.entitySystem.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, 1)); // Down left
        world.entitySystem.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, 1)); // Down right
    }

    /**
     * Hits the entity if the attackbox in the attack frame collides with the target's hitbox.
     * <p>
     * From 0 to motion1 ms the first attack frame is shown. From motion1 to motion2 ms the second attack frame is shown. After
     * motion2 it returns to the motion frame. In the case of the player there is only one attack frame.
     * <p>
     * In the second attack frame, the x-y position is adjusted for the attackbox and checks if it collides with an entity.
     */
    public void hit() {
        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= stats.motion1) sheet.attackNum = 1;
        if (timer.attackAnimationCounter > stats.motion1 && timer.attackAnimationCounter <= stats.motion2) {
            sheet.attackNum = 2;

            int currentX = position.x, currentY = position.y;
            int hitboxWidth = (int) hitbox.getWidth(), hitboxHeight = (int) hitbox.getHeight();

            switch (direction) {
                case DOWN -> position.y += (int) attackbox.getHeight();
                case UP -> position.y -= (int) attackbox.getHeight();
                case LEFT -> position.x -= (int) attackbox.getWidth();
                case RIGHT -> position.x += (int) attackbox.getWidth();
            }

            hitbox.setWidth(attackbox.getWidth());
            hitbox.setHeight(attackbox.getHeight());

            hitPlayer(this, game.gameSystem.collisionChecker.checkPlayer(this), stats.attack);

            position.x = currentX;
            position.y = currentY;

            hitbox.setWidth(hitboxWidth);
            hitbox.setHeight(hitboxHeight);
        }
        if (timer.attackAnimationCounter > stats.motion2) {
            sheet.attackNum = 1;
            timer.attackAnimationCounter = 0;
            flags.hitting = false;
        }
    }

    /**
     * Dropea un item.
     * <p>
     * FIXME El item no siempre dropea en el centro de la entidad
     *
     * @param item item a dropear
     */
    protected void drop(Item item) {
        // Ajusta la posicion del item en el centro del tile
        int x = (int) (position.x + (sheet.frame.getWidth() / 2)) / tile;
        int y = (int) (position.y + (sheet.frame.getHeight() / 2)) / tile;
        if (item.amount > 0) world.entitySystem.createItemWithAmount(item.getType(), world.map.num, item.amount, x, y);
        else world.entitySystem.createItem(item.getType(), world.map.num, x, y);
    }

    /**
     * Golpea al player.
     * <p>
     *
     * @param entity  entidad con la que hace contacto el player.
     * @param contact si la entidad hace contacto con el player.
     * @param attack  ataque de la entidad.
     */
    public void hitPlayer(Entity entity, boolean contact, int attack) {
        if (entity.mobCategory != MobCategory.NPC && entity.mobCategory != MobCategory.PEACEFUL && contact && !world.entitySystem.player.flags.invincible) {
            game.gameSystem.audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
            // Resta la defensa del player del ataque del mob para calcular el daño justo
            int damage = Math.max(attack - world.entitySystem.player.stats.defense, 1);
            world.entitySystem.player.stats.decreaseHp(damage);
            world.entitySystem.player.flags.invincible = true;
        }
    }

    /**
     * Check collisions.
     */
    public void checkCollisions() {
        flags.colliding = false; // Resetea colliding para que la entidad se pueda mover cuando no este colisionando
        game.gameSystem.collisionChecker.checkTile(this);
        game.gameSystem.collisionChecker.checkItem(this);
        game.gameSystem.collisionChecker.checkMob(this);
        game.gameSystem.collisionChecker.checkInteractive(this);
        if (game.gameSystem.collisionChecker.checkPlayer(this)) {
            hitPlayer(this, true, stats.attack);
            flags.colliding = true;
        }
    }

    /**
     * Check if the entity is inside the camera.
     * <p>
     * TODO Tendria que ir a una clase llamada Camera
     *
     * @return true if the entity is inside the camera or false.
     */
    public boolean isOnCamera() {
        /* If the render is with a larger entity (for example, Skeleton) than normal, it will not be represented when
         * the player displays only its feet, since the distance from the boss that starts from the upper left corner
         * (or the center?) is a lot with respect to the view of the player on the screen. Therefore, this vision is
         * increased by multiplying the bossArea. */
        int bossArea = 5;
        return position.x + tile * bossArea > world.entitySystem.player.position.x - X_OFFSET &&
                position.x - tile < world.entitySystem.player.position.x + X_OFFSET &&
                position.y + tile * bossArea > world.entitySystem.player.position.y - Y_OFFSET &&
                position.y - tile < world.entitySystem.player.position.y + Y_OFFSET;
    }

    public int getScreenX() {
        return position.x - world.entitySystem.player.position.x + X_OFFSET;
    }

    public int getScreenY() {
        return position.y - world.entitySystem.player.position.y + Y_OFFSET;
    }

    /**
     * Draw the rectangles that represent the frames, hitbox and attackbox.
     */
    private void drawRects(GraphicsContext gc) {
        // Frame
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(1);
        gc.strokeRect(getScreenX(), getScreenY(), sheet.frame.getWidth(), sheet.frame.getHeight());
        // Hitbox
        gc.setStroke(Color.BLUE);
        gc.strokeRect(getScreenX() + hitbox.getX(), getScreenY() + hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        // Attackbox
        if (flags.hitting) {
            gc.setStroke(Color.RED);
            gc.strokeRect(getScreenX() + attackbox.getX() + hitbox.getX(),
                    getScreenY() + attackbox.getY() + hitbox.getY(),
                    attackbox.getWidth(), attackbox.getHeight());
        }
    }

    /**
     * Gets the central position of x of the entity.
     *
     * @return the center x position of the entity.
     */
    public int getCenterX() {
        return (int) (position.x + sheet.frame.getWidth() / 2);
    }

    /**
     * Gets the central position of y of the entity.
     *
     * @return the central position of y of the entity.
     */
    public int getCenterY() {
        return (int) (position.y + sheet.frame.getHeight() / 2);
    }

    /**
     * Gets the distance in tiles of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in tiles of the target with respect to the mob.
     */
    protected int getTileDistance(Entity target) {
        return (getXDistance(target) + getYDistance(target)) / tile;
    }

    /**
     * Gets the distance in x of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in x of the target with respect to the mob.
     */
    protected int getXDistance(Entity target) {
        return Math.abs(getCenterX() - target.getCenterX());
    }

    /**
     * Gets the distance in y of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in y of the target with respect to the mob.
     */
    protected int getYDistance(Entity target) {
        return Math.abs(getCenterY() - target.getCenterY());
    }

}
