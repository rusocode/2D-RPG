package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.craivet.Game;
import com.craivet.entity.item.Item;
import com.craivet.entity.projectile.Projectile;
import com.craivet.gfx.SpriteSheet;
import com.craivet.tile.Interactive;
import com.craivet.World;
import com.craivet.utils.Timer;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * Crea los componentes para cada entidad.
 *
 * <p>TODO Los metodos para obtener las subimagenes deberian ir en otra clase
 * <p>TODO Los metodos update() y render() se podrian implementar desde una interfaz
 */

public abstract class Entity {

    protected final Game game;
    protected final World world;

    public Timer timer = new Timer();
    public ArrayList<Entity> inventory = new ArrayList<>();
    public String[] dialogues = new String[20];
    public int dialogueIndex;

    // General attributes
    public int x, y;
    public String name;
    public int type = TYPE_MOB;
    // Imagenes estaticas para los items y mobs
    public BufferedImage image, image2, mobImage;
    public int direction = DOWN;
    public int speed, defaultSpeed;
    public int life, maxLife; // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)
    public int mana, maxMana;
    public int ammo;
    public int lvl, exp, nextLvlExp;
    public int coin;
    public int strength, dexterity;
    public int attack, defense;
    public Rectangle hitbox = new Rectangle(0, 0, 48, 48), attackbox = new Rectangle(0, 0, 0, 0);
    public int hitboxDefaultX, hitboxDefaultY;
    public Projectile projectile; // TODO Es necesario declarar este objeto aca?
    public Entity weapon, shield;
    public Entity light; // linterna, antorcha, etc.

    // Item attributes
    public String description;
    public int price;
    public int attackValue, defenseValue;
    public int knockbackValue;
    public int amount = 1;
    public int lightRadius;
    public boolean solid, stackable;

    // States
    public boolean attacking;
    public boolean alive = true;
    public boolean collision;
    public boolean dead;
    public boolean hpBar;
    public boolean invincible;
    public boolean knockback;
    public boolean onPath;

    public int knockbackDirection;

    // Frames
    public BufferedImage movementDown1, movementDown2, movementUp1, movementUp2, movementLeft1, movementLeft2, movementRight1, movementRight2;
    public BufferedImage attackDown1, attackDown2, attackUp1, attackUp2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public int movementNum = 1, attackNum = 1;

    public Entity(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * En caso de que se haya aplicado knockback a la entidad, comprueba las colisiones y en base a eso determina si se
     * detiene el knockback o se actualiza la posicion de la entidad utilizando la variable temporal knockbackDirection.
     * En caso contrario, la entidad realiza una accion y comprueba las colisiones para determinar si se actualiza la
     * posicion o no.
     */
    public void update() {
        if (knockback) {
            checkCollisions();
            if (collision) {
                knockback = false;
                speed = defaultSpeed;
                timer.knockbackCounter = 0;
            } else updatePosition(knockbackDirection);
            timer.timerKnockback(this, INTERVAL_KNOCKBACK);
        } else {
            setAction(); // TIENE QUE REALIZAR UNA ACCION ANTES DE VERIFICAR LA COLISION
            checkCollisions();
            if (!collision) updatePosition(direction);

            timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);
            if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
            if (timer.projectileCounter < INTERVAL_PROJECTILE_ATTACK) timer.projectileCounter++;
        }
    }

