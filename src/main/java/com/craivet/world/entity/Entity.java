package com.craivet.world.entity;

import java.awt.*;
import java.util.ArrayList;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.gfx.SpriteSheet;
import com.craivet.physics.Mechanics;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.mob.Mob;
import com.craivet.world.World;
import com.craivet.utils.Timer;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * <p>
 * TODO Los metodos update() y render() se podrian implementar desde una interfaz
 * <p>
 * TODO No tendria que ser una clase abstracta?
 */

public class Entity extends Stats {

    public final Game game;
    public final World world;

    public SpriteSheet ss = new SpriteSheet();
    public Flags flags = new Flags();
    public Timer timer = new Timer();
    public Mechanics mechanics = new Mechanics();

    public ArrayList<Item> inventory = new ArrayList<>();
    public Entity linkedEntity;
    public boolean hpBar;
    /* La variable knockbackDirection es una variable temporal que almacena la direccion del atacante al momento del
     * ataque para actualizar la posicion de la entidad mientras el frame de esta se mantiene en la misma direccion. */
    public Direction knockbackDirection;
    public String[][] dialogues = new String[20][20];
    public int dialogueSet, dialogueIndex;

    public int tempScreenX, tempScreenY;

    public Entity(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    // TODO Verificar posicion invalida (dentro de los limites del mapa y sobre tiles que no sean solidos, ni sobre otra entidad)
    public Entity(Game game, World world, int x, int y) {
        this.game = game;
        this.world = world;

        this.x = x * tile;
        this.y = y * tile;
    }

    public void update() {
        if (flags.knockback) {
            checkCollision();
            // Si no colisiona, entonces actualiza la posicion dependiendo de la direccion del atacante
            if (!flags.colliding) updatePosition(knockbackDirection);
            else mechanics.stopKnockback(this); // Si colisiona, detiene el knockback
            timer.timerKnockback(this, INTERVAL_KNOCKBACK);
        } else if (flags.hitting) mechanics.hit(this);
        else {
            // Es importante que realize las acciones antes de comprobar las colisiones
            doActions();
            checkCollision();
            if (!flags.colliding) updatePosition(direction);
        }
        timer.checkTimers(this);
    }

    public void render(Graphics2D g2) {
        // TODO Se podria calcular desde un metodo
        screenX = (x - world.player.x) + world.player.screenX;
        screenY = (y - world.player.y) + world.player.screenY;
        if (isOnCamera()) {
            tempScreenX = screenX;
            tempScreenY = screenY;

            // Si el mob hostil tiene activada la barra de vida
            if (type == Type.HOSTILE && hpBar) game.ui.drawHpBar(g2, this);
            if (flags.invincible) {
                // Sin esto, la barra desaparece despues de 4 segundos, incluso si el player sigue atacando al mob
                timer.hpBarCounter = 0;
                Utils.changeAlpha(g2, 0.4f);
            }
            if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            // Si es una animacion
            if (ss.movement != null || ss.attack != null)
                g2.drawImage(ss.getCurrentFrame(this), tempScreenX, tempScreenY, null);
                // Si es una imagen estatica (item, interactive tile)
            else g2.drawImage(image, screenX, screenY, null);

            // drawRects(g2);

            Utils.changeAlpha(g2, 1f);
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
     * <p>
     * TODO Se podria cambiar el nombre a dropObject, ya que dropea items y objetos que dejan los tiles interactivos
     *
     * @param entity entidad.
     * @param item   item que dropea la entidad.
     */
    protected void dropItem(Entity entity, Item item) {
        for (int i = 0; i < world.items[1].length; i++) {
            if (world.items[world.map][i] == null) {
                world.items[world.map][i] = item;
                world.items[world.map][i].x = x + (image.getWidth() / 2 - item.image.getWidth() / 2);
                // Suma la mitad de la hitbox solo de los mobs a la posicion y del item
                world.items[world.map][i].y = y + (image.getHeight() / 2 + (entity instanceof Mob ? hitbox.height / 2 : 0) - item.image.getHeight() / 2);
                break;
            }
        }
    }

    /**
     * Golpea al player.
     *
     * @param contact si el mob hostil hace contacto con el player.
     * @param attack  puntos de ataque.
     */
    public void hitPlayer(boolean contact, int attack) {
        // Si la entidad es hostil y hace contacto con el player que no es invencible
        if (type == Type.HOSTILE && contact && !world.player.flags.invincible) {
            game.playSound(sound_player_damage);
            // Resta la defensa del player al ataque del mob para calcular el daño justo
            int damage = Math.max(attack - world.player.defense, 1);
            world.player.hp -= damage;
            world.player.flags.invincible = true;
        }
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
     * Verifica si la entidad esta dentro de la camara.
     *
     * @return true si la entidad esta dentro de la camara o false.
     */
    private boolean isOnCamera() {
        /* Si el render se encuentra con una entidad mas grande (por ejemplo, Skeleton) de lo normal, no lo va a
         * representar cuando el player visualice solo los pies de este, ya que la distancia del boss que comienza desde
         * la esquina superior izquierda (o del centro?) es mucha con respecto a la vision del player en pantalla. Por
         * lo tanto se aumenta esa vision multiplicando el bossArea. */
        int bossArea = 5;
        return x + tile * bossArea > world.player.x - world.player.screenX &&
                x - tile < world.player.x + world.player.screenX &&
                y + tile * bossArea > world.player.y - world.player.screenY &&
                y - tile < world.player.y + world.player.screenY;
    }

    private void drawRects(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0));
        // Frame
        g2.setColor(Color.magenta);
        g2.drawRect(screenX, screenY, image.getWidth(), image.getHeight());
        // Hitbox
        g2.setColor(Color.green);
        g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
        // Attackbox
        if (flags.hitting) {
            g2.setColor(Color.red);
            g2.drawRect(screenX + attackbox.x + hitbox.x, screenY + attackbox.y + hitbox.y, attackbox.width, attackbox.height);
        }
    }

    /**
     * Obtiene la posicion central de x.
     *
     * @return la posicion central de x.
     */
    public int getCenterX() {
        return x + image.getWidth() / 2;
    }

    /**
     * Obtiene la posicion central de y.
     *
     * @return la posicion central de y.
     */
    public int getCenterY() {
        return y + image.getHeight() / 2;
    }

}
