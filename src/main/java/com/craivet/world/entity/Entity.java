package com.craivet.world.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.gfx.Animation;
import com.craivet.gfx.Screen;
import com.craivet.gfx.SpriteSheet;
import com.craivet.physics.Mechanics;
import com.craivet.world.Position;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.mob.Mob;
import com.craivet.world.World;
import com.craivet.utils.Timer;
import com.craivet.utils.Utils;
import com.craivet.world.entity.projectile.Projectile;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * Cuando se crea un item (por ejemplo, Key) se crean todos los objetos declarados en la clase Entity (Screen, Stats,
 * Position, Flags, etc.). Algunos de estos objetos no son relevantes para el item Key, por ejemplo el objeto Flags no
 * es utilizado por Key. Por lo tanto nose si es necesario crear todos los objetos para cada entidad creada.
 */

public abstract class Entity {

    public final Game game;
    public final World world;

    public Animation down, up, left, right;
    public BufferedImage currentFrame, currentSwordFrame;

    public Projectile projectile;
    public Item weapon, shield, light;

    public Screen screen = new Screen();
    public Stats stats = new Stats();
    public Position pos = new Position();
    public SpriteSheet sheet = new SpriteSheet();
    public Flags flags = new Flags();
    public Timer timer = new Timer();
    public Mechanics mechanics = new Mechanics();
    public ArrayList<Item> inventory = new ArrayList<>();
    public Rectangle hitbox = new Rectangle(0, 0, tile, tile);
    public Rectangle attackbox = new Rectangle(0, 0, 0, 0);
    public int hitboxDefaultX, hitboxDefaultY;

    public Entity linkedEntity;
    /* La variable knockbackDirection es una variable temporal que almacena la direccion del atacante al momento del
     * ataque para actualizar la posicion de la entidad mientras el frame de esta se mantiene en la misma direccion. */
    public Direction knockbackDirection;
    public int knockbackValue;
    public String[][] dialogues = new String[20][20];
    public int dialogueSet, dialogueIndex;

