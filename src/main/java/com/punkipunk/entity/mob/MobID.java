package com.punkipunk.entity.mob;

/**
 * El nombre tiene que ser igual al objeto en mobs.json.
 */

public enum MobID {

    BAT("bat", MobCategory.PEACEFUL),
    BOX("box", MobCategory.NPC),
    LIZARD("lizard", MobCategory.HOSTILE),
    OLDMAN("oldman", MobCategory.NPC),
    ORC("orc", MobCategory.HOSTILE),
    RED_SLIME("redSlime", MobCategory.HOSTILE),
    SLIME("slime", MobCategory.HOSTILE),
    TRADER("trader", MobCategory.NPC),
    PLAYER_DUMMY("playerDummy", MobCategory.NPC);

    private final String name;
    private final MobCategory category;

    MobID(String name, MobCategory category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public MobCategory getCategory() {
        return category;
    }

}