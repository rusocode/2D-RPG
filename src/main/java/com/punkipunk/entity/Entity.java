package com.punkipunk.entity;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.components.Flags;
import com.punkipunk.entity.components.Particle;
import com.punkipunk.entity.components.Stats;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.mob.MobCategory;
import com.punkipunk.entity.spells.Spell;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.Renderer2D;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.gfx.SpriteSheet.SpriteRegion;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.physics.Mechanics;
import com.punkipunk.utils.Timer;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.Position;
import com.punkipunk.world.World;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

public abstract class Entity {

    // Referencias
    public final IGame game;
    public final World world;

    // Componentes
    public final Stats stats;
    public final Flags flags;
    public final SpriteSheet sheet; // TODO Tiene que ir aca?
    public final Timer timer;
    public final Mechanics mechanics;
    public final Position position; // TODO Podria ir en World
    public Rectangle hitbox = new Rectangle(0, 0, tile, tile); // TODO Se crea aca?
    public Rectangle attackbox = new Rectangle(0, 0, 0, 0);
    public Direction direction;

    /** Animacion para entidades con multiplis frames por direccion (Player, Orc, etc.). */
    public Animation down, up, left, right; // Objetos Animation que manejan el indice internamente

    /**
     * Indice del frame actual dentro del array de frames de una dirección especifica que sirve para mantener compatibilidad con
     * el sistema nuevo del sprite sheet.
     */
    public int animationIndex;

    /** Region actual del sprite sheet. */
    public SpriteRegion currentFrame;

    /**
     * Variables temporales para ajustes de renderizado, en donde convierten la posicion del mundo a coordenadas de pantalla. La
     * camara sigue al player, todo se calcula relativo a el.
     */
    public int tempScreenX, tempScreenY;

    public String soundHit, soundDeath = AudioID.Sound.MOB_DEATH;
    public MobCategory mobCategory; // TODO Se podria mover a Mob?
    public Dialogue dialogue;
    public double hitboxDefaultX, hitboxDefaultY;
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

