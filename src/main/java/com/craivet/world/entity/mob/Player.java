package com.craivet.world.entity.mob;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.gfx.Animation;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.projectile.Fireball;
import com.craivet.input.Keyboard;
import com.craivet.physics.Mechanics;
import com.craivet.world.entity.projectile.Projectile;
import com.craivet.world.tile.Interactive;
import com.craivet.utils.*;
import com.craivet.world.entity.item.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Player extends Mob {

    private final Keyboard keyboard;
    private final Mechanics mechanics;
    private BufferedImage currentFrame;
    public Animation down, up, left, right;

    // Variable auxiliar para obtener los atributos de la entidad actual
    private Entity entity;

    public boolean attackCanceled, lightUpdate;

    public Player(Game game, World world) {
        super(game, world);
        centerOnScreen();
        setDefaultPos();
        setDefaultValues();
        keyboard = game.keyboard;
        mechanics = new Mechanics(this);
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
            if (!flags.colliding && !keyboard.checkAccionKeys()) updatePosition(direction);
            mechanics.checkDirectionSpeed(entity);
            checkAttack();
            keyboard.resetAccionKeys();
            if (keyboard.checkMovementKeys()) {
                down.tick();
                up.tick();
                left.tick();
                right.tick();
            }
        } else // TODO Este else no funciona por que no se temporiza la detencion de los nuevos frames
            timer.timeStopMovement(this, 20); // Temporiza la detencion del movimiento cuando se dejan de presionar las teclas de movimiento

        checkShoot();
        checkTimers();
        checkHpMana();
    }

    @Override
    public void render(Graphics2D g2) {
        tempScreenX = screenX;
        tempScreenY = screenY;
        if (flags.invincible) Utils.changeAlpha(g2, 0.3f);
        g2.drawImage(getCurrentAnimationFrame(), tempScreenX, tempScreenY, null);
        // drawRects(g2);
        Utils.changeAlpha(g2, 1);
    }

    private void centerOnScreen() {
        screenX = SCREEN_WIDTH / 2 - (tile_size / 2);
        screenY = SCREEN_HEIGHT / 2 - (tile_size / 2);
    }

    public void setDefaultPos() {
        world.area = OUTSIDE;
        world.map = NIX;
        direction = Direction.DOWN;
        // TODO Se podria comprobar si la posicion es valida o no
        x = 23 * tile_size;
        /* Al usar una imagen mas grande para el player en donde los tiles son de 48x48 hay que ajustar la posicion
         * restando la mitad del alto escalado de la imagen para que respete la coordenada especificada. */
        y = 23 * tile_size - 122 / 2;
    }

    public void setDefaultValues() {
        type = Type.PLAYER;
        direction = Direction.DOWN;
        speed = defaultSpeed = 3; // o 3?
        hp = maxHp = 6;
        mana = maxMana = 4;
        ammo = 5;
        lvl = 1;
        exp = 0;
        nextLvlExp = 5;
        gold = 500;
        strength = 1;
        dexterity = 1;
        flags.invincible = false;

        projectile = new Fireball(game, world);
        weapon = new SwordNormal(game, world);
        shield = new ShieldWood(game, world);
        light = null;
        attack = getAttack();
        defense = getDefense();

        hitbox.x = 13;
        hitbox.y = 70;
        hitbox.width = 23;
        hitbox.height = 42;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        motion1 = 5;
        motion2 = 25;

        // frame.loadWeaponFrames(player_sword, 16, 16);
        ss.loadMovementFramesOfPlayer(player_movement, 1);

        int animationSpeed = 90;
        down = new Animation(animationSpeed, ss.down);
        up = new Animation(animationSpeed, ss.up);
        left = new Animation(animationSpeed, ss.left);
        right = new Animation(animationSpeed, ss.right);
        currentFrame = down.getFirstFrame();
        addItemsToInventory();
    }

    /**
     * TODO Evitar que el player aparezca sobre una entidad solida o fuera de los limites del mapa
     */
    public void setPos(int map, int x, int y) {
        if (map == NIX) world.area = OUTSIDE;
        if (map == NIX_INDOOR_01) world.area = INDOOR;
        if (map == DUNGEON_01 || map == DUNGEON_02) world.area = DUNGEON;
        world.map = map;
        this.x = x * tile_size;
        this.y = y * tile_size;
    }

    public void restoreStatus() {
        hp = maxHp;
        mana = maxMana;
        flags.invincible = false;
        flags.hitting = false;
        flags.knockback = false;
        lightUpdate = true;
    }

    /**
     * Comprueba si puede atacar.
     */
    private void checkAttack() {
        if (keyboard.enter && !attackCanceled && timer.attackCounter == INTERVAL_WEAPON && !flags.shooting) {
            if (weapon.type == Type.SWORD) game.playSound(sound_swing_weapon);
            if (weapon.type != Type.SWORD) game.playSound(sound_swing_axe);
            flags.hitting = true;
            timer.attackAnimationCounter = 0;
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
            projectile.set(x, y, direction, true, this);
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

    private void checkTimers() {
        if (flags.invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
        if (timer.projectileCounter < INTERVAL_PROJECTILE) timer.projectileCounter++;
        if (timer.attackCounter < INTERVAL_WEAPON) timer.attackCounter++;
    }

    private void checkHpMana() {
        if (hp <= 0) die();
        if (hp > maxHp) hp = maxHp;
        if (mana > maxMana) mana = maxMana;
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
            if (keyboard.l && item.type != Type.OBSTACLE) {
                if (item.type == Type.PICKUP) item.use(this);
                else if (canPickup(item)) game.playSound(sound_item_pickup);
                else {
                    game.ui.addMessage("You cannot carry any more!");
                    return;
                }
                world.items[world.map][i] = null;
            }
            if (keyboard.enter && item.type == Type.OBSTACLE) {
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
            if (keyboard.enter && mob.type == Type.NPC) {
                attackCanceled = true;
                mob.dialogue();
            } else mob.move(direction);
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
            if (!flags.invincible && !mob.flags.dead && mob.type == Type.HOSTILE) {
                game.playSound(sound_player_damage);
                int damage = Math.max(mob.attack - defense, 1);
                hp -= damage;
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
            if (!mob.flags.invincible && mob.type != Type.NPC) {

                if (knockbackValue > 0) setKnockback(mob, attacker, knockbackValue);

                int damage = Math.max(attack - mob.defense, 1);
                mob.hp -= damage;
                game.ui.addMessage(damage + " damage!");
                if (mob.hp > 0) {
                    if (mob instanceof Slime || mob instanceof RedSlime) game.playSound(sound_hit_slime);
                    if (mob instanceof Orc) {
                        game.playSound(sound_hit_mob);
                        game.playSound(sound_hit_orc);
                    }
                }

                mob.flags.invincible = true;
                mob.hpBar = true;
                mob.damageReaction();

                if (mob.hp <= 0) {
                    game.playSound(sound_mob_death);
                    mob.flags.dead = true;
                    game.ui.addMessage("Killed the " + mob.name + "!");
                    game.ui.addMessage("Exp + " + mob.exp);
                    exp += mob.exp;
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
                interactive.hp--;
                interactive.flags.invincible = true;

                generateParticle(interactive, interactive);

                if (interactive.hp == 0) {
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
        if (exp >= nextLvlExp) {
            lvl++;
            exp = 0;
            nextLvlExp *= 2;
            maxHp += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            game.playSound(sound_level_up);
            dialogues[0][0] = "You are level " + lvl + "!";
            startDialogue(DIALOGUE_STATE, this, 0);
        }
    }

    /**
     * Selecciona el item del array de inventario utilizando el indice del slot del inventario UI.
     */
    public void selectItem() {
        int itemIndex = game.ui.getItemIndexOnSlot(game.ui.playerSlotCol, game.ui.playerSlotRow);
        if (itemIndex < inventory.size()) {
            Item selectedItem = inventory.get(itemIndex);
            if (selectedItem instanceof Axe || selectedItem instanceof Pickaxe || selectedItem instanceof SwordNormal) {
                weapon = selectedItem;
                attackbox = weapon.attackbox; // TODO Hace falta esto aca?
                attack = getAttack();
                switch (weapon.type) {
                    case SWORD -> {
                        // frame.loadWeaponFrames(player_sword, 16, 16);
                        game.playSound(sound_draw_sword);
                    }
                    // case AXE -> frame.loadWeaponFrames(player_axe, 16, 16);
                    // case PICKAXE -> frame.loadWeaponFrames(player_pickaxe, 16, 16);
                }
            }
            if (selectedItem.type == Type.SHIELD) {
                shield = selectedItem;
                defense = getDefense();
            }
            if (selectedItem.type == Type.LIGHT) {
                light = light == selectedItem ? null : selectedItem;
                lightUpdate = true;
            }
            if (selectedItem.type == Type.CONSUMABLE) {
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
    public boolean canPickup(Entity item) {
        Item newItem = game.itemGenerator.generate(item.name);
        if (item.stackable) {
            int itemIndex = searchItemInInventory(item.name);
            // Si existe en el inventario, entonces solo aumenta la cantidad
            if (itemIndex != -1) {
                inventory.get(itemIndex).amount += item.amount;
                return true;
                // Si no existe en el inventario, lo agrega como nuevo item con su respectiva cantidad
            } else if (inventory.size() != MAX_INVENTORY_SIZE) {
                inventory.add(newItem);
                // Al agregar un nuevo item, no puede utilizar el indice del item anterior, tiene que buscar el indice a partir del nuevo item
                inventory.get(searchItemInInventory(item.name)).amount += item.amount;
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
            if (inventory.get(i).name.equals(name)) return i;
        return -1;
    }

    /**
     * Obtiene la direccion dependiendo de la tecla seleccionada.
     */
    private void getDirection() {
        if (keyboard.s) direction = Direction.DOWN;
        else if (keyboard.w) direction = Direction.UP;
        else if (keyboard.a) direction = Direction.LEFT;
        else if (keyboard.d) direction = Direction.RIGHT;
    }

    @Override
    public void checkCollision() {
        flags.colliding = false;
        game.collision.checkTile(this);
        pickup(game.collision.checkItem(this));
        interactNpc(game.collision.checkEntity(this, world.mobs));
        hurt(game.collision.checkEntity(this, world.mobs));
        setCurrentInteractive(game.collision.checkEntity(this, world.interactives));
        // game.collision.checkEntity(this, world.interactives);
        game.event.checkEvent();
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

    private BufferedImage getCurrentAnimationFrame() {
        if (keyboard.checkMovementKeys()) {
            switch (direction) {
                case DOWN -> {
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

    /**
     * Obtiene el frame actual.
     *
     * @return el frame actual.
     */
    private BufferedImage getCurrentFrame() {
        int frameIndex = 0;
        if (!flags.hitting) {
            switch (direction) {
                case DOWN -> {
                    if (flags.collidingOnMob) frameIndex = ss.movementNum == 1 ? 0 : 1;
                    else frameIndex = ss.movementNum == 1 || flags.colliding ? 0 : 1;
                }
                case UP -> {
                    if (flags.collidingOnMob) frameIndex = ss.movementNum == 1 ? 2 : 3;
                    else frameIndex = ss.movementNum == 1 || flags.colliding ? 2 : 3;
                }
                case LEFT -> {
                    if (flags.collidingOnMob) frameIndex = ss.movementNum == 1 ? 4 : 5;
                    else frameIndex = ss.movementNum == 1 || flags.colliding ? 4 : 5;
                }
                case RIGHT -> {
                    if (flags.collidingOnMob) frameIndex = ss.movementNum == 1 ? 6 : 7;
                    else frameIndex = ss.movementNum == 1 || flags.colliding ? 6 : 7;
                }
            }
        } else {
            switch (direction) {
                case DOWN -> frameIndex = ss.attackNum == 1 ? 0 : 1;
                case UP -> {
                    // Soluciona el bug para las imagenes de ataque up y left, ya que la posicion 0,0 de estas imagenes son tiles transparentes
                    tempScreenY -= tile_size;
                    frameIndex = ss.attackNum == 1 ? 2 : 3;
                }
                case LEFT -> {
                    tempScreenX -= tile_size;
                    frameIndex = ss.attackNum == 1 ? 4 : 5;
                }
                case RIGHT -> frameIndex = ss.attackNum == 1 ? 6 : 7;
            }
        }

        return !flags.hitting ? ss.movement[frameIndex] : ss.weapon[frameIndex];
    }

    public int getAttack() {
        return strength * weapon.attackValue;
    }

    public int getDefense() {
        return dexterity * shield.defenseValue;
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
        Arrays.fill(ss.movement, image);
    }

    private void addItemsToInventory() {
        inventory.clear();
        inventory.add(weapon);
        inventory.add(shield);
        inventory.add(new Key(game, world, 2));
        inventory.add(new PotionRed(game, world, 1));
        inventory.add(new Lantern(game, world));
        inventory.add(new Pickaxe(game, world));
        inventory.add(new Axe(game, world));
        inventory.add(new Tent(game, world));
    }

    private void drawRects(Graphics2D g2) {
        // Imagen
        g2.setColor(Color.magenta);
        g2.drawRect(screenX, screenY, 50, 122);
        // Hitbox
        g2.setColor(Color.red);
        g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
        // Area de ataque
        if (flags.hitting) {
            g2.setColor(Color.green);
            switch (direction) {
                case DOWN ->
                        g2.drawRect(screenX + hitbox.x + attackbox.x, screenY + hitbox.y + attackbox.y + attackbox.height, attackbox.width, attackbox.height);
                case UP ->
                        g2.drawRect(screenX + hitbox.x + attackbox.x, screenY - attackbox.height, attackbox.width, attackbox.height);
                case LEFT ->
                        g2.drawRect(screenX + attackbox.x - attackbox.width, screenY + hitbox.y + attackbox.y, attackbox.width, attackbox.height);
                case RIGHT ->
                        g2.drawRect(screenX + hitbox.x + attackbox.x + attackbox.width, screenY + hitbox.y + attackbox.y, attackbox.width, attackbox.height);
            }
        }
    }

}
