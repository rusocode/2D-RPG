package com.punkipunk.inventory.equipment;

import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;
import com.punkipunk.world.entity.item.ItemType;

import java.util.EnumMap;
import java.util.Map;

public class Equipment {

    private final Map<ItemType, EquipmentStrategy> strategies = new EnumMap<>(ItemType.class);
    private final Player player;

    public Equipment(Player player) {
        this.player = player;
        registerStrategies();
    }

    private void registerStrategies() {
        EquipmentStrategy weaponStrategy = new WeaponEquipmentStrategy();
        for (ItemType weaponType : ItemType.WEAPON_TYPES)
            strategies.put(weaponType, weaponStrategy);
        strategies.put(ItemType.SHIELD, new ShieldEquipmentStrategy());
        strategies.put(ItemType.LIGHT, new LightEquipmentStrategy());
    }

    public void equip(Item item) {
        if (item == null) return;
        EquipmentStrategy strategy = strategies.get(item.itemType);
        if (strategy == null) throw new UnsupportedOperationException("No strategy for item type: " + item.itemType);
        strategy.equip(player, item);
    }

    public void unequip(Item item) {
        if (item == null) return;
        EquipmentStrategy strategy = strategies.get(item.itemType);
        if (strategy == null) throw new UnsupportedOperationException("No strategy for item type: " + item.itemType);
        strategy.unequip(player, item);
    }

    public EquipmentStrategy getStrategyForItemType(ItemType itemType) {
        return strategies.get(itemType);
    }

}
