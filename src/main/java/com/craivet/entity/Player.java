package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.entity.item.*;
import com.craivet.entity.mob.Orc;
import com.craivet.entity.mob.Slime;
import com.craivet.entity.projectile.Fireball;
import com.craivet.input.KeyManager;
import com.craivet.tile.Interactive;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

/**
 * El player permanece fijo en el centro de la pantalla dando la sensacion de movimiento.
 *
 * <p>La colision entre dos entidades, solo se genera cuando el limite de la hitbox del player SUPERA el limite de la
 * hitbox del slime. Pero en el caso del attackbox, solo se genera colision cuando los limites de ambos SE TOCAN.
 */

public class Player extends Entity {

    private final KeyManager key;
    public final int screenX, screenY;
    public boolean attackCanceled, lightUpdate;

    public Player(Game game, World world) {
        super(game, world);
        // Posiciona el player en el centro de la pantalla
        screenX = SCREEN_WIDTH / 2 - (tile_size / 2);
        screenY = SCREEN_HEIGHT / 2 - (tile_size / 2);
        // Posiciona el player en el mundo
        // TODO Implementar metodo para verificar posicion valida
        // public static boolean isValid(short x, short y) {
        //        return (x > 0) && (y > 0) && (x <= MAPA_ANCHO) && (y <= MAPA_ALTO);
        //    }
        // Y tile no solido..
        x = 23 * tile_size;
        y = 21 * tile_size;
        key = game.getKey();
        setDefaultValues();
    }

    public void setDefaultValues() {
        type = TYPE_PLAYER;
        speed = defaultSpeed = 3;
        life = maxLife = 6;
        mana = maxMana = 4;
        ammo = 5;
        lvl = 1;
        exp = 0;
        nextLvlExp = 5;
        coin = 500;
        strength = 1;
        dexterity = 1;
        invincible = false;

        projectile = new Fireball(game, world);
        weapon = new SwordNormal(game, world);
        shield = new ShieldWood(game, world);
        light = null;
        attack = getAttack();
        defense = getDefense();

        hitbox.x = 8;
        hitbox.y = 16;
        hitbox.width = 32;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        motion1 = 5;
        motion2 = 25;

        loadMovementImages(entity_player_movement, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
        loadAttackImages(weapon.type == TYPE_SWORD ? entity_player_attack_sword : entity_player_attack_axe, ENTITY_WIDTH, ENTITY_HEIGHT);
        setItems();
    }

    public void setDefaultPosition() {
        x = 23 * tile_size;
        y = 21 * tile_size;
        direction = DOWN;
    }

    public void restoreStatus() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
        attacking = false;
        knockback = false;
        lightUpdate = true;
    }

    public void update() {

        if (attacking) attack();

        if (checkKeys()) {

            getDirection();
            checkCollisions();
            if (!collision && !key.enter && !key.l) updatePosition();
            checkAttack();

            // Resetea las teclas de accion
            key.enter = false;
            key.l = false;

            // Temporiza la animacion de movimiento solo cuando se presionan las teclas de movimiento
            if (checkMovementKeys()) timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);

        } else timer.timeNaturalStopWalking(this, 20);

        checkShoot();

        // Aplica el timer solo si el player es invencible
        if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
        if (timer.projectileCounter < INTERVAL_PROJECTILE_ATTACK) timer.projectileCounter++;
        if (timer.attackCounter < INTERVAL_SWORD_ATTACK) timer.attackCounter++;

