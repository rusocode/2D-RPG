package com.craivet;

import com.craivet.world.entity.item.Item;

import java.util.ArrayList;

public class Inventory {

    private final ArrayList<Item> inventory;

    public Inventory() {
        inventory = new ArrayList<>();
    }

    /**
     * Agrega un item al inventario.
     *
     * @param item item.
     */
    public void add(Item item) {
        inventory.add(item);
    }

    /**
     * Devuelve la cantidad de items.
     */
    public int size() {
        return inventory.size();
    }

    /**
     * Obtiene el item del inventario.
     *
     * @param i indice del item.
     */
    public Item get(int i) {
        return inventory.get(i);
    }

    /**
     * Elimina un item del inventario.
     *
     * @param i indice del item.
     */
    public void remove(int i) {
        inventory.remove(i);
    }

    /**
     * Elimina todos los items del inventario.
     */
    public void clear() {
        inventory.clear();
    }

    /**
     * Busca el item en el inventario.
     *
     * @param name nombre del item.
     * @return el indice del item o -1 si no esta.
     */
    public int search(String name) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i).stats.name.equals(name)) return i;
        return -1;
    }

    /**
     * Obtiene el indice del slot en el que se encuentra el weapon.
     *
     * @param weapon weapon.
     * @return el indice del slot o 0 si no esta.
     */
    public int getCurrentWeaponSlot(Item weapon) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == weapon) return i;
        return 0; // TODO No es -1?
    }

    public int getCurrentShieldSlot(Item shield) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == shield) return i;
        return 0;
    }

    public int getCurrentLightSlot(Item light) {
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i) == light) return i;
        return 0;
    }


}
