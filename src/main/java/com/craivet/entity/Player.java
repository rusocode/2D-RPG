package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.entity.item.*;
import com.craivet.entity.mob.*;
import com.craivet.entity.npc.Npc;
import com.craivet.entity.projectile.Fireball;
import com.craivet.entity.projectile.Projectile;
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
    public int screenX, screenY;
    public boolean attackCanceled, shooting, lightUpdate;

    // Variable auxiliar para obtener los atributos de la entidad actual
    private Entity currentEntity;

    public Player(Game game, World world) {
        super(game, world);
        centerOnScreen();
        setDefaultPos();
        setDefaultValues();
        key = game.getKey();
    }

    /**
     * Es muy importante el orden de los metodos.
     */
    public void update() {
        // Si esta atacando, entonces ataca
        if (attacking) attack();

        // Comprueba las teclas presionadas de movimiento y accion
        if (checkKeys()) {
            // Obtiene la direccion dependiendo de la tecla presionada de movimiento (w, a, s, d)
            getDirection();
            // Comprueba las colisiones con los tiles, las entidades (items, npcs, mobs y tiles interactivos) y eventos
            checkCollision();
            // Si no colisiona y si no se presionaron las teclas de accion, entonces actualiza la posicion dependiendo de la direccion
            if (!collision && !checkAccionKeys()) updatePos();
            // Comprueba la velocidad de la direccion en caso de que se mueva en la misma direccion que la entidad
            checkDirectionSpeed();
            // Comrpueba si puede atacar
            checkAttack();
            // Resetea las teclas de accion
            resetAccionKeys();
            // Temporiza la animacion de movimiento solo cuando se presionan las teclas de movimiento
            if (checkMovementKeys()) timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);
        } else {
            // Temporiza la detencion del movimiento cuando se dejan de presionar las teclas de movimiento
            timer.timeStopMovement(this, 20);
        }

        // Comprueba si puede lanzar un proyectil
        checkShoot();

        // Aplica el timer solo si el player es invencible
        if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
        if (timer.projectileCounter < INTERVAL_PROJECTILE) timer.projectileCounter++;
        if (timer.attackCounter < INTERVAL_WEAPON) timer.attackCounter++;

        if (HP > maxHP) HP = maxHP;
        if (mana > maxMana) mana = maxMana;
        if (HP <= 0) die();
    }

    public void render(Graphics2D g2) {
        BufferedImage frame = null;
        int tempScreenX = screenX, tempScreenY = screenY;
        switch (direction) {
            case DOWN -> {
                if (!attacking) {
                    // frame = movementNum == 1 || collision ? movementDown1 : movementDown2;
                    if (collisionOnEntity) {
                        frame = movementNum == 1 ? movementDown1 : movementDown2;
                        System.out.println("asd");
                    }
                    else {
                        frame = movementNum == 1 || collision ? movementDown1 : movementDown2;
                    }
                }
                if (attacking) frame = attackNum == 1 ? weaponDown1 : weaponDown2;
            }
            case UP -> {
                if (!attacking) frame = movementNum == 1 || collision ? movementUp1 : movementUp2;
                if (attacking) {
                    // Soluciona el bug para las imagenes de ataque up y left, ya que la posicion 0,0 de estas imagenes son tiles transparentes
                    tempScreenY -= tile_size;
                    frame = attackNum == 1 ? weaponUp1 : weaponUp2;
                }
            }
            case LEFT -> {
                if (!attacking) frame = movementNum == 1 || collision ? movementLeft1 : movementLeft2;
                if (attacking) {
                    tempScreenX -= tile_size;
                    frame = attackNum == 1 ? weaponLeft1 : weaponLeft2;
                }
            }
            case RIGHT -> {
                if (!attacking) frame = movementNum == 1 || collision ? movementRight1 : movementRight2;
                if (attacking) frame = attackNum == 1 ? weaponRight1 : weaponRight2;
            }
        }

        if (invincible) Utils.changeAlpha(g2, 0.3f);
        g2.drawImage(frame, tempScreenX, tempScreenY, null);

        drawRects(g2);

        Utils.changeAlpha(g2, 1);

    }

    private void centerOnScreen() {
        screenX = SCREEN_WIDTH / 2 - (tile_size / 2);
        screenY = SCREEN_HEIGHT / 2 - (tile_size / 2);
    }

    public void setDefaultPos() {
        world.area = OUTSIDE;
        world.map = NIX;
        x = 23 * tile_size;
        y = 21 * tile_size;
    }

    public void setDefaultValues() {
        type = TYPE_PLAYER;
        direction = DOWN;
        speed = defaultSpeed = 3;
        HP = maxHP = 6;
        mana = maxMana = 4;
        ammo = 5;
        lvl = 1;
        exp = 0;
        nextLvlExp = 5;
        coin = 500;
        strength = 0;
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
        hitbox.height = 30;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        motion1 = 5;
        motion2 = 25;

        loadMovementImages(entity_player_movement, 16, 16, tile_size);
        loadWeaponImages(entity_player_sword, 16, 16);
        setItems();
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
        HP = maxHP;
        mana = maxMana;
        invincible = false;
        attacking = false;
        knockback = false;
        lightUpdate = true;
    }

    /**
     * Comprueba si puede atacar. No puede atacar si interactua con un npc o bebe agua.
     */
    private void checkAttack() {
        // Si presiono enter y el ataque no esta cancelado
        if (key.enter && !attackCanceled && timer.attackCounter == INTERVAL_WEAPON && !shooting) {
            if (weapon.type == TYPE_SWORD) game.playSound(sound_swing_weapon);
            if (weapon.type != TYPE_SWORD) game.playSound(sound_swing_axe);
            attacking = true;
            timer.attackAnimationCounter = 0;
            timer.attackCounter = 0;
        }
        shooting = false;
        attackCanceled = false; // Para que pueda volver a atacar despues de interactuar con un npc o beber agua
    }

    /**
     * Comprueba si puede lanzar un proyectil.
     */
    private void checkShoot() {
        if (key.f && !projectile.alive && timer.projectileCounter == INTERVAL_PROJECTILE && projectile.haveResource(this) && !attacking) {
            shooting = true;
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
                if (item.type == TYPE_OBSTACLE) {
                    attackCanceled = true;
                    item.interact();
                }
            }
        }
    }

    /**
     * Interactua con el npc.
     *
     * @param npcIndex indice del npc.
     */
    private void interactNPC(int npcIndex) {
        if (npcIndex != -1) {
            // System.out.println("Colision!");
            currentEntity = world.npcs[world.map][npcIndex];
            if (key.enter) {
                attackCanceled = true; // No puede atacar si interactua con un npc
                world.npcs[world.map][npcIndex].speak();
            }
            world.npcs[world.map][npcIndex].move(direction);
        }
    }

    /**
     * El player recibe daño si colisiona con un mob.
     *
     * @param mobIndex indice del mob.
     */
    private void hurtPlayer(int mobIndex) {
        if (mobIndex >= 0) {
            currentEntity = world.mobs[world.map][mobIndex];
            Entity mob = world.mobs[world.map][mobIndex];
            if (!invincible && !mob.dead) {
                game.playSound(sound_receive_damage);
                // En caso de que el ataque sea menor a la defensa, entonces no hace daño
                int damage = Math.max(mob.attack - defense, 1);
                HP -= damage;
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
    public void hurtMob(int mobIndex, Entity attacker, int knockbackValue, int attack) {
        if (mobIndex != -1) { // TODO Lo cambio por >= 0 para evitar la doble negacion y comparacion -1?\
            currentEntity = world.mobs[world.map][mobIndex];
            Entity mob = world.mobs[world.map][mobIndex];
            if (!mob.invincible) {

                if (knockbackValue > 0) setKnockback(mob, attacker, knockbackValue);

                int damage = Math.max(attack - mob.defense, 0);
                mob.HP -= damage;
                game.ui.addMessage(damage + " damage!");
                if (mob.HP > 0) {
                    if (mob instanceof Slime) game.playSound(sound_hit_slime);
                    if (mob instanceof Orc) {
                        game.playSound(sound_hit_monster);
                        game.playSound(sound_hit_orc);
                    }
                }

                mob.invincible = true;
                mob.hpBar = true;
                mob.damageReaction();

                if (mob.HP <= 0) {
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
            if (iTile.destructible && iTile.isCorrectWeapon(weapon) && !iTile.invincible) {
                iTile.playSound();
                iTile.HP--;
                iTile.invincible = true;

                generateParticle(iTile, iTile);

                if (iTile.HP == 0) {
                    iTile.checkDrop();
                    world.interactives[world.map][iTileIndex] = iTile.replaceBy();
                }
            }
        }
    }

    protected void damageProjectile(int projectileIndex) {
        if (projectileIndex != -1) {
            currentEntity = world.projectiles[world.map][projectileIndex];
            Entity projectile = world.projectiles[world.map][projectileIndex];
            // Evita daniar el propio proyectil
            if (projectile != this.projectile) {
                game.playSound(sound_receive_damage);
                projectile.alive = false;
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
            maxHP += 2;
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
            Entity selectedItem = inventory.get(itemIndex);
            if (selectedItem instanceof Axe || selectedItem instanceof Pickaxe || selectedItem instanceof SwordNormal) {
                weapon = selectedItem;
                attackbox = weapon.attackbox; // TODO Hace falta esto aca?
                attack = getAttack();
                switch (weapon.type) {
                    case TYPE_SWORD -> {
                        loadWeaponImages(entity_player_sword, 16, 16);
                        game.playSound(sound_draw_sword);
                    }
                    case TYPE_AXE -> loadWeaponImages(entity_player_axe, 16, 16);
                    case TYPE_PICKAXE -> loadWeaponImages(entity_player_pickaxe, 16, 16);
                }
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
        // Evita la referencia al mismo item
        Entity newItem = game.generator.getItem(item.name);
        // Verifica si es stackable
        if (item.stackable) {
            // Verifica si ya existe en el inventario, y en caso de que ya exista aumenta la cantidad
            int itemIndex = searchItemInInventory(item.name);
            if (itemIndex != -1) {
                inventory.get(itemIndex).amount += item.amount;
                canObtain = true;
            } else if (inventory.size() != MAX_INVENTORY_SIZE) {
                inventory.add(newItem);
                canObtain = true;
            }
        } else if (inventory.size() != MAX_INVENTORY_SIZE) {
            inventory.add(newItem);
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
    public void checkCollision() {
        collision = false;
        game.collider.checkTile(this);
        pickUpItem(game.collider.checkItem(this));
        interactNPC(game.collider.checkEntity(this, world.npcs));
        hurtPlayer(game.collider.checkEntity(this, world.mobs));
        game.collider.checkEntity(this, world.interactives);
        game.event.checkEvent();
    }

    /**
     * Actualiza la posicion.
     */
    private void updatePos() {
        switch (direction) {
            case DOWN -> y += speed;
            case UP -> y -= speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
        }
    }

    /**
     * Comprueba la velocidad de la direccion cuando colisiona con un Npc en movimiento en la misma direccion. Esto
     * se hace para evitar el "tartamudeo" en la animacion de movimiento del Player. En ese caso, iguala la velocidad a
     * la de la entidad. En caso contrario, vuelve a la velocidad por defecto y desactiva el estado collisionOnEntity.
     * <p>
     * Pero hay un pequeño problema. Por ejemplo, al igualar la velocidad a la del Oldman que se mueve hacia la derecha
     * y querer dialogar al mismo tiempo, no lo podria hacer ya que la posicion del Player se actualiza 1 pixel antes a
     * la del Oldman, por lo que no colisionan y por eso mismo no pueden dialogar. Es importante aclarar que en el
     * Collider se utiliza la velocidad por defecto y nunca la actualizada.
     */
    private void checkDirectionSpeed() {
        // Si colisiona con la entidad y esta en la misma direccion y no hay distancia y es un Npc
        if (collisionOnEntity && direction == currentEntity.direction && !checkEntityDistance() && currentEntity instanceof Npc)
            speed = currentEntity.speed;
        else {
            speed = defaultSpeed;
            collisionOnEntity = false;
        }

        // Desactiva el estado collisionOnEntity cuando ataca para mantener la velocidad normal
        /* if (attacking && collisionOnEntity) {
            speed = defaultSpeed;
            collisionOnEntity = false;
        } */

    }

    /**
     * Comprueba la distancia con la entidad. Cuando sigue (siempre colisionando) a la entidad en la misma direccion
     * pero en algun momento la deja de seguir y esta se mantiene en la misma direccion, entonces la velocidad no se
     * actualiza. Comprobando la distancia se soluciona el problema.
     * <p>
     * Es neseario sumar o restar 1 pixel dependiendo de la direccion para que haya una diferencia en la distancia y
     * se pueda comprobar.
     *
     * @return true si se cumple la distancia especificada, false en caso contrario.
     */
    private boolean checkEntityDistance() {
        switch (currentEntity.direction) {
            case DOWN -> {
                if (y + hitbox.y + hitbox.height + 1 < currentEntity.y + currentEntity.hitbox.y)
                    return true;
            }
            case UP -> {
                if (y + hitbox.y - 1 > currentEntity.y + currentEntity.hitbox.y + currentEntity.hitbox.height)
                    return true;
            }
            case LEFT -> {
                if (x + hitbox.x - 1 > currentEntity.x + currentEntity.hitbox.x + currentEntity.hitbox.width)
                    return true;
            }
            case RIGHT -> {
                if (x + hitbox.x + hitbox.width + 1 < currentEntity.x + currentEntity.hitbox.x)
                    return true;
            }
        }
        return false;
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

    private void resetAccionKeys() {
        key.enter = false;
        key.l = false;
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

    public int getCurrentLightSlot() {
        int currentLightSlot = 0;
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == light) currentLightSlot = i;
        return currentLightSlot;
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

    private void setItems() {
        inventory.clear();
        inventory.add(weapon);
        inventory.add(shield);
        inventory.add(new PotionRed(game, world, 15));
        inventory.add(new Key(game, world, 2));
        inventory.add(new Lantern(game, world));
        inventory.add(new Pickaxe(game, world));
    }

    private void drawRects(Graphics2D g2) {
        // Imagen
        // g2.setColor(Color.magenta);
        // g2.drawRect(screenX, screenY, tile_size, tile_size);
        // Hitbox
        g2.setColor(Color.red);
        g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
        // Area de ataque
        if (attacking) {
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