    public void render(Graphics2D g2) {
        BufferedImage auxImage = null;
        int screenX = (x - world.player.x) + world.player.screenX;
        int screenY = (y - world.player.y) + world.player.screenY;
        if (x + tile_size > world.player.x - world.player.screenX &&
                x - tile_size < world.player.x + world.player.screenX &&
                y + tile_size > world.player.y - world.player.screenY &&
                y - tile_size < world.player.y + world.player.screenY) {
            switch (direction) {
                case DOWN -> auxImage = movementNum == 1 || collision ? movementDown1 : movementDown2;
                case UP -> auxImage = movementNum == 1 || collision ? movementUp1 : movementUp2;
                case LEFT -> auxImage = movementNum == 1 || collision ? movementLeft1 : movementLeft2;
                case RIGHT -> auxImage = movementNum == 1 || collision ? movementRight1 : movementRight2;
            }

            // Si la barra de hp esta activada
            if (type == TYPE_MOB && hpBar) {

                double oneScale = (double) tile_size / maxLife;
                double hpBarValue = oneScale * life;

                /* En caso de que el valor de la barra de vida calculada sea menor a 0, le asigna 0 para que no se
                 * dibuje como valor negativo hacia la izquierda. */
                if (hpBarValue < 0) hpBarValue = 0;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY + tile_size + 4, tile_size + 2, 7);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY + tile_size + 5, (int) hpBarValue, 5);

                timer.timeHpBar(this, INTERVAL_HP_BAR);
            }
            if (invincible) {
                // Sin esto, la barra desaparece despues de 4 segundos, incluso si el player sigue atacando al mob
                timer.hpBarCounter = 0;
                if (!(this instanceof Interactive)) Utils.changeAlpha(g2, 0.4f);
            }
            if (dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            g2.drawImage(auxImage, screenX, screenY, null);
            g2.drawImage(image, screenX, screenY, null); // TODO Es eficiente esto?
            // g2.setColor(Color.cyan);
            // g2.drawRect(hitbox.x + screenX, hitbox.y + screenY, hitbox.width, hitbox.height);
            // g2.drawRect(screenX, screenY, tile_size, tile_size);
            Utils.changeAlpha(g2, 1);
        }
    }

    public void setAction() {

    }

    public void damageReaction() {
    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) dialogueIndex = 0;
        game.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        switch (world.player.direction) {
            case DOWN -> direction = UP;
            case UP -> direction = DOWN;
            case LEFT -> direction = RIGHT;
            case RIGHT -> direction = LEFT;
        }
    }

    public boolean use(Entity entity) {
        return false;
    }

    public void checkDrop() {
    }

    /**
     * Dropea el item.
     *
     * @param item el item.
     */
    public void dropItem(Item item) {
        for (int i = 0; i < world.items[1].length; i++) {
            if (world.items[world.map][i] == null) {
                world.items[world.map][i] = item;
                world.items[world.map][i].x = x + (mobImage.getWidth() / 2 - item.image.getWidth() / 2);
                world.items[world.map][i].y = y + (mobImage.getHeight() / 2 - item.image.getHeight() / 2) + 11;
                break;
            }
        }
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

    public void interact() {

    }

    /**
     * Detecta si el item especificado se encuentra en la posicion adyacente del player.
     *
     * @param user       el player.
     * @param target     lista de items.
     * @param targetName nombre del item especificado.
     * @return el indice del item especificado a la posicion adyacente del player.
     */
    public int getDetected(Entity user, Entity[][] target, String targetName) {
        int index = -1;

        // Verifica el item adyacente al usuario
        int nextWorldX = user.getLeft();
        int nextWorldY = user.getTop();

        switch (user.direction) {
            case DOWN -> nextWorldY = user.getBottom() + user.speed;
            case UP -> nextWorldY = user.getTop() - user.speed;
            case LEFT -> nextWorldX = user.getLeft() - user.speed;
            case RIGHT -> nextWorldX = user.getRight() + user.speed;
        }

        int row = nextWorldY / tile_size;
        int col = nextWorldX / tile_size;

        // Si el item iterado es igual a la posicion adyacente del usuario
        for (int i = 0; i < target[1].length; i++) {
            if (target[world.map][i] != null) {
                if (target[world.map][i].getRow() == row && target[world.map][i].getCol() == col && target[world.map][i].name.equals(targetName)) {
                    index = i;
                    break;
                }
            }
        }

        return index;

    }

    /**
     * Genera 4 particulas en el objetivo.
     *
     * @param generator la entidad que va a generar las particulas.
     * @param target    el objetivo en donde se van a generar las particulas.
     */
    public void generateParticle(Entity generator, Entity target) {
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, -1)); // Top left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, -1)); // Top right
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, 1)); // Down left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, 1)); // Down right
    }

    /**
     * Daña al player.
     *
     * @param contact si el mob hace contacto con el player.
     * @param attack  puntos de ataque.
     */
    public void damagePlayer(boolean contact, int attack) {
        // Si el mob hace contacto con el player que no es invencible
        if (type == TYPE_MOB && contact && !world.player.invincible) {
            game.playSound(sound_receive_damage);
            // Resta la defensa del player al ataque del mob para calcular el daño justo
            int damage = Math.max(attack - world.player.defense, 0);
            world.player.life -= damage;
            world.player.invincible = true;
        }
    }

    /**
     * Inicializa las subimagenes de movimiento del SpriteSheet y escala cada una.
     *
     * @param image  el SpriteSheet.
     * @param width  el ancho de la subimagen.
     * @param height el alto de la subimagen.
     * @param scale  el valor de scala.
     */
    public void initMovementImages(SpriteSheet image, int width, int height, int scale) {
        BufferedImage[] subimages = SpriteSheet.getMovementSubimages(image, width, height);
        if (subimages.length > 2) {
            movementDown1 = Utils.scaleImage(subimages[0], scale, scale);
            movementDown2 = Utils.scaleImage(subimages[1], scale, scale);
            movementUp1 = Utils.scaleImage(subimages[2], scale, scale);
            movementUp2 = Utils.scaleImage(subimages[3], scale, scale);
            movementLeft1 = Utils.scaleImage(subimages[4], scale, scale);
            movementLeft2 = Utils.scaleImage(subimages[5], scale, scale);
            movementRight1 = Utils.scaleImage(subimages[6], scale, scale);
            movementRight2 = Utils.scaleImage(subimages[7], scale, scale);
        } else if (subimages.length == 2) { // Slime
            movementDown1 = Utils.scaleImage(subimages[0], scale, scale);
            movementDown2 = Utils.scaleImage(subimages[1], scale, scale);
            movementUp1 = Utils.scaleImage(subimages[0], scale, scale);
            movementUp2 = Utils.scaleImage(subimages[1], scale, scale);
            movementLeft1 = Utils.scaleImage(subimages[0], scale, scale);
            movementLeft2 = Utils.scaleImage(subimages[1], scale, scale);
            movementRight1 = Utils.scaleImage(subimages[0], scale, scale);
            movementRight2 = Utils.scaleImage(subimages[1], scale, scale);
        } else { // SickyBall
            movementDown1 = Utils.scaleImage(subimages[0], scale, scale);
            movementDown2 = Utils.scaleImage(subimages[0], scale, scale);
            movementUp1 = Utils.scaleImage(subimages[0], scale, scale);
            movementUp2 = Utils.scaleImage(subimages[0], scale, scale);
            movementLeft1 = Utils.scaleImage(subimages[0], scale, scale);
            movementLeft2 = Utils.scaleImage(subimages[0], scale, scale);
            movementRight1 = Utils.scaleImage(subimages[0], scale, scale);
            movementRight2 = Utils.scaleImage(subimages[0], scale, scale);
        }
    }

    /**
     * Inicializa las subimagenes de ataque del sprite sheet y escala cada una.
     *
     * @param image  el sprite sheet.
     * @param width  el ancho de la subimagen.
     * @param height el alto de la subimagen.
     */
    public void initAttackImages(SpriteSheet image, int width, int height) {
        BufferedImage[] subimages = SpriteSheet.getAttackSubimages(image, width, height);
        attackDown1 = Utils.scaleImage(subimages[0], tile_size, tile_size * 2);
        attackDown2 = Utils.scaleImage(subimages[1], tile_size, tile_size * 2);
        attackUp1 = Utils.scaleImage(subimages[2], tile_size, tile_size * 2);
        attackUp2 = Utils.scaleImage(subimages[3], tile_size, tile_size * 2);
        attackLeft1 = Utils.scaleImage(subimages[4], tile_size * 2, tile_size);
        attackLeft2 = Utils.scaleImage(subimages[5], tile_size * 2, tile_size);
        attackRight1 = Utils.scaleImage(subimages[6], tile_size * 2, tile_size);
        attackRight2 = Utils.scaleImage(subimages[7], tile_size * 2, tile_size);
    }

    private void checkCollisions() {
        collision = false;
        game.collider.checkTile(this);
        game.collider.checkItem(this);
        game.collider.checkEntity(this, world.npcs);
        game.collider.checkEntity(this, world.mobs);
        game.collider.checkEntity(this, world.interactives);
        damagePlayer(game.collider.checkPlayer(this), attack);
    }

    /**
     * Actualiza la posicion de la entidad dependiendo de la direccion.
     *
     * @param direction direccion.
     */
    private void updatePosition(int direction) {
        switch (direction) {
            case DOWN -> y += speed;
            case UP -> y -= speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
        }
    }

    /**
     * Establece el retroceso al objetivo del atacante.
     *
     * @param target         objetivo del atacante.
     * @param attacker       atacante de la entidad.
     * @param knockbackValue valor de knockback.
     */
    protected void setKnockback(Entity target, Entity attacker, int knockbackValue) {
        /* La variable knockbackDirection es una variable temporal que almacena la direccion del atacante al momento del
         * ataque para actualizar la posicion de la entidad mientras el frame de esta se mantiene en la misma direccion. */
        target.knockbackDirection = attacker.direction;
        target.speed += knockbackValue;
        target.knockback = true;
    }

    /**
     * Comprueba si deja de seguir al objetivo.
     *
     * @param target   el objetivo.
     * @param distance la distancia en tiles.
     */
    public void checkUnfollow(Entity target, int distance) {
        if (getTileDistance(target) > distance) onPath = false;
    }

    /**
     * Comprueba si empieza a seguir al objetivo.
     *
     * @param target   el objetivo.
     * @param distance la distancia en tiles.
     * @param rate     la tasa que determina si sigue al objetivo.
     */
    public void checkFollow(Entity target, int distance, int rate) {
        if (getTileDistance(target) < distance && Utils.azar(rate) == 1) onPath = true;
    }

    public int getXDistance(Entity target) {
        return Math.abs(x - target.x);
    }

    public int getYDistance(Entity target) {
        return Math.abs(y - target.y);
    }

    /**
     * Obtiene la distancia del objetivo en tiles.
     *
     * @param target el objetivo.
     * @return la distancia en tiles del objetivo.
     */
    public int getTileDistance(Entity target) {
        return (getXDistance(target) + getYDistance(target)) / tile_size;
    }

    public int getGoalRow(Entity target) {
        return (target.y + target.hitbox.y) / tile_size;
    }

    public int getGoalCol(Entity target) {
        return (target.x + target.hitbox.x) / tile_size;
    }

    public int getLeft() {
        return x + hitbox.x;
    }

    public int getRight() {
        return x + hitbox.x + hitbox.width;
    }

    public int getTop() {
        return y + hitbox.y;
    }

    public int getBottom() {
        return y + hitbox.y + hitbox.height;
    }

    public int getCol() {
        return (x + hitbox.x) / tile_size;
    }

    public int getRow() {
        return (y + hitbox.y) / tile_size;
    }

    public void searchPath(int goalRow, int goalCol) {
        int startRow = (y + hitbox.y) / tile_size;
        int startCol = (x + hitbox.x) / tile_size;

        game.aStar.setNodes(startRow, startCol, goalRow, goalCol);

        // Si devuelve verdadero, significa que ha encontrado un camino para guiar a la entidad hacia el objetivo
        if (game.aStar.search()) {

            // Obtiene la siguiente posicion x/y de la ruta
            int nextX = game.aStar.pathList.get(0).col * tile_size;
            int nextY = game.aStar.pathList.get(0).row * tile_size;
            // Obtiene la posicion de la entidad
            int left = x + hitbox.x;
            int right = x + hitbox.x + hitbox.width;
            int top = y + hitbox.y;
            int bottom = y + hitbox.y + hitbox.height;

            // Averigua la direccion relativa del siguiente nodo segun la posicion actual de la entidad
            /* Si el lado izquierdo y derecho de la entidad estan entre la siguiente posicion x de la ruta, entonces
             * se define su movimiento hacia arriba o abajo. */
            if (left >= nextX && right < nextX + tile_size) direction = top > nextY ? UP : DOWN;
            /* Si el lado superior y inferior de la entidad estan entre la siguiente posicion y de la ruta, entonces
             * se define su movimiento hacia la izquierda o derecha. */
            if (top >= nextY && bottom < nextY + tile_size) direction = left > nextX ? LEFT : RIGHT;

                /* Hasta ahora funciona bien, pero en el caso de que una entidad este en el tile que esta debajo del
                 * siguiente tile, PERO no puede cambiar a la direccion DIR_UP por que hay un arbol. */
            else if (top > nextY && left > nextX) {
                // up o left
                direction = UP;
                checkCollisions();
                if (collision) direction = LEFT;
            } else if (top > nextY && left < nextX) {
                // up o right
                direction = UP;
                checkCollisions();
                if (collision) direction = RIGHT;
            } else if (top < nextY && left > nextX) {
                // down o left
                direction = DOWN;
                checkCollisions();
                if (collision) direction = LEFT;
            } else if (top < nextY && left < nextX) {
                // down o right
                direction = DOWN;
                checkCollisions();
                if (collision) direction = RIGHT;
            }

            // Si la entidad llego al objetivo entonces sale del algoritmo de busqueda
            // int nextRow = game.aStar.pathList.get(0).row;
            // int nextCol = game.aStar.pathList.get(0).col;
            // if (nextRow == goalRow && nextCol == goalCol) onPath = false;

        }
    }

}
