package com.punkipunk.world.entity;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.gui.container.Container;
import com.punkipunk.physics.Mechanics;
import com.punkipunk.utils.Timer;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.Position;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.item.Item;
import com.punkipunk.world.entity.mob.Mob;
import com.punkipunk.world.entity.projectile.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

public abstract class Entity {
    public final Game game;
    public final World world;

    // TODO Faltaria el sonido
    public Type type = Type.HOSTILE;
    public Direction direction = Direction.DOWN;
    public Position pos = new Position(); // TODO Podria ir en World
    public Stats stats = new Stats();
    public Flags flags = new Flags();
    public SpriteSheet sheet = new SpriteSheet();
    public Timer timer = new Timer();
    public Mechanics mechanics = new Mechanics();
    public Rectangle hitbox = new Rectangle(0, 0, tile, tile);
    public Rectangle attackbox = new Rectangle(0, 0, 0, 0);
    public Container inventory;
    public Dialogue dialogue;
    public double hitboxDefaultX, hitboxDefaultY;

    public Animation down, up, left, right;
    public Image currentFrame, currentSwordFrame;

    public Projectile projectile;

    public Entity linkedEntity;

    // Si el boss esta o no dormido para la cutscene
    public boolean sleep;
    /* Para indicar si el player entro en el area del boss, entonces evita dibujarlo para simular el movimiento de la
     * camara con PlayerDummy (personaje ficticio). */
    public boolean drawing = true;
    /* Esta variable sirve para controlar la puerta de entrada al boss, es decir si morimos mientras peleamos con el
     * boss, la puerta temporal desaparece para que podemos entrar de nuevo al boss. */
    public boolean temp;

    public int interval;

    // Variables temporales
    public int tempScreenX, tempScreenY;

    public Entity(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public Entity(Game game, World world, int col, int row) {
        this.game = game;
        this.world = world;
        pos.set(col, row);
    }

    public void update() {
        if (!sleep) {
            if (flags.knockback) {
                checkCollisions();
                // If it doesn't collide, then update the position depending on the attacker's direction
                if (!flags.colliding) pos.update(this, direction.knockbackDirection);
                else mechanics.stopKnockback(this); // If it collides, it stops the knockback
                timer.timerKnockback(this, INTERVAL_KNOCKBACK);
            } else if (flags.hitting) hit();
            else {
                // It is important that you perform the actions before checking for collisions
                doActions();
                checkCollisions();
                if (!flags.colliding) pos.update(this, direction);
            }
            timer.checkTimers(this);
        }
    }

    public void render(GraphicsContext g2) {
        if (isOnCamera()) {
            tempScreenX = getScreenX();
            tempScreenY = getScreenY();

            // If the hostile mob that is not a boss has the life bar activated
            if (type == Type.HOSTILE && flags.hpBar && !flags.boss) game.system.ui.renderHpBar(this);

            if (flags.invincible) {
                // Without this, the bar disappears after 4 seconds, even if the player continues attacking the mob
                timer.hpBarCounter = 0;
                Utils.changeAlpha(g2, 0.4f);
            }
            if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            // If it is an animation
            if (sheet.movement != null || sheet.attack != null)
                g2.drawImage(sheet.getCurrentAnimationFrame(this), tempScreenX, tempScreenY);
                // If it is a static image (item, interactive tile)
            else g2.drawImage(sheet.frame, getScreenX(), getScreenY());

            if (game.system.keyboard.isKeyToggled(Key.RECTS)) drawRects(g2);

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
        world.entities.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, -1)); // Top left
        world.entities.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, -1)); // Top right
        world.entities.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, 1)); // Down left
        world.entities.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, 1)); // Down right
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

            int currentX = pos.x, currentY = pos.y;
            int hitboxWidth = (int) hitbox.getWidth(), hitboxHeight = (int) hitbox.getHeight();

            switch (direction) {
                case DOWN -> pos.y += (int) attackbox.getHeight();
                case UP -> pos.y -= (int) attackbox.getHeight();
                case LEFT -> pos.x -= (int) attackbox.getWidth();
                case RIGHT -> pos.x += (int) attackbox.getWidth();
            }

            hitbox.setWidth(attackbox.getWidth());
            hitbox.setHeight(attackbox.getHeight());

            hitPlayer(game.system.collisionChecker.checkPlayer(this), stats.attack);

            pos.x = currentX;
            pos.y = currentY;

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
     * Drops an object.
     *
     * @param entity entity.
     * @param item   object that drops the entity.
     */
    protected void drop(Entity entity, Item item) {
        for (int i = 0; i < world.entities.items[1].length; i++) {
            if (world.entities.items[world.map.num][i] == null) {
                world.entities.items[world.map.num][i] = item;
                world.entities.items[world.map.num][i].pos.x = (int) (pos.x + (sheet.frame.getWidth() / 2 - item.sheet.frame.getWidth() / 2));
                // Add half of the hitbox of just the mobs to the position and the item
                world.entities.items[world.map.num][i].pos.y = (int) (pos.y + (sheet.frame.getHeight() / 2 + (entity instanceof Mob ? hitbox.getHeight() / 2 : 0) - item.sheet.frame.getHeight() / 2));
                break;
            }
        }
    }

    /**
     * Hit the player.
     *
     * @param contact if the hostile mob makes contact with the player.
     * @param attack  attack points.
     */
    public void hitPlayer(boolean contact, int attack) {
        // If the entity is hostile and makes contact with the player who is not invincible
        if (type == Type.HOSTILE && contact && !world.entities.player.flags.invincible) {
            game.system.audio.playSound(Assets.getAudio(AudioAssets.PLAYER_DAMAGE));
            // Subtract the player's defense from the mob's attack to calculate fair damage
            int damage = Math.max(attack - world.entities.player.stats.defense, 1);
            world.entities.player.stats.decreaseHp(damage);
            world.entities.player.flags.invincible = true;
        }
    }

    /**
     * Check collisions.
     */
    public void checkCollisions() {
        flags.colliding = false;
        game.system.collisionChecker.checkTile(this);
        game.system.collisionChecker.checkItem(this);
        game.system.collisionChecker.checkEntity(this, world.entities.mobs);
        game.system.collisionChecker.checkEntity(this, world.entities.interactives);
        hitPlayer(game.system.collisionChecker.checkPlayer(this), stats.attack);
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
        return pos.x + tile * bossArea > world.entities.player.pos.x - X_OFFSET &&
                pos.x - tile < world.entities.player.pos.x + X_OFFSET &&
                pos.y + tile * bossArea > world.entities.player.pos.y - Y_OFFSET &&
                pos.y - tile < world.entities.player.pos.y + Y_OFFSET;
    }

    public int getScreenX() {
        return pos.x - world.entities.player.pos.x + X_OFFSET;
    }

    public int getScreenY() {
        return pos.y - world.entities.player.pos.y + Y_OFFSET;
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
        gc.setStroke(Color.YELLOW);
        gc.strokeRect(getScreenX() + hitbox.getX(), getScreenY() + hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        // Attackbox
        if (flags.hitting) {
            gc.setStroke(Color.RED);
            gc.strokeRect(getScreenX() + attackbox.getX() + hitbox.getX(),
                    getScreenY() + attackbox.getY() + hitbox.getY(),
                    attackbox.getWidth(), attackbox.getHeight());
        }
    }

}
