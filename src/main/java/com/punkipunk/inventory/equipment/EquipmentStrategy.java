package com.punkipunk.inventory.equipment;

import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;

public interface EquipmentStrategy {

    void equip(Player player, Item item);

    void unequip(Player player, Item item);

    boolean isEquipped(Player player, Item item);

}
