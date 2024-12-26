package com.punkipunk.gui.container.equipment;

import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.player.Player;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 * La <i>inicializacion en el punto de declaracion</i> es cuando asignas valores a campos directamente al declararlos en la clase,
 * fuera de cualquier metodo o constructor. Es una buena practica para campos final que no dependen de parametros externos, como
 * colecciones vacias o valores constantes, ya que ocurre antes de que el constructor se ejecute. Sin embargo, cuando necesitas
 * parametros del constructor, calculos complejos o manejo de excepciones, la inicializacion debe realizarse en el constructor.
 * Esta inicializacion ofrece <b>inmutabilidad</b>, <b>claridad</b> (hace mas claro que la coleccion {@code strategies} sea una
 * parte fundamental de la clase y siempre debe existir) y <b>seguridad</b> (evita NPE).
 */

// TODO El paquete quipment no debria ir en inventory?
public class Equipment {

    private final Player player;
    private final Map<ItemType, EquipmentStrategy> strategies = new EnumMap<>(ItemType.class);

    public Equipment(Player player) {
        this.player = player;
        registerStrategies();
    }

    private void registerStrategies() {
        EquipmentStrategy weaponStrategy = new WeaponEquipmentStrategy();
        for (ItemType weaponType : ItemType.WEAPON)
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
