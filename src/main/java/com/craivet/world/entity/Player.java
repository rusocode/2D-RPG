package com.craivet.world.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.gfx.Animation;
import com.craivet.world.World;
import com.craivet.world.entity.mob.Mob;
import com.craivet.world.entity.mob.Slime;
import com.craivet.world.entity.projectile.Fireball;
import com.craivet.input.Keyboard;
import com.craivet.world.entity.projectile.Projectile;
import com.craivet.world.tile.Interactive;
import com.craivet.utils.*;
import com.craivet.world.entity.item.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

// TODO No tendria que crear el objeto UI desde aca?
// TODO No tendria que convertirse en la fomasa clase Client?
public class Player extends Entity {

    public final Keyboard keyboard;
    private Animation down, up, left, right;
    public BufferedImage currentFrame, currentSwordFrame;

    private Entity entity; // Variable auxiliar para obtener los atributos de la entidad actual
    public boolean attackCanceled, lightUpdate;

    public Player(Game game, World world) {
        super(game, world);
        centerOnScreen();
        setDefaultValues();
        setDefaultPos();
        keyboard = game.keyboard;
    }

    /**
     * Es muy importante el orden de los metodos.
     */
    @Override
    public void update() {
        if (flags.hitting) hit();
        if (keyboard.checkKeys()) {
            getDirection();
            checkCollision();
            if (!flags.colliding && !keyboard.checkAccionKeys()) updatePosition(stats.direction);
            mechanics.checkDirectionSpeed(this, entity);
            checkAttack();
            keyboard.resetAccionKeys();
            if (keyboard.checkMovementKeys()) {
                down.tick();
                up.tick();
                left.tick();
                right.tick();
            }
        } else // TODO Este else no funciona porque no se temporiza la detencion de los nuevos frames
            timer.timeStopMovement(this, 20);

        checkShoot();
        timer.checkTimers(this);
        checkStats();
    }

    @Override
    public void render(Graphics2D g2) {
        if (flags.invincible) Utils.changeAlpha(g2, 0.3f);
        if (!flags.hitting) g2.drawImage(getCurrentAnimationFrame(), screen.x, screen.y, null);
        else getCurrentItemFrame(g2);
        drawRects(g2);
        Utils.changeAlpha(g2, 1);
    }

    private void centerOnScreen() {
        screen.x = WINDOW_WIDTH / 2 - (tile / 2);
        screen.y = WINDOW_HEIGHT / 2 - (tile * 2 / 2);
    }

