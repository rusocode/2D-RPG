package com.craivet.world.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.gfx.SpriteSheet;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.mob.Mob;
import com.craivet.world.entity.mob.Slime;
import com.craivet.world.tile.Interactive;
import com.craivet.world.World;
import com.craivet.utils.Timer;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * TODO Los metodos para obtener las subimagenes deberian ir en otra clase
 * <p>
 * TODO Los metodos update() y render() se podrian implementar desde una interfaz
 * <p>
 * TODO No tendria que ser una clase abstracta?
 */

public class Entity extends Stats {

    protected final Game game;
    protected final World world;

    public SpriteSheet ss = new SpriteSheet();
    public Flags flags = new Flags();
    public Timer timer = new Timer();

    public ArrayList<Item> inventory = new ArrayList<>();
    public Entity linkedEntity;
    public boolean hpBar;
    /* La variable knockbackDirection es una variable temporal que almacena la direccion del atacante al momento del
     * ataque para actualizar la posicion de la entidad mientras el frame de esta se mantiene en la misma direccion. */
    public Direction knockbackDirection;
    public String[][] dialogues = new String[20][20];
    public int dialogueSet, dialogueIndex;

    public Entity(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public Entity(Game game, World world, int x, int y) {
        this.game = game;
        this.world = world;
        // TODO Verificar posicion invalida (dentro de los limites del mapa y sobre tiles que no sean solidos, ni sobre otra entidad)
        this.x = x * tile;
        this.y = y * tile;
    }

    // TODO Creo que el update y render se pueden mover a mob
    public void update() {
        if (flags.knockback) {
            checkCollision();
            // Si no colisiona, entonces actualiza la posicion dependiendo de la direccion del atacante
            if (!flags.colliding) updatePosition(knockbackDirection);
            else stopKnockback(); // Si colisiona, detiene el knockback
            timer.timerKnockback(this, INTERVAL_KNOCKBACK);
        } else if (flags.hitting) hit();
        else {
            // Es importante que realize las acciones antes de comprobar las colisiones
            doActions();
            checkCollision();
            if (!flags.colliding) updatePosition(direction);
        }
        checkTimers();
    }

    public void render(Graphics2D g2) {
        // TODO Se podria calcular desde un metodo
        screenX = (x - world.player.x) + world.player.screenX;
        screenY = (y - world.player.y) + world.player.screenY;
        if (isOnCamera()) {
            tempScreenX = screenX;
            tempScreenY = screenY;

            // Si el mob hostil tiene activada la barra de vida
            if (type == Type.HOSTILE && hpBar) drawHpBar(g2);
            if (flags.invincible) {
                // Sin esto, la barra desaparece despues de 4 segundos, incluso si el player sigue atacando al mob
                timer.hpBarCounter = 0;
                if (!(this instanceof Interactive)) Utils.changeAlpha(g2, 0.4f);
            }
            if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            // Si es una animacion
            if (ss.movement != null || ss.weapon != null)
                g2.drawImage(getCurrentFrame(), tempScreenX, tempScreenY, null);
                // Si es una imagen estatica
            else g2.drawImage(image, screenX, screenY, null);

            drawRects(g2);

            Utils.changeAlpha(g2, 1);
        }
    }

    /**
     * Realiza una secuencia de acciones.
     */
    protected void doActions() {
    }

    /**
     * Comprueba si dropeo un item.
     */
    public void checkDrop() {
    }

    public void startDialogue(int state, Entity entity, int set) {
        game.state = state;
        game.ui.entity = entity;
        dialogueSet = set;
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
     * Genera 4 particulas en el objetivo.
     *
     * @param generator entidad que va a generar las particulas.
     * @param target    objetivo en donde se van a generar las particulas.
     */
    protected void generateParticle(Entity generator, Entity target) {
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, -1)); // Top left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, -1)); // Top right
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, 1)); // Down left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, 1)); // Down right
    }

    /**
     * Dropea un item.
     *
     * @param entity entidad.
     * @param item   item a dropear.
     */
    protected void dropItem(Entity entity, Item item) {
        for (int i = 0; i < world.items[1].length; i++) {
            if (world.items[world.map][i] == null) {
                world.items[world.map][i] = item;
                if (entity instanceof Mob) {
                    world.items[world.map][i].x = x + (mobImage.getWidth() / 2 - item.image.getWidth() / 2);
                    world.items[world.map][i].y = y + (mobImage.getHeight() / 2 - item.image.getHeight() / 2) + 11;
                } else if (entity instanceof Interactive) {
                    world.items[world.map][i].x = x + (image.getWidth() / 2 - item.image.getWidth() / 2);
                    world.items[world.map][i].y = y + (image.getHeight() / 2 - item.image.getHeight() / 2);
                }
                break;
            }
        }
    }

    /**
     * Golpea a la entidad si la attackbox en el frame de ataque colisiona con la hitbox del objetivo.
     * <p>
     * De 0 a motion1 ms se muestra el primer frame de ataque. De motion1 a motion2 ms se muestra el segundo frame de
     * ataque. Despues de motion2 vuelve al frame de movimiento. Para el caso del player solo hay un frame de ataque.
     * <p>
     * En el segundo frame de ataque, la posicion x/y se ajusta para la attackbox y verifica si colisiona con una
     * entidad.
     * <p>
     * TODO Se podria mover a mecanica
     */
    protected void hit() {
        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= motion1) ss.attackNum = 1; // (de 0-motion1ms frame de ataque 1)
        if (timer.attackAnimationCounter > motion1 && timer.attackAnimationCounter <= motion2) { // (de motion1-motion2ms frame de ataque 2)
            ss.attackNum = 2;

            // Guarda la posicion actual de x/y y el tamaño de la hitbox
            int currentX = x, currentY = y;
            int hitboxWidth = hitbox.width, hitboxHeight = hitbox.height;

            /* Ajusta la attackbox (en la hoja de la espada para ser mas especificos) del player dependiendo de la
             * direccion de ataque. Es importante aclarar que las coordenadas x/y de la attackbox parten de la esquina
             * superior izquierda de la hitbox del player (nose si es necesario partir desde esa esquina). */
            if (type == Type.PLAYER) {
                switch (direction) {
                    case DOWN -> {
                        attackbox.x = -1;
                        attackbox.y = 4;
                        attackbox.width = 4;
                        attackbox.height = 36;
                    }
                    case UP -> {
                        attackbox.x = 12;
                        attackbox.y = -43;
                        attackbox.width = 4;
                        attackbox.height = 42;
                    }
                    case LEFT -> {
                        attackbox.x = -20;
                        attackbox.y = 0;
                        attackbox.width = 19;
                        attackbox.height = 4;
                    }
                    case RIGHT -> {
                        attackbox.x = 10;
                        attackbox.y = 2;
                        attackbox.width = 19;
                        attackbox.height = 4;
                    }
                }
                /* Acumula la posicion de la attackbox a la posicion del player para verificar la colision con las
                 * coordenas ajustadas de la attackbox. */
                x += attackbox.x;
                y += attackbox.y;
            } else if (type == Type.HOSTILE) {
                switch (direction) {
                    case DOWN -> y += attackbox.height;
                    case UP -> y -= attackbox.height;
                    case LEFT -> x -= attackbox.width;
                    case RIGHT -> x += attackbox.width;
                }
            }

            // Convierte la hitbox (el ancho y alto) en la attackbox para verificar la colision solo con la attackbox
            hitbox.width = attackbox.width;
            hitbox.height = attackbox.height;

            if (type == Type.HOSTILE) hitPlayer(game.collision.checkPlayer(this), attack);
            else {
                // Verifica la colision con el mob usando la posicion y tamaño de la hitbox actualizada, osea con la attackbox
                int mobIndex = game.collision.checkEntity(this, world.mobs);
                world.player.hitMob(mobIndex, this, weapon.knockbackValue, attack);

                int interactiveIndex = game.collision.checkEntity(this, world.interactives);
                world.player.hitInteractive(interactiveIndex);

                int projectileIndex = game.collision.checkEntity(this, world.projectiles);
                world.player.hitProjectile(projectileIndex);
            }

            // Despues de verificar la colision, resetea los datos originales
            x = currentX;
            y = currentY;
            hitbox.width = hitboxWidth;
            hitbox.height = hitboxHeight;
        }
        if (timer.attackAnimationCounter > motion2) {
            ss.attackNum = 1;
            timer.attackAnimationCounter = 0;
            flags.hitting = false;
        }
    }

    /**
     * Golpea al player.
     *
     * @param contact si el mob hostil hace contacto con el player.
     * @param attack  puntos de ataque.
     */
    protected void hitPlayer(boolean contact, int attack) {
        // Si la entidad es hostil y hace contacto con el player que no es invencible
        if (type == Type.HOSTILE && contact && !world.player.flags.invincible) {
            game.playSound(sound_player_damage);
            // Resta la defensa del player al ataque del mob para calcular el daño justo
            int damage = Math.max(attack - world.player.defense, 1);
            world.player.hp -= damage;
            world.player.flags.invincible = true;
        }
    }

    private void checkTimers() {
        timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);
        if (flags.invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
        if (timer.projectileCounter < INTERVAL_PROJECTILE) timer.projectileCounter++;
    }

    /**
     * Comprueba las colisiones.
     */
    public void checkCollision() {
        flags.colliding = false;
        game.collision.checkTile(this);
        game.collision.checkItem(this);
        game.collision.checkEntity(this, world.mobs);
        game.collision.checkEntity(this, world.interactives);
        hitPlayer(game.collision.checkPlayer(this), attack);
    }

    /**
     * Actualiza la posicion de la entidad.
     *
     * @param direction direccion de la entidad.
     */
    protected void updatePosition(Direction direction) {
        switch (direction) {
            case DOWN -> y += speed;
            case UP -> y -= speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
        }
    }

    /**
     * Establece el knockback al objetivo del atacante.
     *
     * @param target         objetivo del atacante.
     * @param attacker       atacante de la entidad.
     * @param knockbackValue valor de knockback.
     */
    protected void setKnockback(Entity target, Entity attacker, int knockbackValue) {
        target.knockbackDirection = attacker.direction;
        target.speed += knockbackValue;
        target.flags.knockback = true;
    }

    private void stopKnockback() {
        flags.knockback = false;
        speed = defaultSpeed;
        timer.knockbackCounter = 0;
    }

    private void drawHpBar(Graphics2D g2) {
        double oneScale = (double) tile / maxHp;
        double hpBarValue = oneScale * hp;

        /* En caso de que el valor de la barra de vida calculado sea menor a 0, le asigna 0 para que no se
         * dibuje como valor negativo hacia la izquierda. */
        if (hpBarValue < 0) hpBarValue = 0;

        g2.setColor(new Color(35, 35, 35));
        g2.fillRect(screenX - 1, screenY + tile + 4, tile + 2, 7);

        g2.setColor(new Color(255, 0, 30));
        g2.fillRect(screenX, screenY + tile + 5, (int) hpBarValue, 5);

        timer.timeHpBar(this, INTERVAL_HP_BAR);
    }

    /**
     * Verifica si la entidad esta dentro de la camara.
     *
     * @return true si la entidad esta dentro de la camara o false.
     */
    private boolean isOnCamera() {
        return x + tile > world.player.x - world.player.screenX &&
                x - tile < world.player.x + world.player.screenX &&
                y + tile > world.player.y - world.player.screenY &&
                y - tile < world.player.y + world.player.screenY;
    }

    /**
     * Obtiene el frame actual.
     *
     * @return el frame actual.
     */
    private BufferedImage getCurrentFrame() {
        int frameIndex = 0;

        if (!flags.hitting) {
            if (ss.movement.length == 2) { // Si se trata de entidades de dos frames
                switch (direction) {
                    case DOWN, UP, LEFT, RIGHT -> frameIndex = ss.movementNum == 1 || flags.colliding ? 0 : 1;
                }
            } else {
                switch (direction) {
                    case DOWN -> frameIndex = ss.movementNum == 1 || flags.colliding ? 0 : 1;
                    case UP -> frameIndex = ss.movementNum == 1 || flags.colliding ? 2 : 3;
                    case LEFT -> frameIndex = ss.movementNum == 1 || flags.colliding ? 4 : 5;
                    case RIGHT -> frameIndex = ss.movementNum == 1 || flags.colliding ? 6 : 7;
                }
            }
        } else {
            switch (direction) {
                case DOWN -> frameIndex = ss.attackNum == 1 ? 0 : 1;
                case UP -> {
                    tempScreenY -= tile;
                    frameIndex = ss.attackNum == 1 ? 2 : 3;
                }
                case LEFT -> {
                    tempScreenX -= tile;
                    frameIndex = ss.attackNum == 1 ? 4 : 5;
                }
                case RIGHT -> frameIndex = ss.attackNum == 1 ? 6 : 7;
            }
        }

        return !flags.hitting ? ss.movement[frameIndex] : ss.weapon[frameIndex];
    }

    private void drawRects(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0));
        // Hitbox
        g2.setColor(Color.blue);
        g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
    }

}
