package com.punkipunk.entity.item;

import java.util.Set;

public enum ItemType {

    SWORD, AXE, SHIELD, USABLE, LIGHT, PICKAXE, OBSTACLE, PICKUP;

    // Conjunto de constantes para tipos de items que comparten el mismo espacio de equipamiento
    public static final Set<ItemType> WEAPON = Set.of(SWORD, AXE, PICKAXE);

}
