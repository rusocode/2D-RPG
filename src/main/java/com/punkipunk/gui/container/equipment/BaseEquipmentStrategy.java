package com.punkipunk.gui.container.equipment;

import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.player.Player;
import com.punkipunk.entity.item.Item;

/**
 * El patron que se esta usando aqui es el Template Method, donde:
 * <ul>
 * <li>La clase base proporciona la estructura general del algoritmo (equip, unequip, isEquipped)
 * <li>Las subclases solo necesitan preocuparse por implementar los detalles especificos (onEquip, onUnequip, checkEquipped)
 * </ul>
 * <p>
 * Este diseño tiene ventajas:
 * <ul>
 * <li>Separa claramente la interfaz publica del comportamiento interno
 * <li>Hace mas evidente que metodos deben implementar las subclases
 * <li>Previene que las subclases modifiquen accidentalmente la logica general del algoritmo
 * </ul>
 * <p>
 * Los metodos protected abstractos (onEquip, onUnequip, checkEquipped) son parte de la implementacion interna y representan los
 * "hooks" o puntos de extension que las subclases deben implementar.
 */

public abstract class BaseEquipmentStrategy implements EquipmentStrategy {

    protected abstract void onEquip(Player player, Item item);

    protected abstract void onUnequip(Player player, Item item);

    protected abstract boolean checkEquipped(Player player, Item item);

    @Override
    public void equip(Player player, Item item) {
        if (isEquipped(player, item)) {
            unequip(player, item);
            return;
        }
        onEquip(player, item);
    }

    @Override
    public void unequip(Player player, Item item) {
        onUnequip(player, item);
    }

    @Override
    public boolean isEquipped(Player player, Item item) {
        return checkEquipped(player, item);
    }

}