        if (life > maxLife) life = maxLife;
        if (mana > maxMana) mana = maxMana;
        if (life <= 0) die();
    }

    public void render(Graphics2D g2) {
        BufferedImage frame = null;
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

        if (invincible) Utils.changeAlpha(g2, 0.3f);
        g2.drawImage(frame, tempScreenX, tempScreenY, null);

        // drawRects(g2);

        Utils.changeAlpha(g2, 1);

    }

    /**
     * Comprueba si puede atacar. No puede atacar si interactua con un npc o bebe agua.
     */
    private void checkAttack() {
        // Si presiono enter y el ataque no esta cancelado
        if (key.enter && !attackCanceled && timer.attackCounter == INTERVAL_SWORD_ATTACK) {
            if (weapon.type == TYPE_SWORD) game.playSound(sound_swing_weapon);
            if (weapon.type == TYPE_AXE) game.playSound(sound_swing_axe);
            attacking = true;
            timer.attackAnimationCounter = 0;
            timer.attackCounter = 0;
        }
        attackCanceled = false; // Para que pueda volver a atacar despues de interactuar con un npc o beber agua
    }

    /**
     * Comprueba si puede lanzar un proyectil.
     */
    private void checkShoot() {
        if (key.f && !projectile.alive && timer.projectileCounter == INTERVAL_PROJECTILE_ATTACK && projectile.haveResource(this)) {
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

    /**
     * Recoge un item.
     *
     * @param itemIndex indice del item.
     */
    private void pickUpItem(int itemIndex) {
        if (itemIndex != -1) {
            Entity item = world.items[world.map][itemIndex];
            if (key.l && item.type != TYPE_OBSTACLE) {
                if (item.type == TYPE_PICKUP_ONLY) item.use(this);
                else if (canObtainItem(item)) game.playSound(sound_pick_up);
                else {
                    game.ui.addMessage("You cannot carry any more!");
                    return;
                }
                world.items[world.map][itemIndex] = null;
            }
            if (key.enter) {
                attackCanceled = true;
                if (item.type == TYPE_OBSTACLE) item.interact();
            }
        }
    }

    /**
     * Interactua con el npc.
     *
     * @param npcIndex indice del npc.
     */
    private void interactNPC(int npcIndex) {
        if (npcIndex != -1 && key.enter) {
            attackCanceled = true; // No puede atacar si interactua con un npc
            game.state = DIALOGUE_STATE;
            world.npcs[world.map][npcIndex].speak();
        }
    }

    /**
     * El player recibe daño si colisiona con un mob.
     *
     * @param mobIndex indice del mob.
     */
    private void damagePlayer(int mobIndex) {
        if (mobIndex >= 0) {
            Entity mob = world.mobs[world.map][mobIndex];
            if (!invincible && !mob.dead) {
                game.playSound(sound_receive_damage);
                // En caso de que el ataque sea menor a la defensa, entonces no hace daño
                int damage = Math.max(mob.attack - defense, 1);
                life -= damage;
                invincible = true;
            }
        }
    }

    /**
     * Daña al mob.
     *
     * @param mobIndex       indice del mob.
     * @param attacker       atacante del mob.
     * @param knockbackValue valor de knockback.
     * @param attack         tipo de ataque (sword o fireball).
     */
    public void damageMob(int mobIndex, Entity attacker, int knockbackValue, int attack) {
        if (mobIndex != -1) { // TODO Lo cambio por >= 0 para evitar la doble negacion y comparacion -1?
            Entity mob = world.mobs[world.map][mobIndex];
            if (!mob.invincible) {

                if (knockbackValue > 0) setKnockback(mob, attacker, knockbackValue);

                int damage = Math.max(attack - mob.defense, 0);
                mob.life -= damage;
                game.ui.addMessage(damage + " damage!");
                if (mob.life > 0) {
                    if (mob instanceof Slime) game.playSound(sound_hit_slime);
                    if (mob instanceof Orc) {
                        game.playSound(sound_hit_monster);
                        game.playSound(sound_hit_orc);
                    }
                }

                mob.invincible = true;
                mob.hpBar = true;
                mob.damageReaction();

                if (mob.life <= 0) {
                    game.playSound(sound_mob_death);
                    mob.dead = true;
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
     * @param iTileIndex indice del tile interactivo.
     */
    protected void damageInteractiveTile(int iTileIndex) {
        if (iTileIndex != -1) {
            Interactive iTile = world.interactives[world.map][iTileIndex];
            if (iTile.destructible && iTile.isCorrectItem(weapon) && !iTile.invincible) {
                game.playSound(sound_cut_tree);

                iTile.life--;
                iTile.invincible = true;

                generateParticle(iTile, iTile);

                if (iTile.life == 0) world.interactives[world.map][iTileIndex] = iTile.getDestroyedForm();
            }
        }
    }

    protected void damageProjectile(int projectileIndex) {
        if (projectileIndex != -1) {
            game.playSound(sound_receive_damage);
            Entity projectile = world.projectiles[world.map][projectileIndex];
            projectile.alive = false;
            generateParticle(projectile, projectile);
        }
    }

    /**
     * Verifica si subio de nivel.
     */
    private void checkLevelUp() {
        if (exp >= nextLvlExp) {
            lvl++;
            nextLvlExp *= 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            game.playSound(sound_level_up);
            game.state = DIALOGUE_STATE;
            game.ui.currentDialogue = "You are level " + lvl + "!";
        }
    }

    /**
     * Selecciona el item del array de inventario utilizando el indice del slot del inventario UI.
     */
    public void selectItem() {
        int itemIndex = game.ui.getItemIndexOnSlot(game.ui.playerSlotCol, game.ui.playerSlotRow);
        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);
            if (selectedItem instanceof SwordNormal || selectedItem instanceof Axe) {
                weapon = selectedItem;
                attackbox = weapon.attackbox; // TODO Hace falta esto aca?
                attack = getAttack();
                if (weapon.type == TYPE_SWORD) game.playSound(sound_draw_sword);
                loadAttackImages(weapon.type == TYPE_SWORD ? entity_player_attack_sword : entity_player_attack_axe, ENTITY_WIDTH, ENTITY_HEIGHT);
            }
            if (selectedItem.type == TYPE_SHIELD) {
                shield = selectedItem;
                defense = getDefense();
            }
            if (selectedItem.type == TYPE_LIGHT) {
                light = light == selectedItem ? null : selectedItem;
                lightUpdate = true;
            }
            if (selectedItem.type == TYPE_CONSUMABLE) {
                if (selectedItem.use(this)) {
                    if (selectedItem.amount > 1) selectedItem.amount--;
                    else inventory.remove(itemIndex);
                }
            }
        }
    }

    /**
     * Verifica si puede obtener el item especificado y en caso afirmativo lo agrega al inventario.
     *
     * @param item el item especificado.
     * @return si se puede obtener el item o no.
     */
    public boolean canObtainItem(Entity item) {
        boolean canObtain = false;
        // Verifica si es stackable
        if (item.stackable) {
            // Verifica si ya existe en el inventario, y en caso de que ya exista aumenta la cantidad
            int itemIndex = searchItemInInventory(item.name);
            if (itemIndex != -1) {
                inventory.get(itemIndex).amount += item.amount;
                canObtain = true;
            } else if (inventory.size() != MAX_INVENTORY_SIZE) {
                inventory.add(item);
                canObtain = true;
            }
        } else if (inventory.size() != MAX_INVENTORY_SIZE) {
            inventory.add(item);
            canObtain = true;
        }
        return canObtain;
    }

    /**
     * Busca el item especificado en el inventario.
     *
     * @param name el nombre del item especificado.
     * @return el indice del item especificado.
     */
    private int searchItemInInventory(String name) {
        int itemIndex = -1;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).name.equals(name)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    /**
     * Obtiene la direccion dependiendo de la tecla seleccionada.
     */
    private void getDirection() {
        if (key.s) direction = DOWN;
        else if (key.w) direction = UP;
        else if (key.a) direction = LEFT;
        else if (key.d) direction = RIGHT;
    }

    /**
     * Comprueba las colisiones con tiles, items, npcs, mobs, tiles interactivos y eventos.
     */
    private void checkCollisions() {
        collision = false;
        game.collider.checkTile(this);
        pickUpItem(game.collider.checkItem(this));
        interactNPC(game.collider.checkEntity(this, world.npcs));
        damagePlayer(game.collider.checkEntity(this, world.mobs));
        game.collider.checkEntity(this, world.interactives);
        game.event.checkEvent();
    }

    /**
     * Actualiza la posicion del player.
     */
    private void updatePosition() {
        switch (direction) {
            case DOWN -> y += speed;
            case UP -> y -= speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
        }
    }

    private boolean checkKeys() {
        return checkMovementKeys() || checkAccionKeys();
    }

    private boolean checkMovementKeys() {
        return key.s || key.w || key.a || key.d;
    }

    private boolean checkAccionKeys() {
        return key.enter || key.l;
    }

    private void die() {
        game.state = GAME_OVER_STATE;
        game.playSound(sound_player_die);
        game.ui.command = -1;
        game.music.stop();
        attacking = false;
    }

    public int getAttack() {
        return strength * weapon.attackValue;
    }

    public int getDefense() {
        return dexterity * shield.defenseValue;
    }

    public int getCurrentWeaponSlot() {
        int currentWeaponSlot = 0;
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == weapon) currentWeaponSlot = i;
        return currentWeaponSlot;
    }

    public int getCurrentShieldSlot() {
        int currentShieldSlot = 0;
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == shield) currentShieldSlot = i;
        return currentShieldSlot;
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

    public void setItems() {
        inventory.clear();
        inventory.add(weapon);
        inventory.add(shield);
        inventory.add(new PotionRed(game, world, 15));
        inventory.add(new Key(game, world, 1));
        inventory.add(new Lantern(game, world));
    }

    private void drawRects(Graphics2D g2) {
        // Imagen
        g2.setColor(Color.magenta);
        g2.drawRect(screenX, screenY, tile_size, tile_size);
        // Cuerpo
        g2.setColor(Color.yellow);
        g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
        // Area de ataque
        if (attacking) {
            g2.setColor(Color.red);
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