    public Entity(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    // TODO Verificar posicion invalida (dentro de los limites del mapa y sobre tiles que no sean solidos, ni sobre otra entidad)
    public Entity(Game game, World world, int x, int y) {
        this.game = game;
        this.world = world;
        pos.x = x * tile;
        pos.y = y * tile;
    }

    public void update() {
        if (flags.knockback) {
            checkCollision();
            // Si no colisiona, entonces actualiza la posicion dependiendo de la direccion del atacante
            if (!flags.colliding) updatePosition(knockbackDirection);
            else mechanics.stopKnockback(this); // Si colisiona, detiene el knockback
            timer.timerKnockback(this, INTERVAL_KNOCKBACK);
        } else if (flags.hitting) hit();
        else {
            // Es importante que realize las acciones antes de comprobar las colisiones
            doActions();
            checkCollision();
            if (!flags.colliding) updatePosition(stats.direction);
        }
        timer.checkTimers(this);
    }

    public void render(Graphics2D g2) {
        // TODO Se podria calcular desde un metodo
        screen.x = (pos.x - world.player.pos.x) + world.player.screen.x;
        screen.y = (pos.y - world.player.pos.y) + world.player.screen.y;
        if (isOnCamera()) {
            screen.tempScreenX = screen.x;
            screen.tempScreenY = screen.y;

            // Si el mob hostil tiene activada la barra de vida
            if (stats.type == Type.HOSTILE && flags.hpBar) game.ui.drawHpBar(g2, this);
            if (flags.invincible) {
                // Sin esto, la barra desaparece despues de 4 segundos, incluso si el player sigue atacando al mob
                timer.hpBarCounter = 0;
                Utils.changeAlpha(g2, 0.4f);
            }
            if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            // Si es una animacion
            if (sheet.movement != null || sheet.attack != null)
                g2.drawImage(sheet.getCurrentAnimationFrame(this), screen.tempScreenX, screen.tempScreenY, null);
                // Si es una imagen estatica (item, interactive tile)
            else g2.drawImage(sheet.frame, screen.x, screen.y, null);

            drawRects(g2);

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
     * Golpea a la entidad si la attackbox en el frame de ataque colisiona con la hitbox del objetivo.
     * <p>
     * De 0 a motion1 ms se muestra el primer frame de ataque. De motion1 a motion2 ms se muestra el segundo frame de
     * ataque. Despues de motion2 vuelve al frame de movimiento. Para el caso del player solo hay un frame de ataque.
     * <p>
     * En el segundo frame de ataque, la posicion x/y se ajusta para la attackbox y verifica si colisiona con una
     * entidad.
     */
    public void hit() {
        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= stats.motion1) sheet.attackNum = 1;
        if (timer.attackAnimationCounter > stats.motion1 && timer.attackAnimationCounter <= stats.motion2) {
            sheet.attackNum = 2;

            int currentX = pos.x, currentY = pos.y;
            int hitboxWidth = hitbox.width, hitboxHeight = hitbox.height;

            switch (stats.direction) {
                case DOWN -> pos.y += attackbox.height;
                case UP -> pos.y -= attackbox.height;
                case LEFT -> pos.x -= attackbox.width;
                case RIGHT -> pos.x += attackbox.width;
            }

            hitbox.width = attackbox.width;
            hitbox.height = attackbox.height;

            hitPlayer(game.collision.checkPlayer(this), stats.attack);

            pos.x = currentX;
            pos.y = currentY;
            hitbox.width = hitboxWidth;
            hitbox.height = hitboxHeight;
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
     * TODO Se podria cambiar el nombre a dropObject, ya que dropea items y objetos que dejan los tiles interactivos
     *
     * @param entity entidad.
     * @param item   item que dropea la entidad.
     */
    protected void dropItem(Entity entity, Item item) {
        for (int i = 0; i < world.items[1].length; i++) {
            if (world.items[world.map][i] == null) {
                world.items[world.map][i] = item;
                world.items[world.map][i].pos.x = pos.x + (sheet.frame.getWidth() / 2 - item.sheet.frame.getWidth() / 2);
                // Suma la mitad de la hitbox solo de los mobs a la posicion y del item
                world.items[world.map][i].pos.y = pos.y + (sheet.frame.getHeight() / 2 + (entity instanceof Mob ? hitbox.height / 2 : 0) - item.sheet.frame.getHeight() / 2);
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
        if (stats.type == Type.HOSTILE && contact && !world.player.flags.invincible) {
            game.playSound(sound_player_damage);
            // Resta la defensa del player al ataque del mob para calcular el daño justo
            int damage = Math.max(attack - world.player.stats.defense, 1);
            world.player.stats.hp -= damage;
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
        hitPlayer(game.collision.checkPlayer(this), stats.attack);
    }

    /**
     * Actualiza la posicion de la entidad.
     *
     * @param direction direccion de la entidad.
     */
    protected void updatePosition(Direction direction) {
        switch (direction) {
            case DOWN -> pos.y += stats.speed;
            case UP -> pos.y -= stats.speed;
            case LEFT -> pos.x -= stats.speed;
            case RIGHT -> pos.x += stats.speed;
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
        return pos.x + tile * bossArea > world.player.pos.x - world.player.screen.x &&
                pos.x - tile < world.player.pos.x + world.player.screen.x &&
                pos.y + tile * bossArea > world.player.pos.y - world.player.screen.y &&
                pos.y - tile < world.player.pos.y + world.player.screen.y;
    }

    private void drawRects(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0));
        // Frame
        g2.setColor(Color.magenta);
        g2.drawRect(screen.x, screen.y, sheet.frame.getWidth(), sheet.frame.getHeight());
        // Hitbox
        g2.setColor(Color.green);
        g2.drawRect(screen.x + hitbox.x, screen.y + hitbox.y, hitbox.width, hitbox.height);
        // Attackbox
        if (flags.hitting) {
            g2.setColor(Color.red);
            g2.drawRect(screen.x + attackbox.x + hitbox.x, screen.y + attackbox.y + hitbox.y, attackbox.width, attackbox.height);
        }
    }

    /**
     * Obtiene la posicion central de x.
     *
     * @return la posicion central de x.
     */
    public int getCenterX() {
        return pos.x + sheet.frame.getWidth() / 2;
    }

    /**
     * Obtiene la posicion central de y.
     *
     * @return la posicion central de y.
     */
    public int getCenterY() {
        return pos.y + sheet.frame.getHeight() / 2;
    }

}