    public Entity(IGame game, World world, int... pos) {
        this.game = game;
        this.world = world;
        this.stats = new Stats();
        this.flags = new Flags();
        this.sheet = new SpriteSheet();
        this.timer = new Timer();
        this.mechanics = new Mechanics();
        this.position = new Position();
        this.direction = Direction.DOWN;

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
            updateAnimation(); // Actualiza el indice de animacion
        }
    }

    public void render(Renderer2D renderer) {
        // Verifica si la entidad esta dentro del area visible de la camara, si esta fuera, no la renderiza (optimizacion)
        if (!isOnCamera()) return;

        // 1. Calcula posicion en pantalla
        tempScreenX = getScreenX();
        tempScreenY = getScreenY();

        // 2. Renderiza la barra de vida si esta activada y no es un boss
        if (flags.hpBar && !flags.boss) game.getGameSystem().ui.renderHpBar(this);

        // 3. Aplica efectos visuales
        if (flags.invincible) {
            // Without this, the bar disappears after 4 seconds, even if the player continues attacking the mob
            timer.hpBarCounter = 0;
            Utils.changeAlpha(renderer, 0.4f);
        }

        if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, renderer);

        // 4. Obtiene el frame actual
        SpriteRegion currentFrame = getCurrentFrame();

        // 5. Renderiza el sprite
        if (currentFrame != null && sheet.getTexture() != null) renderSprite(renderer, currentFrame);

        // 6. Debug: renderiza hitbox y attackbox
        if (game.getGameSystem().keyboard.isKeyToggled(Key.RECTS)) drawRects(renderer);

        // 7. Restaura alpha si fue cambiado
        Utils.changeAlpha(renderer, 1f); // TODO Cambiar por renderer.setAlpha(1.0f);

    }

    protected void renderRegion(Renderer2D renderer, SpriteSheet.SpriteRegion region, int x, int y) {
        float sx = region.u0 * sheet.getTexture().getWidth();
        float sy = region.v0 * sheet.getTexture().getHeight();
        float sw = (region.u1 - region.u0) * sheet.getTexture().getWidth();
        float sh = (region.v1 - region.v0) * sheet.getTexture().getHeight();
        renderer.drawImage(sheet.getTexture(), sx, sy, sw, sh, x, y, region.width, region.height);
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

            hitPlayer(this, game.getGameSystem().collisionChecker.checkPlayer(this), stats.attack);

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
     * Golpea al player.
     * <p>
     *
     * @param entity  entidad con la que hace contacto el player.
     * @param contact si la entidad hace contacto con el player.
     * @param attack  ataque de la entidad.
     */
    public void hitPlayer(Entity entity, boolean contact, int attack) {
        if (entity.mobCategory != MobCategory.NPC && entity.mobCategory != MobCategory.PEACEFUL && contact && !world.entitySystem.player.flags.invincible) {
            game.getGameSystem().audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
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
        game.getGameSystem().collisionChecker.checkTile(this);
        game.getGameSystem().collisionChecker.checkItem(this);
        game.getGameSystem().collisionChecker.checkMob(this);
        game.getGameSystem().collisionChecker.checkInteractive(this);
        if (game.getGameSystem().collisionChecker.checkPlayer(this)) {
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

        /*
        int screenX = position.x - world.entitySystem.player.position.x + X_OFFSET;
        int screenY = position.y - world.entitySystem.player.position.y + Y_OFFSET;

        return screenX + tile > 0 &&      // No está a la izquierda
               screenX < WINDOW_WIDTH &&  // No está a la derecha
               screenY + tile > 0 &&      // No está arriba
               screenY < WINDOW_HEIGHT;   // No está abajo
        */
    }

    /**
     * Obtiene la posicion X en pantalla relativo al player.
     */
    public int getScreenX() {
        return position.x - world.entitySystem.player.position.x + X_OFFSET;
    }

    /**
     * Obtiene la posicion Y en pantalla relativo al player.
     */
    public int getScreenY() {
        return position.y - world.entitySystem.player.position.y + Y_OFFSET;
    }

    /**
     * Gets the central position of x of the entity.
     *
     * @return the center x position of the entity.
     */
    public int getCenterX() {
        return (int) (position.x + currentFrame.width / 2);
    }

    /**
     * Gets the central position of y of the entity.
     *
     * @return the central position of y of the entity.
     */
    public int getCenterY() {
        return (int) (position.y + currentFrame.height / 2);
    }

    /**
     * Perform a sequence of actions.
     */
    protected void doActions() {
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
     * Dropea un item.
     * <p>
     * FIXME El item no siempre dropea en el centro de la entidad
     *
     * @param item item a dropear
     */
    protected void drop(Item item) {
        // Ajusta la posicion del item en el centro del tile
        int x = (int) (position.x + (currentFrame.width / 2)) / tile;
        int y = (int) (position.y + (currentFrame.height / 2)) / tile;
        if (item.amount > 0) world.entitySystem.createItemWithAmount(item.getID(), world.map.id, item.amount, x, y);
        else world.entitySystem.createItem(item.getID(), world.map.id, x, y);
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

    /**
     * Obtiene el frame actual segun el tipo de animacion.
     * <p>
     * Tipos de animacion:
     * <ol>
     *  <li>Multiples frames por direccion (Player, Orc): usa getCurrentAnimationFrame2()
     *  <li>Dos frames por direccion (la mayoria de mobs): usa getCurrentAnimationFrame()
     *  <li>Frame estatico (items, interactivos): usa sheet.frame
     * </ol>
     *
     * @return SpriteRegion del frame actual
     */
    private SpriteRegion getCurrentFrame() {
        // Tipo 1: Animacion con multiples frames para cada direccion (Player, Orc, etc.)
        if (down != null && up != null && left != null && right != null) return sheet.getCurrentAnimationFrame2(this);
            // Tipo 2: Animacion simple de 2 frames para cada direccion (la mayoria de mobs)
        else if (sheet.movement != null /* || sheet.attack != null */) return sheet.getCurrentAnimationFrame(this);
            // Tipo 3: Frame estatico (items, interactivos)
        else return sheet.frame;
    }

    /**
     * Renderiza el sprite usando la region del sprite sheet.
     *
     * @param renderer renderer 2D
     * @param region   region del sprite a renderizar
     */
    private void renderSprite(Renderer2D renderer, SpriteRegion region) {
        // Calcula coordenadas fuente en pixeles desde las coordenadas UV
        float sourceX = region.u0 * sheet.getTexture().getWidth();
        float sourceY = region.v0 * sheet.getTexture().getHeight();
        float sourceWidth = (region.u1 - region.u0) * sheet.getTexture().getWidth();
        float sourceHeight = (region.v1 - region.v0) * sheet.getTexture().getHeight();

        // Renderiza usando drawImage con region
        renderer.drawImage(
                sheet.getTexture(),    // Textura completa del sprite sheet
                sourceX,               // x fuente (pixeles)
                sourceY,               // y fuente (pixeles)
                sourceWidth,           // ancho fuente (pixeles)
                sourceHeight,          // alto fuente (pixeles)
                tempScreenX,           // x destino (pantalla)
                tempScreenY,           // y destino (pantalla)
                region.width,          // ancho destino (ya incluye el escalado)
                region.height          // alto destino (ya incluye el escalado)
        );
    }

    /**
     * Actualiza el indice de animacion para entidades con multiples frames.
     * <p>
     * Este metodo se llama en cada frame y avanza el indice de animacion segun la velocidad de animacion configurada.
     */
    protected void updateAnimation() {
        // Si la entidad usa objetos Animation (Player, Orc, etc.)
        if (down != null && up != null && left != null && right != null) {
            // Los objetos Animation manejan su propio indice internamente
            switch (direction) {
                case DOWN -> down.tick();
                case UP -> up.tick();
                case LEFT -> left.tick();
                case RIGHT -> right.tick();
            }

            // Sincroniza animationIndex con el indice del Animation actual
            Animation currentAnimation = getCurrentAnimation();
            if (currentAnimation != null) animationIndex = currentAnimation.getIndex();

        }
        // Si usa el sistema simple de 2 frames (la mayoria de mobs)
        else if (sheet.movement != null) {
            // El Timer ya maneja movementNum (1 o 2) en timer.timeMovement(), por lo tanto no necesitamos hacer nada aqui
        }
    }

    /**
     * Obtiene el objeto Animation actual segun la direccion.
     */
    private Animation getCurrentAnimation() {
        return switch (direction) {
            case DOWN -> down;
            case UP -> up;
            case LEFT -> left;
            case RIGHT -> right;
            case ANY -> null;
        };
    }

    /**
     * Draw the rectangles that represent the frames, hitbox and attackbox.
     */
    private void drawRects(Renderer2D renderer) {
        // Frame
        renderer.setStroke(Color.MAGENTA);
        renderer.setLineWidth(1);
        renderer.strokeRect(getScreenX(), getScreenY(), currentFrame.width, currentFrame.height);
        // Hitbox
        renderer.setStroke(Color.BLUE);
        renderer.strokeRect(getScreenX() + hitbox.getX(), getScreenY() + hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        // Attackbox
        if (flags.hitting) {
            renderer.setStroke(Color.RED);
            renderer.strokeRect(getScreenX() + attackbox.getX() + hitbox.getX(),
                    getScreenY() + attackbox.getY() + hitbox.getY(),
                    attackbox.getWidth(), attackbox.getHeight());
        }
    }

}
