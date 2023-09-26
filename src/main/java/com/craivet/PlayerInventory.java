package com.craivet;

import com.craivet.world.World;
import com.craivet.world.entity.Player;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * Esta clase representa las funciones adicionales del inventario del player, como equipar o desequipar un item, agarrar
 * un item, seleccionar un item, entre otros.
 */

public class PlayerInventory extends Inventory {

    private final Game game;
    private final World world;
    private final Player player;

    // Variables que utiliza el cursor para representar su posicion en el inventario
    public int playerSlotCol, playerSlotRow, npcSlotCol, npcSlotRow;

    public PlayerInventory(Game game, World world, Player player) {
        super();
        this.game = game;
        this.world = world;
        this.player = player;
    }

    /**
     * Inicializa el inventario del player con los items por defecto.
     */
    public void init() {
        clear(); // Limpia el inventario en caso de que se cree un nuevo juego desde el menu sin cerrar la ventana
        add(player.weapon);
        add(player.shield);
        add(new Lantern(game, world));
        add(new PotionRed(game, world, 2));
        add(new Key(game, world, 2));
        add(new Pickaxe(game, world));
        add(new Axe(game, world));
        add(new Tent(game, world));
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
            int i = search(item.stats.name);
            // Si existe en el inventario, entonces solo aumenta la cantidad
            if (i != -1) {
                get(i).amount += item.amount;
                return true;
                // Si no existe en el inventario, lo agrega como nuevo item con su respectiva cantidad
            } else if (size() != MAX_INVENTORY_SLOTS) {
                add(newItem);
                // Al agregar un nuevo item, no puede utilizar el indice del item anterior, tiene que buscar el indice a partir del nuevo item
                get(search(item.stats.name)).amount += item.amount;
                return true;
            }
        } else if (size() != MAX_INVENTORY_SLOTS) { // TODO o < MAX_INVENTORY_SLOTS
            add(newItem);
            return true;
        }
        return false;
    }

    /**
     * Selecciona el item.
     */
    public void select() {
        int i = getSlot(playerSlotCol, playerSlotRow);
        if (i < inventory.size()) {
            Item item = inventory.get(i);
            if (item.type != Type.CONSUMABLE) equip(item);
            if (item.type == Type.CONSUMABLE) consume(item, i);
        }
    }

    /**
     * Equipa el item.
     *
     * @param item item.
     */
    private void equip(Item item) {
        if (item.type == Type.AXE || item.type == Type.PICKAXE || item.type == Type.SWORD) {
            player.weapon = player.weapon == item ? null : item; // Verifica si ya tiene equipada el weapon
            if (player.weapon != null) {
                player.stats.attack = player.getAttack();
                switch (player.weapon.type) {
                    case SWORD -> {
                        player.sheet.loadWeaponFrames(sword_frame, 16, 16);
                        player.game.playSound(sound_draw_sword);
                    }
                    case AXE -> player.sheet.loadWeaponFrames(axe_frame, 16, 16);
                    case PICKAXE -> player.sheet.loadWeaponFrames(pickaxe_frame, 16, 16);
                }
            }
        }
        if (item.type == Type.SHIELD) {
            player.shield = item;
            player.stats.defense = player.getDefense();
        }
        if (item.type == Type.LIGHT) {
            player.light = player.light == item ? null : item;
            player.lightUpdate = true;
        }
    }

    /**
     * Consume el item.
     *
     * @param item item.
     * @param i    indice del item.
     */
    private void consume(Item item, int i) {
        if (item.use(player)) {
            if (item.amount > 1) item.amount--;
            else inventory.remove(i);
        }
    }

}
