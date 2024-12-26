package com.punkipunk.gui.container.equipment;

import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.player.Player;

public interface EquipmentStrategy {

    void equip(Player player, Item item);

    void unequip(Player player, Item item);

    boolean isEquipped(Player player, Item item);

}
