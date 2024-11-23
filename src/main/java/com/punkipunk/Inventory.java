package com.punkipunk;

import com.punkipunk.world.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

import static com.punkipunk.utils.Global.MAX_INVENTORY_SLOTS;

/**
 * The Inventory class has the basic methods of an inventory, such as adding an item (add), removing an item (remove), obtaining
 * the slot of the specified item (getSlot), among others.
 */

public class Inventory {

    protected List<Item> inventory;

    public Inventory() {
        inventory = new ArrayList<>();
    }

    public void add(Item item) {
        if (inventory.size() < MAX_INVENTORY_SLOTS) inventory.add(item);
    }

    public int size() {
        return inventory.size();
    }

    /**
     * Obtiene el item en el indice especificado.
     *
     * @param i indice del item.
     * @return el item en el indice especificado.
     * @throws IndexOutOfBoundsException si el indice esta fuera de los limites.
     * @throws IllegalStateException     si el inventario esta vacio.
     */
    public Item get(int i) {
        if (inventory.isEmpty()) throw new IllegalStateException("El inventario esta vacio");
        if (i < 0 || i >= inventory.size())
            throw new IndexOutOfBoundsException("Indice " + i + " fuera de los limites. Tamaño del inventario: " + inventory.size());
        return inventory.get(i);
    }

    /**
     * Gets the slot index.
     *
     * @param col slot column.
     * @param row slot row.
     * @return the slot index.
     */
    public int getSlot(int col, int row) {
        int lastColumn = 5;
        return col + (row * lastColumn);
    }

    /**
     * Gets the index of the item.
     *
     * @param item item.
     * @return the index of the item.
     */
    public int getSlot(Item item) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == item) return i;
        return 0;
    }

    /**
     * Remove the item
     *
     * @param i index of the item.
     */
    public void remove(int i) {
        inventory.remove(i);
    }

    /**
     * Remove all items.
     */
    public void clear() {
        inventory.clear();
    }

    /**
     * Search for the item.
     *
     * @param name name of the item.
     * @return the index of the item or -1 if it is not found.
     */
    public int search(String name) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i).stats.name.equals(name)) return i;
        return -1;
    }

}
