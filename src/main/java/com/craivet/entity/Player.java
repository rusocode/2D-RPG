package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.entity.item.*;
import com.craivet.entity.mob.*;
import com.craivet.entity.npc.BigRock;
import com.craivet.entity.npc.Npc;
import com.craivet.entity.projectile.Fireball;
import com.craivet.input.Key;
import com.craivet.tile.Interactive;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

/**
 * El player permanece fijo en el centro de la pantalla dando la sensacion de movimiento.
 */

public class Player extends Entity {

    private final Key key;
    public int screenX, screenY;
    private int tempScreenX, tempScreenY;
    public boolean attackCanceled, shooting, lightUpdate;

    // Variable auxiliar para obtener los atributos de la entidad actual
    private Entity currentEntity;
    // Se utiliza para indicar cuando esta unido a una entidad en movimiento
    private boolean united;

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
        // Si esta golpeando, entonces golpea
        if (isHitting) hit();
        // Comprueba las teclas presionadas de movimiento y accion
        if (checkKeys()) {
            // Obtiene la direccion dependiendo de la tecla presionada de movimiento (w, a, s, d)
            getDirection();
            // Comprueba las colisiones con los tiles, las entidades (items, npcs, mobs y tiles interactivos) y eventos
            checkCollision();
            // Si no colisiona y si no se presionaron las teclas de accion, entonces actualiza la posicion dependiendo de la direccion
            if (!isColliding && !checkAccionKeys()) updatePos();
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
        if (isInvincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
        if (timer.projectileCounter < INTERVAL_PROJECTILE) timer.projectileCounter++;
        if (timer.attackCounter < INTERVAL_WEAPON) timer.attackCounter++;

        if (HP > maxHP) HP = maxHP;
        if (mana > maxMana) mana = maxMana;
        if (HP <= 0) die();
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
        if (isInvincible) Utils.changeAlpha(g2, 0.3f);
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
        direction = DOWN;
        // TODO Se podria comprobar si la posicion es valida o no
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
        gold = 500;
        strength = 1;
        dexterity = 1;
        isInvincible = false;

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
        isInvincible = false;
        isHitting = false;
        isKnockback = false;
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
            isHitting = true;
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
        if (key.f && !projectile.isAlive && timer.projectileCounter == INTERVAL_PROJECTILE && projectile.haveResource(this) && !isHitting) {
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
    private void pickup(int itemIndex) {
        if (itemIndex != -1) {
            Entity item = world.items[world.map][itemIndex];
            if (key.l && item.type != TYPE_OBSTACLE) {
                if (item.type == TYPE_PICKUP_ONLY) item.use(this);
                else if (canPickup(item)) game.playSound(sound_pickup);
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
    private void interactNpc(int npcIndex) {
        if (npcIndex != -1) {
            currentEntity = world.npcs[world.map][npcIndex];
            if (key.enter) {
                attackCanceled = true;
                world.npcs[world.map][npcIndex].speak();
            } else world.npcs[world.map][npcIndex].move(direction);
        }
    }

    /**
     * El player recibe daño si colisiona con un mob.
     *
     * @param mobIndex indice del mob.
     */
    private void hitPlayer(int mobIndex) {
        if (mobIndex >= 0) {
            currentEntity = world.mobs[world.map][mobIndex];
            Entity mob = world.mobs[world.map][mobIndex];
            if (!isInvincible && !mob.isDead) {
                game.playSound(sound_receive_damage);
                int damage = Math.max(mob.attack - defense, 1);
                HP -= damage;
                isInvincible = true;
            }
        }
    }

    /**
     * Golpea al mob.
     *
     * @param mobIndex       indice del mob.
     * @param attacker       atacante del mob.
     * @param knockbackValue valor de knockback.
     * @param attack         tipo de ataque (sword o fireball).
     */
    public void hitMob(int mobIndex, Entity attacker, int knockbackValue, int attack) {
        if (mobIndex != -1) { // TODO Lo cambio por >= 0 para evitar la doble negacion y comparacion -1?
            currentEntity = world.mobs[world.map][mobIndex];
            Entity mob = world.mobs[world.map][mobIndex];
            if (!mob.isInvincible) {

                if (knockbackValue > 0) setKnockback(mob, attacker, knockbackValue);

                int damage = Math.max(attack - mob.defense, 1);
                mob.HP -= damage;
                game.ui.addMessage(damage + " damage!");
                if (mob.HP > 0) {
                    if (mob instanceof Slime || mob instanceof RedSlime) game.playSound(sound_hit_slime);
                    if (mob instanceof Orc) {
                        game.playSound(sound_hit_mob);
                        game.playSound(sound_hit_orc);
                    }
                }

                mob.isInvincible = true;
                mob.hpBar = true;
                mob.damageReaction();

                if (mob.HP <= 0) {
                    game.playSound(sound_mob_death);
                    mob.isDead = true;
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
    protected void hitInteractiveTile(int iTileIndex) {
        if (iTileIndex != -1) {
            currentEntity = world.interactives[world.map][iTileIndex];
            Interactive iTile = world.interactives[world.map][iTileIndex];
            if (iTile.destructible && iTile.isCorrectWeapon(weapon) && !iTile.isInvincible) {
                iTile.playSound();
                iTile.HP--;
                iTile.isInvincible = true;

                generateParticle(iTile, iTile);

                if (iTile.HP == 0) {
                    iTile.checkDrop();
                    world.interactives[world.map][iTileIndex] = iTile.replaceBy();
                }
            }
        }
    }

    protected void hitProjectile(int projectileIndex) {
        if (projectileIndex != -1) {
            currentEntity = world.projectiles[world.map][projectileIndex];
            Entity projectile = world.projectiles[world.map][projectileIndex];
            // Evita daniar el propio proyectil
            if (projectile != this.projectile) {
                game.playSound(sound_receive_damage);
                projectile.isAlive = false;
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
     * Verifica si puede recoger el item y en caso afirmativo lo agrega al inventario.
     *
     * @param item item.
     * @return true si se puede recoger el item o false.
     */
    public boolean canPickup(Entity item) {
        // Evita la referencia al mismo item
        Item newItem = /*game.generator.*/getItem(item.name);
        if (item.stackable) {
            int itemIndex = searchItemInInventory(item.name);
            if (itemIndex != -1) {
                inventory.get(itemIndex).amount += item.amount;
                return true;
            } else if (inventory.size() != MAX_INVENTORY_SIZE) {
                inventory.add(newItem);
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
        if (key.s) direction = DOWN;
        else if (key.w) direction = UP;
        else if (key.a) direction = LEFT;
        else if (key.d) direction = RIGHT;
    }

    /**
     * Comprueba las colisiones con tiles, items, npcs, mobs, tiles interactivos y eventos.
     */
    public void checkCollision() {
        isColliding = false;
        game.collision.checkTile(this);
        pickup(game.collision.checkItem(this));
        interactNpc(game.collision.checkEntity(this, world.npcs));
        hitPlayer(game.collision.checkEntity(this, world.mobs));
        setCurrentInteractive(game.collision.checkEntity(this, world.interactives));
        // game.collider.checkEntity(this, world.interactives);
        game.event.checkEvent();
    }

    private void setCurrentInteractive(int iTileIndex) {
        if (iTileIndex != -1) currentEntity = world.interactives[world.map][iTileIndex];
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
     * Comprueba la velocidad de la direccion cuando colisiona con una entidad en movimiento en la misma direccion. Esto
     * se hace para evitar un "tartamudeo" en la animacion de movimiento del Player. En ese caso, "une" el Player a la
     * entidad. En caso contrario, "desune" el Player de la entidad.
     */
    private void checkDirectionSpeed() {
        if (checkSomeConditionsForUnion()) unite();
        else disunite();
        // Desactiva el estado collisionOnEntity cuando ataca para mantener la velocidad normal (usarlo en caso aplicar en Mobs)
        /* if (attacking && collisionOnEntity) {
            speed = defaultSpeed;
            collisionOnEntity = false;
        } */
    }

    /**
     * Comprueba si la entidad actual es distinta a null, y si la entidad actual es un Npc, y si el Player colisiono con
     * la entidad, y si el Player esta en la misma direccion que la entidad actual, y si el Player no tiene distancia
     * con la entidad y si la entidad actual no colisiono.
     *
     * @return true si se cumplen todas las condiciones especificadas o false.
     */
    private boolean checkSomeConditionsForUnion() {
        return currentEntity != null && currentEntity instanceof Npc &&
                isCollidingOnEntity && direction == currentEntity.direction &&
                !isDistanceWithEntity() && !currentEntity.isColliding;
    }

    /**
     * Comprueba si hay distancia con la entidad.
     * <p>
     * <h3>¿Para que hace esto?</h3>
     * Cuando sigue (siempre colisionando) a la entidad pero en algun momento la deja de seguir y esta se mantiene en la
     * misma direccion, la velocidad va a seguir siendo la misma a la de la entidad. Entonces para solucionar ese
     * problema se comprueba la distancia, y si hay distancia entre el Player y la entidad, vuelve a la velocidad por
     * defecto.
     *
     * @return true si hay distancia o false.
     */
    private boolean isDistanceWithEntity() {
        switch (currentEntity.direction) {
            case DOWN -> {
                if (y + hitbox.y + hitbox.height + currentEntity.speed < currentEntity.y + currentEntity.hitbox.y)
                    return true;
            }
            case UP -> {
                if (y + hitbox.y - currentEntity.speed > currentEntity.y + currentEntity.hitbox.y + currentEntity.hitbox.height)
                    return true;
            }
            case LEFT -> {
                if (x + hitbox.x - currentEntity.speed > currentEntity.x + currentEntity.hitbox.x + currentEntity.hitbox.width)
                    return true;
            }
            case RIGHT -> {
                if (x + hitbox.x + hitbox.width + currentEntity.speed < currentEntity.x + currentEntity.hitbox.x)
                    return true;
            }
        }
        return false;
    }

    /**
     * Une el Player a la entidad.
     * <p>
     * Iguala la velocidad de la entidad a la del Player y dependiendo de la direccion, suma o resta un pixel. Esto
     * ultimo se hace para poder hablar con el Npc si este esta en movimiento.
     */
    private void unite() {
        speed = currentEntity.speed;
        united = true;
        if (!(currentEntity instanceof BigRock)) {
            switch (direction) {
                case DOWN -> y++;
                case UP -> y--;
                case LEFT -> x--;
                case RIGHT -> x++;
            }
        }
    }

    /**
     * Desune el Player de la entidad.
     * <p>
     * Vuelve a la velocidad por defecto y verifica si estan unidos para "destrabar" ambas entidades restando o sumando
     * un pixel.
     */
    private void disunite() {
        speed = defaultSpeed;
        isCollidingOnEntity = false;
        if (united) {
            switch (direction) {
                case DOWN -> y--;
                case UP -> y++;
                case LEFT -> x++;
                case RIGHT -> x--;
            }
            united = false;
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

    private void resetAccionKeys() {
        key.enter = false;
        key.l = false;
    }

    private void die() {
        game.state = GAME_OVER_STATE;
        game.playSound(sound_player_die);
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
        // Si no esta atacando
        if (!isHitting) {
            // Si colisiona con una entidad
            if (isCollidingOnEntity) {
                // Utiliza el frame movement1 si movementNum es 1 o el frame movement2 en caso contrario
                frame = movementNum == 1 ? movement1 : movement2;
                // Si la entidad actual esta colisionando, entonces utiliza el frame movement1
                if (currentEntity.isColliding) frame = movement1;
            } else frame = movementNum == 1 || isColliding ? movement1 : movement2;
        } else {
            // Soluciona el bug para las imagenes de ataque up y left, ya que la posicion 0,0 de estas imagenes son tiles transparentes
            switch (direction) {
                case UP -> tempScreenY -= tile_size;
                case LEFT -> tempScreenX -= tile_size;
            }
            // Si esta atacando utiliza el frame attack1 si attackNum es 1 o el frame attack2 en caso contrario
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

    private void setItems() {
        inventory.clear();
        inventory.add(weapon);
        inventory.add(shield);
        inventory.add(new PotionRed(game, world, 15));
        inventory.add(new com.craivet.entity.item.Key(game, world, 2));
        inventory.add(new Lantern(game, world));
        inventory.add(new Pickaxe(game, world));
        inventory.add(new Axe(game, world));
    }

    private void drawRects(Graphics2D g2) {
        // Imagen
        // g2.setColor(Color.magenta);
        // g2.drawRect(screenX, screenY, tile_size, tile_size);
        // Hitbox
        g2.setColor(Color.red);
        g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
        // Area de ataque
        if (isHitting) {
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
