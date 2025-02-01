package com.punkipunk.world;

public enum MapID {

    ABANDONED_ISLAND(Zone.FOREST),
    ABANDONED_ISLAND_MARKET(Zone.FOREST),
    DUNGEON_BREG(Zone.DUNGEON),
    DUNGEON_BREG_SUB(Zone.DUNGEON);

    public final Zone zone;

    MapID(Zone zone) {
        this.zone = zone;
    }

}
