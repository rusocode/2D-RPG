package com.punkipunk.entity.item;

import java.util.Set;

/**
 * TODO Cambiar PICKUP
 */

public enum ItemCategory {

    SWORD, AXE, SHIELD, USABLE, LIGHT, PICKAXE, OBSTACLE, PICKUP;

    // Conjunto de constantes para tipos de items que comparten el mismo espacio de equipamiento
    public static final Set<ItemCategory> WEAPON = Set.of(SWORD, AXE, PICKAXE);

    public boolean isWeapon() {
        return WEAPON.contains(this);
    }

}
