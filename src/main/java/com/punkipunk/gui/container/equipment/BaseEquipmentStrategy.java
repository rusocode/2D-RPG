package com.punkipunk.gui.container.equipment;

import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;

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
