package com.craivet;

import com.craivet.world.entity.item.*;

import java.util.*;

import static com.craivet.utils.Global.*;

/**
 * La clase Inventory tiene los metodos basicos de un inventario, como agregar un item (add), eliminar un item (remove),
 * obtener el slot del item especificado (getSlot), entre otros.
 * <p>
 * FIXME El inventario no tiene limites de items, usar 20 slots
 */

public class Inventory {

    protected List<Item> inventory;

    public Inventory() {
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

}
