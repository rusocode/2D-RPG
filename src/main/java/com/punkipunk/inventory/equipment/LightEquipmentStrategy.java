package com.punkipunk.inventory.equipment;

import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;

public class LightEquipmentStrategy extends BaseEquipmentStrategy {

    @Override
    protected void onEquip(Player player, Item item) {
        player.light = item;
        player.lightUpdate = true;
    }

    @Override
    protected void onUnequip(Player player, Item item) {
        player.light = null;
    }

    @Override
    protected boolean checkEquipped(Player player, Item item) {
        return player.light == item;
    }

}
