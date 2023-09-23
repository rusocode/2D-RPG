package com.craivet;

import com.craivet.world.entity.Player;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Axe;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.item.Pickaxe;
import com.craivet.world.entity.item.SwordIron;

import java.util.ArrayList;

import static com.craivet.gfx.Assets.*;
import static com.craivet.gfx.Assets.pickaxe_frame;

/**
 * FIXME El inventario no tiene limites de items, usar 20 slots
 */

public class Inventory {

    private Player player;
    private final ArrayList<Item> inventory;
    public int playerSlotCol, playerSlotRow, npcSlotCol, npcSlotRow;

    public Inventory() {
        inventory = new ArrayList<>();
    }

    public Inventory(Player player) {
        this.player = player;
        inventory = new ArrayList<>();
    }

    /**
     * Agrega el item.
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
     * Obtiene el item.
     *
     * @param i indice del item.
     */
    public Item get(int i) {
        return inventory.get(i);
    }

    /**
     * Obtiene el slot del item.
     *
     * @param slotCol columna del slot.
     * @param slotRow fila del slot.
     * @return el slot del item.
     */
    public int getSlot(int slotCol, int slotRow) {
        return slotCol + (slotRow * 5);
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
     * @return el indice del item; -1 si no lo encuentra.
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
     * @return el indice del slot; 0 si no esta.
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
