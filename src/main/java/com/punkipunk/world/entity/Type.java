package com.punkipunk.world.entity;

import java.util.EnumSet;

/**
 * Types of entities.
 * <p>
 * TODO The CONSUMABLE constant could be combined, since a potion is grabable and consumable.
 * TODO AXE, SWORD and PICKAXE could be unified in WEAPON
 */

public enum Type {

    PLAYER, NPC, HOSTILE, SWORD, AXE, SHIELD, CONSUMABLE, PICKUP, OBSTACLE, LIGHT, PICKAXE;


    public static final EnumSet<Type> WEAPON = EnumSet.of(SWORD, AXE, PICKAXE);

}
