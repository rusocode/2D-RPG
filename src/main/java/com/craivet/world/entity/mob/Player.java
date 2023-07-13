package com.craivet.world.entity.mob;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.projectile.Fireball;
import com.craivet.input.Keyboard;
import com.craivet.physics.Mechanics;
import com.craivet.world.entity.projectile.Projectile;
import com.craivet.world.tile.Interactive;
import com.craivet.util.*;
import com.craivet.world.entity.item.*;

import static com.craivet.util.Global.*;
import static com.craivet.gfx.Assets.*;

public class Player extends Mob {

    private final Keyboard keyboard;
    private final Mechanics mechanics;

    // Variable auxiliar para obtener los atributos de la entidad actual
    private Entity entity;

    public boolean attackCanceled, lightUpdate;

    public Player(Game game, World world) {
        super(game, world, 23, 20);
        centerOnScreen();
        setDefaultPos();
        setDefaultValues();
        keyboard = game.keyboard;
        mechanics = new Mechanics(this);
    }

    /**
     * Es muy importante el orden de los metodos.
     */
    public void update() {
        if (flags.hitting) hit();
        if (keyboard.checkKeys()) {
            getDirection();
            checkCollision();
            if (!flags.colliding && !keyboard.checkAccionKeys()) updatePosition(direction);
            mechanics.checkDirectionSpeed(entity);
            checkAttack();
            keyboard.resetAccionKeys();
            if (keyboard.checkMovementKeys()) timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);
        } else
            timer.timeStopMovement(this, 20); // Temporiza la detencion del movimiento cuando se dejan de presionar las teclas de movimiento

        checkShoot();
        checkTimers();
        checkHpMana();
    }

    public void render(Graphics2D g2) {
        BufferedImage frame = null;
        tempScreenX = screenX;
        tempScreenY = screenY;
        switch (direction) {
            case DOWN -> frame = getFrame(DOWN, movementDown1, movementDown2, weaponDown1, weaponDown2);
            case UP -> frame = getFrame(UP, movementUp1, movementUp2, weaponUp1, weaponUp2);
            case LEFT -> frame = getFrame(LEFT, movementLeft1, movementLeft2, weaponLeft1, weaponLeft2);
            case RIGHT -> frame = getFrame(RIGHT, movementRight1, movementRight2, weaponRight1, weaponRight2);
        }
        if (flags.invincible) Utils.changeAlpha(g2, 0.3f);
        g2.drawImage(frame, tempScreenX, tempScreenY, null);
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
        direction = DOWN;
        // TODO Se podria comprobar si la posicion es valida o no
        // x = 23 * tile_size;
        // y = 21 * tile_size;
    }

    public void setDefaultValues() {
        type = Type.PLAYER;
        direction = DOWN;
        speed = defaultSpeed = 3;
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

        hitbox.x = 8;
        hitbox.y = 16;
        hitbox.width = 32;
        hitbox.height = 31;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        motion1 = 5;
        motion2 = 25;

        loadMovementImages(entity_player_movement, 16, 16, tile_size);
        loadWeaponImages(entity_player_sword, 16, 16);
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
            game.playSound(sound_burning);
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
     *
     * @param i indice del item.
     */
    private void pickup(int i) {
        if (i != -1) {
            Item item = world.items[world.map][i];
            if (keyboard.l && item.type != Type.OBSTACLE) {
                if (item.type == Type.PICKUP) item.use(this);
                else if (canPickup(item)) game.playSound(sound_pickup);
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
    public void hitInteractiveTile(int i) {
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
            Item selectedItem = (Item) inventory.get(itemIndex);
            if (selectedItem instanceof Axe || selectedItem instanceof Pickaxe || selectedItem instanceof SwordNormal) {
                weapon = selectedItem;
                attackbox = weapon.attackbox; // TODO Hace falta esto aca?
                attack = getAttack();
                switch (weapon.type) {
                    case SWORD -> {
                        loadWeaponImages(entity_player_sword, 16, 16);
                        game.playSound(sound_draw_sword);
                    }
                    case AXE -> loadWeaponImages(entity_player_axe, 16, 16);
                    case PICKAXE -> loadWeaponImages(entity_player_pickaxe, 16, 16);
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
        if (keyboard.s) direction = DOWN;
        else if (keyboard.w) direction = UP;
        else if (keyboard.a) direction = LEFT;
        else if (keyboard.d) direction = RIGHT;
    }

    /**
     * Comprueba las colisiones con tiles, items, mobs, tiles interactivos y eventos.
     */
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

    /**
     * Obtiene el frame de la direccion especificada ya sea de movimiento o ataque.
     *
     * @param direction direccion.
     * @param movement1 frame de movimiento 1.
     * @param movement2 frame de movimiento 2.
     * @param attack1   frame de ataque 1.
     * @param attack2   frame de ataque 2.
     * @return el frame de la direccion especificada ya sea de movimiento o ataque, o null.
     */
    private BufferedImage getFrame(int direction, BufferedImage movement1, BufferedImage movement2, BufferedImage attack1, BufferedImage attack2) {
        BufferedImage frame;
        if (!flags.hitting) {
            if (flags.collidingOnMob) frame = movementNum == 1 ? movement1 : movement2;
            else frame = movementNum == 1 || flags.colliding ? movement1 : movement2;
        } else {
            // Soluciona el bug para las imagenes de ataque up y left, ya que la posicion 0,0 de estas imagenes son tiles transparentes
            switch (direction) {
                case UP -> tempScreenY -= tile_size;
                case LEFT -> tempScreenX -= tile_size;
            }
            frame = attackNum == 1 ? attack1 : attack2;
        }
        return frame;
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
        movementDown1 = image;
        movementDown2 = image;
        movementUp1 = image;
        movementUp2 = image;
        movementLeft1 = image;
        movementLeft2 = image;
        movementRight1 = image;
        movementRight2 = image;
    }

    private void addItemsToInventory() {
        inventory.clear();
        inventory.add(weapon);
        inventory.add(shield);
        inventory.add(new PotionRed(game, world, 1));
        inventory.add(new Lantern(game, world));
        inventory.add(new Pickaxe(game, world));
        inventory.add(new Axe(game, world));
        inventory.add(new Tent(game, world));
    }

    private void drawRects(Graphics2D g2) {
        // Imagen
        // g2.setColor(Color.magenta);
        // g2.drawRect(screenX, screenY, tile_size, tile_size);
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