    public void setDefaultValues() {
        stats.type = Type.PLAYER;
        stats.direction = Direction.DOWN;
        stats.speed = stats.defaultSpeed = 2;
        stats.hp = stats.maxHp = 6;
        stats.mana = stats.maxMana = 4;
        stats.ammo = 5;
        stats.lvl = 1;
        stats.exp = 0;
        stats.nextLvlExp = 5;
        stats.gold = 500;
        stats.strength = 1;
        stats.dexterity = 1;
        flags.invincible = false; // TODO Hace falta?

        projectile = new Fireball(game, world);
        weapon = new SwordIron(game, world);
        shield = new ShieldWood(game, world);
        light = null;
        stats.attack = getAttack();
        stats.defense = getDefense();

        hitbox.x = 7;
        hitbox.y = 32;
        hitbox.width = 10;
        hitbox.height = 24;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        stats.motion1 = 5;
        stats.motion2 = 18;

        sheet.loadPlayerMovementFrames(player_movement, 1);
        sheet.loadWeaponFrames(sword_frame, 16, 16);
        sheet.frame = sheet.down[0];

        int animationSpeed = 90;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);
        currentFrame = down.getFirstFrame();
        addItemsToInventory();
    }

    /**
     * Establece la posicion por defecto.
     */
    public void setDefaultPos() {
        world.zone = DUNGEON;
        world.map = DUNGEON_01;
        stats.direction = Direction.DOWN;
        // Posiciona la hitbox, NO la imagen
        int startCol = 21, startRow = 22; // 26, 39 // 10, 27
        // Suma la mitad del ancho de la hitbox y resta un pixel para centrar la posicion horizontal dentro del tile
        pos.x = (startCol * tile) + hitbox.width / 2 - 1;
        /* Resta el alto de la hitbox para que la posicion se ajuste en la fila especificada, ya que la imagen del
         * player ocupa dos tiles verticales. Por ultimo se resta un pixel en caso de que la posicion este por encima
         * de un tile solido para evitar que se "trabe". */
        pos.y = (startRow * tile) - hitbox.height - 1;
    }

    /**
     * TODO Evitar que el player aparezca sobre una entidad solida o fuera de los limites del mapa
     * TODO Falta agregar el parametro zone
     */
    public void setPos(int map, int x, int y) {
        if (map == NASHE) world.zone = OUTSIDE;
        if (map == NASHE_INDOOR_01) world.zone = INDOOR;
        if (map == DUNGEON_01 || map == DUNGEON_02) world.zone = DUNGEON;
        world.map = map;
        pos.x = x * tile;
        pos.y = y * tile;
    }

    public void resetStats() {
        stats.hp = stats.maxHp;
        stats.mana = stats.maxMana;
        flags.invincible = false;
        flags.hitting = false;
        flags.knockback = false;
        lightUpdate = true;
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
        if (timer.attackAnimationCounter <= stats.motion1) sheet.attackNum = 1; // (de 0-motion1ms frame de ataque 1)
        if (timer.attackAnimationCounter > stats.motion1 && timer.attackAnimationCounter <= stats.motion2) { // (de motion1-motion2ms frame de ataque 2)
            sheet.attackNum = 2;

            // Guarda la posicion actual de x/y y el tamaño de la hitbox
            int currentX = pos.x, currentY = pos.y;
            int hitboxWidth = hitbox.width, hitboxHeight = hitbox.height;

            /* Ajusta la attackbox (en la hoja de la espada para ser mas especificos) del player dependiendo de la
             * direccion de ataque. Es importante aclarar que las coordenadas x/y de la attackbox parten de la esquina
             * superior izquierda de la hitbox del player (nose si es necesario partir desde esa esquina). */
            switch (stats.direction) {
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
            pos.x += attackbox.x;
            pos.y += attackbox.y;

            // Convierte la hitbox (el ancho y alto) en la attackbox para verificar la colision solo con la attackbox
            hitbox.width = attackbox.width;
            hitbox.height = attackbox.height;

            // Verifica la colision con el mob usando la posicion y tamaño de la hitbox actualizada, osea con la attackbox
            int mobIndex = game.collision.checkEntity(this, world.mobs);
            world.player.hitMob(mobIndex, this, weapon.knockbackValue, stats.attack);

            int interactiveIndex = game.collision.checkEntity(this, world.interactives);
            world.player.hitInteractive(interactiveIndex);

            int projectileIndex = game.collision.checkEntity(this, world.projectiles);
            world.player.hitProjectile(projectileIndex);

            // Despues de verificar la colision, resetea los datos originales
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
     * Comprueba si puede atacar.
     */
    private void checkAttack() {
        if (keyboard.enter && !attackCanceled && timer.attackCounter == INTERVAL_WEAPON && !flags.shooting) {
            if (weapon.stats.type == Type.SWORD) game.playSound(sound_swing_weapon);
            if (weapon.stats.type != Type.SWORD) game.playSound(sound_swing_axe);
            flags.hitting = true;
            timer.attackCounter = 0;
        }
        flags.shooting = false;
        attackCanceled = false; // Para que pueda volver a atacar despues de interactuar con un npc o beber agua
    }

    /**
     * Comprueba si puede lanzar un proyectil.
     */
    private void checkShoot() {
        if (keyboard.f && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE && projectile.haveResource(this) && !flags.hitting) {
            flags.shooting = true;
            game.playSound(sound_fireball);
            projectile.set(pos.x, pos.y, stats.direction, true, this);
            // Comprueba vacante para agregar el proyectil
            for (int i = 0; i < world.projectiles[1].length; i++) {
                if (world.projectiles[world.map][i] == null) {
                    world.projectiles[world.map][i] = projectile;
                    break;
                }
            }
            projectile.subtractResource(this);
            timer.projectileCounter = 0;
        }
    }

    private void checkStats() {
        if (!keyboard.godMode) if (stats.hp <= 0) die();
        if (stats.hp > stats.maxHp) stats.hp = stats.maxHp;
        if (stats.mana > stats.maxMana) stats.mana = stats.maxMana;
    }

    /**
     * Recoge un item.
     * <p>
     * TODO No tendria que ir en la clase Item?
     *
     * @param i indice del item.
     */
    private void pickup(int i) {
        if (i != -1) {
            Item item = world.items[world.map][i];
            if (keyboard.p && item.stats.type != Type.OBSTACLE) {
                if (item.stats.type == Type.PICKUP) item.use(this);
                else if (canPickup(item)) game.playSound(sound_item_pickup);
                else {
                    game.ui.addMessageToConsole("You cannot carry any more!");
                    return;
                }
                world.items[world.map][i] = null;
            }
            if (keyboard.enter && item.stats.type == Type.OBSTACLE) {
                attackCanceled = true;
                item.interact();
            }
        }
    }

    /**
     * Interactua con el npc.
     *
     * @param i indice del npc.
     */
    private void interactNpc(int i) {
        if (i != -1) {
            entity = world.mobs[world.map][i];
            Mob mob = world.mobs[world.map][i];
            if (keyboard.enter && mob.stats.type == Type.NPC) {
                attackCanceled = true;
                mob.dialogue();
            } else mob.move(stats.direction); // En caso de que sea la roca
        }
    }

    /**
     * Daña al player si colisiona con un mob hostil.
     *
     * @param i indice del mob.
     */
    private void hurt(int i) {
        if (i != -1) {
            entity = world.mobs[world.map][i];
            Mob mob = world.mobs[world.map][i];
            if (!flags.invincible && !mob.flags.dead && mob.stats.type == Type.HOSTILE) {
                game.playSound(sound_player_damage);
                int damage = Math.max(mob.stats.attack - stats.defense, 1);
                stats.hp -= damage;
                flags.invincible = true;
            }
        }
    }

    /**
     * Golpea al mob.
     *
     * @param i              indice del mob.
     * @param attacker       atacante del mob.
     * @param knockbackValue valor de knockback.
     * @param attack         tipo de ataque (sword o fireball).
     */
    public void hitMob(int i, Entity attacker, int knockbackValue, int attack) {
        if (i != -1) { // TODO Lo cambio por >= 0 para evitar la doble negacion y comparacion -1?
            entity = world.mobs[world.map][i];
            Mob mob = world.mobs[world.map][i];
            if (!mob.flags.invincible && mob.stats.type != Type.NPC) {

                if (knockbackValue > 0) mechanics.setKnockback(mob, attacker, knockbackValue);

                int damage = Math.max(attack - mob.stats.defense, 1);
                mob.stats.hp -= damage;
                game.ui.addMessageToConsole(damage + " damage!");
                if (mob.stats.hp > 0) game.playSound(mob.soundHit);

                mob.flags.invincible = true;
                mob.flags.hpBar = true;
                mob.damageReaction();

                if (mob.stats.hp <= 0) {
                    game.playSound(sound_mob_death);
                    if (!(mob instanceof Slime)) game.playSound(mob.soundDeath);
                    mob.flags.dead = true;
                    game.ui.addMessageToConsole("Killed the " + mob.stats.name + "!");
                    game.ui.addMessageToConsole("Exp + " + mob.stats.exp);
                    stats.exp += mob.stats.exp;
                    checkLevelUp();
                }
            }
        }
    }

    /**
     * Daña al tile interactivo.
     *
     * @param i indice del tile interactivo.
     */
    public void hitInteractive(int i) {
        if (i != -1) {
            entity = world.interactives[world.map][i];
            Interactive interactive = world.interactives[world.map][i];
            if (interactive.destructible && interactive.isCorrectWeapon(weapon) && !interactive.flags.invincible) {
                interactive.playSound();
                interactive.stats.hp--;
                interactive.flags.invincible = true;

                generateParticle(interactive, interactive);

                if (interactive.stats.hp == 0) {
                    interactive.checkDrop();
                    world.interactives[world.map][i] = interactive.replaceBy();
                }
            }
        }
    }

    public void hitProjectile(int i) {
        if (i != -1) {
            entity = world.projectiles[world.map][i];
            Projectile projectile = world.projectiles[world.map][i];
            // Evita dañar el propio proyectil
            if (projectile != this.projectile) {
                game.playSound(sound_player_damage);
                projectile.flags.alive = false;
                generateParticle(projectile, projectile);
            }
        }
    }

    /**
     * Comprueba si subio de nivel.
     */
    private void checkLevelUp() {
        if (stats.exp >= stats.nextLvlExp) {
            stats.lvl++;
            stats.exp = 0;
            stats.nextLvlExp *= 2;
            stats.maxHp += 2;
            stats.strength++;
            stats.dexterity++;
            stats.attack = getAttack();
            stats.defense = getDefense();
            game.playSound(sound_level_up);
            dialogues[0][0] = "You are level " + stats.lvl + "!";
            startDialogue(DIALOGUE_STATE, this, 0);
        }
    }

    /**
     * Selecciona el item del array de inventario utilizando el indice del slot del inventario UI.
     */
    public void selectItem() {
        int itemIndex = game.ui.getItemIndexOnInventory(game.ui.playerSlotCol, game.ui.playerSlotRow);
        if (itemIndex < inventory.size()) {
            Item selectedItem = inventory.get(itemIndex);
            if (selectedItem instanceof Axe || selectedItem instanceof Pickaxe || selectedItem instanceof SwordIron) {
                weapon = selectedItem;
                attackbox = weapon.attackbox; // TODO Hace falta esto aca?
                stats.attack = getAttack();
                switch (weapon.stats.type) {
                    case SWORD -> {
                        sheet.loadWeaponFrames(sword_frame, 16, 16);
                        game.playSound(sound_draw_sword);
                    }
                    case AXE -> sheet.loadWeaponFrames(axe_frame, 16, 16);
                    case PICKAXE -> sheet.loadWeaponFrames(pickaxe_frame, 16, 16);
                }
            }
            if (selectedItem.stats.type == Type.SHIELD) {
                shield = selectedItem;
                stats.defense = getDefense();
            }
            if (selectedItem.stats.type == Type.LIGHT) {
                light = light == selectedItem ? null : selectedItem;
                lightUpdate = true;
            }
            if (selectedItem.stats.type == Type.CONSUMABLE) {
                if (selectedItem.use(this)) {
                    if (selectedItem.amount > 1) selectedItem.amount--;
                    else inventory.remove(itemIndex);
                }
            }
        }
    }

    /**
     * Verifica si puede recoger el item y en caso afirmativo lo agrega al inventario.
     *
     * @param item item.
     * @return true si se puede recoger el item o false.
     */
    public boolean canPickup(Item item) {
        Item newItem = game.itemGenerator.generate(item.stats.name);
        if (item.stackable) {
            int itemIndex = searchItemInInventory(item.stats.name);
            // Si existe en el inventario, entonces solo aumenta la cantidad
            if (itemIndex != -1) {
                inventory.get(itemIndex).amount += item.amount;
                return true;
                // Si no existe en el inventario, lo agrega como nuevo item con su respectiva cantidad
            } else if (inventory.size() != MAX_INVENTORY_SIZE) {
                inventory.add(newItem);
                // Al agregar un nuevo item, no puede utilizar el indice del item anterior, tiene que buscar el indice a partir del nuevo item
                inventory.get(searchItemInInventory(item.stats.name)).amount += item.amount;
                return true;
            }
        } else if (inventory.size() != MAX_INVENTORY_SIZE) {
            inventory.add(newItem);
            return true;
        }
        return false;
    }

    /**
     * Busca el item en el inventario.
     *
     * @param name nombre del item.
     * @return el indice del item o -1 si no esta.
     */
    private int searchItemInInventory(String name) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i).stats.name.equals(name)) return i;
        return -1;
    }

    /**
     * Obtiene la direccion dependiendo de la tecla seleccionada.
     */
    private void getDirection() {
        if (keyboard.s) stats.direction = Direction.DOWN;
        else if (keyboard.w) stats.direction = Direction.UP;
        else if (keyboard.a) stats.direction = Direction.LEFT;
        else if (keyboard.d) stats.direction = Direction.RIGHT;
    }

    @Override
    public void checkCollision() {
        flags.colliding = false;
        if (!keyboard.godMode) game.collision.checkTile(this);
        pickup(game.collision.checkItem(this));
        interactNpc(game.collision.checkEntity(this, world.mobs));
        hurt(game.collision.checkEntity(this, world.mobs));
        setCurrentInteractive(game.collision.checkEntity(this, world.interactives));
        // game.collision.checkEntity(this, world.interactives);
        game.event.checkEvent(this);
    }

    private void setCurrentInteractive(int i) {
        if (i != -1) entity = world.interactives[world.map][i];
    }

    private void die() {
        game.playSound(sound_player_death);
        game.state = GAME_OVER_STATE;
        game.ui.command = -1;
        game.music.stop();
    }

    private void getCurrentItemFrame(Graphics2D g2) {
        switch (stats.direction) {
            case DOWN -> {
                currentFrame = down.getFirstFrame();
                g2.drawImage(sheet.down[1], screen.x, screen.y, null);
                g2.drawImage(sheet.weapon[0], screen.x, screen.y + 34, null);
            }
            case UP -> {
                currentFrame = up.getFirstFrame();
                g2.drawImage(sheet.up[2], screen.x, screen.y, null);
                g2.drawImage(sheet.weapon[1], screen.x + 13, screen.y + 17, null);
            }
            case LEFT -> {
                currentFrame = left.getFirstFrame();
                g2.drawImage(sheet.left[2], screen.x, screen.y, null);
                g2.drawImage(sheet.weapon[2], screen.x - 7, screen.y + 26, null);
            }
            case RIGHT -> {
                currentFrame = right.getFirstFrame();
                g2.drawImage(sheet.right[4], screen.x, screen.y, null);
                g2.drawImage(sheet.weapon[3], screen.x + 15, screen.y + 28, null);
            }
        }
    }

    /**
     * Obtiene el frame de animacion actual.
     *
     * @return el frame de animacion actual.
     */
    private BufferedImage getCurrentAnimationFrame() {
        /* Cuando se deja de mover, devuelve el primer frame guardado de la ultima direccion para representar la
         * detencion del player. */
        if (keyboard.checkMovementKeys()) {
            switch (stats.direction) {
                case DOWN -> {
                    // Guarda el primer frame hacia abajo
                    currentFrame = down.getFirstFrame();
                    if (flags.collidingOnMob) return down.getCurrentFrame();
                    else return flags.colliding ? down.getFirstFrame() : down.getCurrentFrame();
                }
                case UP -> {
                    currentFrame = up.getFirstFrame();
                    if (flags.collidingOnMob) return up.getCurrentFrame();
                    else return flags.colliding ? up.getFirstFrame() : up.getCurrentFrame();
                }
                case LEFT -> {
                    currentFrame = left.getFirstFrame();
                    if (flags.collidingOnMob) return left.getCurrentFrame();
                    else return flags.colliding ? left.getFirstFrame() : left.getCurrentFrame();
                }
                case RIGHT -> {
                    currentFrame = right.getFirstFrame();
                    if (flags.collidingOnMob) return right.getCurrentFrame();
                    else return flags.colliding ? right.getFirstFrame() : right.getCurrentFrame();
                }
            }
        }
        return currentFrame;
    }

    public int getAttack() {
        return stats.strength * weapon.attackValue;
    }

    public int getDefense() {
        return stats.dexterity * shield.defenseValue;
    }

    public int getCurrentWeaponSlot() {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == weapon) return i;
        return 0; // TODO No es -1?
    }

    public int getCurrentShieldSlot() {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == shield) return i;
        return 0;
    }

    public int getCurrentLightSlot() {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == light) return i;
        return 0;
    }

    public void initSleepImage(BufferedImage image) {
        currentFrame = image;
    }

    private void addItemsToInventory() {
        inventory.clear();
        inventory.add(weapon);
        inventory.add(shield);
        inventory.add(new Lantern(game, world));
        inventory.add(new PotionRed(game, world, 2));
        inventory.add(new Key(game, world, 2));
        inventory.add(new Pickaxe(game, world));
        inventory.add(new Axe(game, world));
        inventory.add(new Tent(game, world));
    }

    // TODO Activar con tecla
    private void drawRects(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0));
        // Frame
        g2.setColor(Color.magenta);
        g2.drawRect(screen.x, screen.y, currentFrame.getWidth(), currentFrame.getHeight()); // TODO Creo que se podria reemplazar por image.getWidth()
        // Hitbox
        g2.setColor(Color.green);
        g2.drawRect(screen.x + hitbox.x, screen.y + hitbox.y, hitbox.width, hitbox.height);
        // Attackbox
        if (flags.hitting) {
            g2.setColor(Color.red);
            /* Se suma la posicion de la attackbox a la posicion del player porque despues de verificar la deteccion del
             * golpe en el metodo hit, se resetea la posicio del player, por lo tanto se suma desde aca para que el
             * rectangulo dibujado coincida con la posicion especificada en el metodo hit. */
            g2.drawRect(screen.x + attackbox.x + hitbox.x, screen.y + attackbox.y + hitbox.y, attackbox.width, attackbox.height);
        }
    }

}
