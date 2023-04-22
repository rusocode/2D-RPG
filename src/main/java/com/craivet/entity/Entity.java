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
    public BufferedImage image, image2, mobImage; // Imagenes estaticas para los items y mobs
    public int direction = DOWN;
    public int speed, defaultSpeed;
    public int life, maxLife; // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)
    public int mana, maxMana;
    public int ammo;
    public int lvl, exp, nextLvlExp;
    public int coin;
    public int strength, dexterity;
    public int attack, defense;
    public int motion1, motion2;
    public Rectangle hitbox = new Rectangle(0, 0, 48, 48), attackbox = new Rectangle(0, 0, 0, 0);
    public int hitboxDefaultX, hitboxDefaultY;
    public Projectile projectile; // TODO Es necesario declarar este objeto aca?
    public Entity weapon, shield;
    public Entity light; // linterna, antorcha, etc.

    // Item attributes
    public Entity loot;
    public String description;
    public int price;
    public int attackValue, defenseValue;
    public int knockbackValue;
    public int amount = 1;
    public int lightRadius;
    public boolean solid, stackable;
    public boolean opened, empty; // chest

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
     * En caso contrario, comprueba si la entidad esta atacando y si es verdadero ataca. Si ninguna de las dos
     * condiciones anteriores se cumple (knockback o attacking), entonces la entidad realiza una accion y comprueba las
     * colisiones para determinar si se actualiza la posicion o no aplicando el intervalo de movimiento.
     */
    public void update() {
        // TODO Se podria separar en metodos
        if (knockback) {
            checkCollisions();
            if (collision) {
                knockback = false;
                speed = defaultSpeed;
                timer.knockbackCounter = 0;
            } else updatePosition(knockbackDirection);
            timer.timerKnockback(this, INTERVAL_KNOCKBACK);
        } else if (attacking) attack();
        else {
            setAction(); // TIENE QUE REALIZAR UNA ACCION ANTES DE VERIFICAR LA COLISION
            checkCollisions();
            if (!collision) updatePosition(direction);

            /* TODO Estos timers deberian ir fuera del else? Al usar un timer para el movimiento y otro para la
             * animacion de ataque, creo que no interrumpiria el tiempo en el cambio de frames. */
            timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);
            if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
            if (timer.projectileCounter < INTERVAL_PROJECTILE_ATTACK) timer.projectileCounter++;
        }
    }

    public void render(Graphics2D g2) {
        BufferedImage frame = null;
        int screenX = (x - world.player.x) + world.player.screenX;
        int screenY = (y - world.player.y) + world.player.screenY;
        if (x + tile_size > world.player.x - world.player.screenX &&
                x - tile_size < world.player.x + world.player.screenX &&
                y + tile_size > world.player.y - world.player.screenY &&
                y - tile_size < world.player.y + world.player.screenY) {

            int tempScreenX = screenX, tempScreenY = screenY;
            switch (direction) {
                case DOWN -> {
                    if (!attacking) frame = movementNum == 1 || collision ? movementDown1 : movementDown2;
                    if (attacking) frame = attackNum == 1 ? attackDown1 : attackDown2;
                }
                case UP -> {
                    if (!attacking) frame = movementNum == 1 || collision ? movementUp1 : movementUp2;
                    if (attacking) {
                        // Soluciona el bug para las imagenes de ataque up y left, ya que la posicion 0,0 de estas imagenes son tiles transparentes
                        tempScreenY -= tile_size;
                        frame = attackNum == 1 ? attackUp1 : attackUp2;
                    }
                }
                case LEFT -> {
                    if (!attacking) frame = movementNum == 1 || collision ? movementLeft1 : movementLeft2;
                    if (attacking) {
                        tempScreenX -= tile_size;
                        frame = attackNum == 1 ? attackLeft1 : attackLeft2;
                    }
                }
                case RIGHT -> {
                    if (!attacking) frame = movementNum == 1 || collision ? movementRight1 : movementRight2;
                    if (attacking) frame = attackNum == 1 ? attackRight1 : attackRight2;
                }
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

            g2.drawImage(frame, tempScreenX, tempScreenY, null);
            g2.drawImage(image, screenX, screenY, null); // TODO Es eficiente esto?
            // g2.setColor(Color.cyan);
            // g2.drawRect(hitbox.x + screenX, hitbox.y + screenY, hitbox.width, hitbox.height);
            // g2.drawRect(screenX, screenY, tile_size, tile_size);
            Utils.changeAlpha(g2, 1);
        }
    }

    public void setAction() {

    }

    public void setLoot(Entity loot) {}

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
     * @param user       player.
     * @param target     lista de items.
     * @param targetName nombre del item especificado.
     * @return el indice del item especificado a la posicion adyacente del player.
     */
    public int getDetected(Entity user, Entity[][] target, String targetName) {
        int index = -1;

        // Verifica el item adyacente al usuario
        int nextX = user.getLeft();
        int nextY = user.getTop();

        switch (user.direction) {
            case DOWN -> nextY = user.getBottom() + user.speed;
            case UP -> nextY = user.getTop() - user.speed;
            case LEFT -> nextX = user.getLeft() - user.speed;
            case RIGHT -> nextX = user.getRight() + user.speed;
        }

        int row = nextY / tile_size;
        int col = nextX / tile_size;

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
     * @param generator entidad que va a generar las particulas.
     * @param target    objetivo en donde se van a generar las particulas.
     */
    public void generateParticle(Entity generator, Entity target) {
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, -1)); // Top left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, -1)); // Top right
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, 1)); // Down left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, 1)); // Down right
    }

    /**
     * Daña la entidad si el frame de ataque colisiona con la hitbox del objetivo.
     * <p>
     * De 0 a motion1 ms se muestra el primer frame de ataque. De motion1 a motion2 ms se muestra el segundo frame de
     * ataque. Despues de motion2 vuelve al frame de movimiento.
     * <p>
     * En el segundo frame de ataque, la posicion x/y se ajusta para el area de ataque y verifica si colisiona con una
     * entidad.
     */
    public void attack() {
        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= motion1) attackNum = 1; // (de 0-motion1 ms frame de ataque 1)
        if (timer.attackAnimationCounter > motion1 && timer.attackAnimationCounter <= motion2) { // (de motion1-motion2 ms frame de ataque 2)
            attackNum = 2;

            // Guarda la posicion actual de worldX, worldY y el tamaño del hitbox
            int currentX = x;
            int currentY = y;
            int hitboxWidth = hitbox.width;
            int hitboxHeight = hitbox.height;

            // Ajusta el area de ataque del player para cada direccion
            if (type == TYPE_PLAYER) {
                switch (direction) {
                    case DOWN -> {
                        attackbox.x = 9;
                        attackbox.y = 5;
                        attackbox.width = 10;
                        attackbox.height = 27;
                        x += attackbox.x;
                        y += attackbox.y + attackbox.height;
                    }
                    case UP -> {
                        attackbox.x = 15;
                        attackbox.y = 4;
                        attackbox.width = 10;
                        attackbox.height = 28;
                        x += attackbox.x;
                        y -= hitbox.y + attackbox.height; // TODO Por que se resta hitbox.y y no attackArea.y?
                    }
                    case LEFT -> {
                        attackbox.x = 0;
                        attackbox.y = 10;
                        attackbox.width = 25;
                        attackbox.height = 10;
                        x -= hitbox.x + attackbox.x + attackbox.width;
                        y += attackbox.y;
                    }
                    case RIGHT -> {
                        attackbox.x = 16;
                        attackbox.y = 10;
                        attackbox.width = 24;
                        attackbox.height = 10;
                        x += attackbox.x + attackbox.width;
                        y += attackbox.y;
                    }
                }
            } else if (type == TYPE_MOB) {
                switch (direction) {
                    case DOWN -> y += attackbox.height;
                    case UP -> y -= attackbox.height;
                    case LEFT -> x -= attackbox.width;
                    case RIGHT -> x += attackbox.width;
                }
            }

            // Convierte el area del cuerpo en el area de ataque para verificar la colision solo con el area de ataque
            hitbox.width = attackbox.width;
            hitbox.height = attackbox.height;

            if (type == TYPE_MOB) {
                damagePlayer(game.collider.checkPlayer(this), attack);
            } else {
                // Verifica la colision con el mob usando la posicion y tamaño del hitbox actualizados, osea el area de ataque
                int mobIndex = game.collider.checkEntity(this, world.mobs);
                world.player.damageMob(mobIndex, this, weapon.knockbackValue, attack);

                int iTileIndex = game.collider.checkEntity(this, world.interactives);
                world.player.damageInteractiveTile(iTileIndex);

                // TODO Hay un bug que cuando lanza una bola de fuego y ataca al mimso tiempo la dania
                int projectileIndex = game.collider.checkEntity(this, world.projectiles);
                world.player.damageProjectile(projectileIndex);
            }

            // Despues de verificar la colision, resetea los datos originales
            x = currentX;
            y = currentY;
            hitbox.width = hitboxWidth;
            hitbox.height = hitboxHeight;
        }
        if (timer.attackAnimationCounter > motion2) {
            attackNum = 1;
            timer.attackAnimationCounter = 0;
            attacking = false;
        }
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
     * Carga las subimagenes de movimiento.
     *
     * @param ss SpriteSheet con todas las subimages de movimiento.
     * @param w  ancho de la subimagen.
     * @param h  alto de la subimagen.
     * @param s  valor de escala.
     */
    public void loadMovementImages(SpriteSheet ss, int w, int h, int s) {
        BufferedImage[] subimages = SpriteSheet.getMovementSubimages(ss, w, h);
        if (subimages.length > 2) { // Orc (8 frames)
            movementDown1 = Utils.scaleImage(subimages[0], s, s);
            movementDown2 = Utils.scaleImage(subimages[1], s, s);
            movementUp1 = Utils.scaleImage(subimages[2], s, s);
            movementUp2 = Utils.scaleImage(subimages[3], s, s);
            movementLeft1 = Utils.scaleImage(subimages[4], s, s);
            movementLeft2 = Utils.scaleImage(subimages[5], s, s);
            movementRight1 = Utils.scaleImage(subimages[6], s, s);
            movementRight2 = Utils.scaleImage(subimages[7], s, s);
        } else if (subimages.length == 2) { // Slime (2 frames)
            movementDown1 = Utils.scaleImage(subimages[0], s, s);
            movementDown2 = Utils.scaleImage(subimages[1], s, s);
            movementUp1 = Utils.scaleImage(subimages[0], s, s);
            movementUp2 = Utils.scaleImage(subimages[1], s, s);
            movementLeft1 = Utils.scaleImage(subimages[0], s, s);
            movementLeft2 = Utils.scaleImage(subimages[1], s, s);
            movementRight1 = Utils.scaleImage(subimages[0], s, s);
            movementRight2 = Utils.scaleImage(subimages[1], s, s);
        } else { // StickyBall (1 frame)
            movementDown1 = Utils.scaleImage(subimages[0], s, s);
            movementDown2 = Utils.scaleImage(subimages[0], s, s);
            movementUp1 = Utils.scaleImage(subimages[0], s, s);
            movementUp2 = Utils.scaleImage(subimages[0], s, s);
            movementLeft1 = Utils.scaleImage(subimages[0], s, s);
            movementLeft2 = Utils.scaleImage(subimages[0], s, s);
            movementRight1 = Utils.scaleImage(subimages[0], s, s);
            movementRight2 = Utils.scaleImage(subimages[0], s, s);
        }
    }

    /**
     * Carga las subimagenes de ataque.
     *
     * @param ss SpriteSheet con todas las subimages de ataque.
     * @param w  ancho de la subimagen.
     * @param h  alto de la subimagen.
     */
    public void loadAttackImages(SpriteSheet ss, int w, int h) {
        BufferedImage[] subimages = SpriteSheet.getAttackSubimages(ss, w, h);
        attackDown1 = Utils.scaleImage(subimages[0], tile_size, tile_size * 2);
        attackDown2 = Utils.scaleImage(subimages[1], tile_size, tile_size * 2);
        attackUp1 = Utils.scaleImage(subimages[2], tile_size, tile_size * 2);
        attackUp2 = Utils.scaleImage(subimages[3], tile_size, tile_size * 2);
        attackLeft1 = Utils.scaleImage(subimages[4], tile_size * 2, tile_size);
        attackLeft2 = Utils.scaleImage(subimages[5], tile_size * 2, tile_size);
        attackRight1 = Utils.scaleImage(subimages[6], tile_size * 2, tile_size);
        attackRight2 = Utils.scaleImage(subimages[7], tile_size * 2, tile_size);
    }

    /**
     * Comprueba si puede atacar o no verificando si el objetivo esta dentro del rango especificado.
     *
     * @param rate       probabilidad de que el mob ataque.
     * @param vertical   indica la distancia vertical.
     * @param horizontal indica la distancia horizontal.
     */
    public void checkAttackOrNot(int rate, int vertical, int horizontal) {
        boolean targetInRage = false;
        int xDis = getXDistance(world.player);
        int yDis = getYDistance(world.player);
        switch (direction) {
            case DOWN -> {
                if (world.player.y > y && yDis < vertical && xDis < horizontal) targetInRage = true;
            }
            case UP -> {
                if (world.player.y < y && yDis < vertical && xDis < horizontal) targetInRage = true;
            }
            case LEFT -> {
                if (world.player.x < x && xDis < vertical && yDis < horizontal) targetInRage = true;
            }
            case RIGHT -> {
                if (world.player.x > x && xDis < vertical && yDis < horizontal) targetInRage = true;
            }
        }
        // Calcula la probabilidad de atacar si el objetivo esta dentro del rango
        if (targetInRage) {
            if (Utils.azar(rate) == 1) {
                attacking = true;
                movementNum = 1;
                timer.movementCounter = 0; // TODO O se referia al contador de ataque?
                timer.projectileCounter = 0;
            }
        }
    }

    /**
     * Comprueba las colisiones.
     */
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
     * Actualiza la posicion de la entidad.
     *
     * @param direction direccion de la entidad.
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

    /**
     * Busca la mejor ruta para la entidad.
     *
     * @param goalRow fila objetivo.
     * @param goalCol columna objetivo.
     */
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

        }
    }

    /**
     * Obtiene la diferencia entre la posicion x del mob y la posicion x del objetivo.
     *
     * @param target objetivo.
     * @return la diferencia entre la posicion x del mob y la posicion x del objetivo.
     */
    public int getXDistance(Entity target) {
        return Math.abs(x - target.x);
    }

    /**
     * Obtiene la diferencia entre la posicion y del mob y la posicion y del objetivo.
     *
     * @param target objetivo.
     * @return la diferencia entre la posicion y del mob y la posicion y del objetivo.
     */
    public int getYDistance(Entity target) {
        return Math.abs(y - target.y);
    }

    /**
     * Obtiene la distancia del objetivo en tiles.
     *
     * @param target objetivo.
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

    public int getTop() {
        return y + hitbox.y;
    }

    public int getBottom() {
        return y + hitbox.y + hitbox.height;
    }

    public int getLeft() {
        return x + hitbox.x;
    }

    public int getRight() {
        return x + hitbox.x + hitbox.width;
    }

    public int getCol() {
        return (x + hitbox.x) / tile_size;
    }

    public int getRow() {
        return (y + hitbox.y) / tile_size;
    }

}
