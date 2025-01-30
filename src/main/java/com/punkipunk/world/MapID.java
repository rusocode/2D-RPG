package com.punkipunk.world;

public enum MapID {

    ABANDONED_ISLAND(Zone.OVERWORLD),
    ABANDONED_ISLAND_MARKET(Zone.MARKET),
    DUNGEON_BREG(Zone.DUNGEON),
    DUNGEON_BREG_SUB(Zone.BOSS);

    public final Zone zone;

    MapID(Zone zone) {
        this.zone = zone;
    }

}
