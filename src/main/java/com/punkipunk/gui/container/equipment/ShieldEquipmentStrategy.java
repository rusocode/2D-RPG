package com.punkipunk.gui.container.equipment;

import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;

public class ShieldEquipmentStrategy extends BaseEquipmentStrategy {

    @Override
    protected void onEquip(Player player, Item item) {
        player.shield = item;
        player.stats.defense = player.getDefense();
    }

    @Override
    protected void onUnequip(Player player, Item item) {
        player.shield = null;
    }

    @Override
    protected boolean checkEquipped(Player player, Item item) {
        return player.shield == item;
    }

}
