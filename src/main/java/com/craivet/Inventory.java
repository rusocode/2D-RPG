package com.craivet;

import com.craivet.world.World;
import com.craivet.world.entity.Player;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.*;

import java.util.ArrayList;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * FIXME El inventario no tiene limites de items, usar 20 slots
 */

public class Inventory {

    private Game game;
    private World world;
    protected ArrayList<Item> inventory;
    // Variables que utiliza el cursor para representar su posicion en el inventario
    public int playerSlotCol, playerSlotRow, npcSlotCol, npcSlotRow;

    public Inventory() {
        inventory = new ArrayList<>();
    }

    public Inventory(Game game, World world) {
        this.game = game;
        this.world = world;
        inventory = new ArrayList<>();
    }

    /**
     * Agrega el item.
     *
     * @param item item.
     */
    public void add(Item item) {
        if (inventory.size() < MAX_INVENTORY_SLOTS) inventory.add(item);
    }

    /**
     * Devuelve la cantidad de items.
     */
    public int size() {
        return inventory.size();
    }

    /**
     * Obtiene el item.
     *
     * @param i indice del item.
     * @return el item.
     */
    public Item get(int i) {
        return inventory.get(i);
    }

    /**
     * Obtiene el indice del slot.
     *
     * @param col columna del slot.
     * @param row fila del slot.
     * @return el indice del slot.
     */
    public int getSlot(int col, int row) {
        int lastColumn = 5;
        return col + (row * lastColumn);
    }

    /**
     * Obtiene el indice del slot.
     *
     * @param item item.
     * @return el indice del item.
     */
    public int getSlot(Item item) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == item) return i;
        return 0;
    }

    /**
     * Elimina el item.
     *
     * @param i indice del item.
     */
    public void remove(int i) {
        inventory.remove(i);
    }

    /**
     * Elimina todos los items.
     */
    public void clear() {
        inventory.clear();
    }

    /**
     * Busca el item.
     *
     * @param name nombre del item.
     * @return el indice del item o -1 si no lo encuentra.
     */
    public int search(String name) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i).stats.name.equals(name)) return i;
        return -1;
    }

    /**
     * Recoge un item.
     * <p>
     * TODO No tendria que ir en la clase Item?
     *
     * @param i indice del item.
     */
    public void pickup(int i) {
        if (i != -1) {
            Item item = world.items[world.map][i];
            if (game.keyboard.p && item.type != Type.OBSTACLE) {
                if (item.type == Type.PICKUP) item.use(world.player);
                else if (canPickup(item)) game.playSound(sound_item_pickup);
                else {
                    game.ui.addMessageToConsole("You cannot carry any more!");
                    return;
                }
                world.items[world.map][i] = null;
            }
            if (game.keyboard.enter && item.type == Type.OBSTACLE) {
                world.player.attackCanceled = true;
                item.interact();
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
            world.player.weapon = world.player.weapon == item ? null : item; // Verifica si ya tiene equipada el weapon
            if (world.player.weapon != null) {
                world.player.stats.attack = world.player.getAttack();
                switch (world.player.weapon.type) {
                    case SWORD -> {
                        world.player.sheet.loadWeaponFrames(sword_frame, 16, 16);
                        world.player.game.playSound(sound_draw_sword);
                    }
                    case AXE -> world.player.sheet.loadWeaponFrames(axe_frame, 16, 16);
                    case PICKAXE -> world.player.sheet.loadWeaponFrames(pickaxe_frame, 16, 16);
                }
            }
        }
        if (item.type == Type.SHIELD) {
            world.player.shield = item;
            world.player.stats.defense = world.player.getDefense();
        }
        if (item.type == Type.LIGHT) {
            world.player.light = world.player.light == item ? null : item;
            world.player.lightUpdate = true;
        }
    }

    /**
     * Consume el item.
     *
     * @param item item.
     * @param i    indice del item.
     */
    private void consume(Item item, int i) {
        if (item.use(world.player)) {
            if (item.amount > 1) item.amount--;
            else inventory.remove(i);
        }
    }

}
