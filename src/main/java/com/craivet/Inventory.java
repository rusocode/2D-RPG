package com.craivet;

import com.craivet.world.entity.item.*;

import java.util.*;

import static com.craivet.utils.Global.*;

/**
 * The Inventory class has the basic methods of an inventory, such as adding an item (add), removing an item (remove),
 * obtaining the slot of the specified item (getSlot), among others.
 */

public class Inventory {

    protected List<Item> inventory;

    public Inventory() {
        inventory = new ArrayList<>();
    }

    /**
     * Add the item.
     *
     * @param item item.
     */
    public void add(Item item) {
        if (inventory.size() < MAX_INVENTORY_SLOTS) inventory.add(item);
    }

    /**
     * Returns the number of items.
     */
    public int size() {
        return inventory.size();
    }

    /**
     * Gets the item.
     *
     * @param i item index.
     * @return the item.
     */
    public Item get(int i) {
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
     * Gets the slot index.
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
