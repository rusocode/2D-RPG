package com.punkipunk.json.model;

public record PlayerData(
        Integer lvl,
        Integer exp,
        Integer nextExp,
        Integer speed,
        Integer hp,
        Integer mana,
        Integer ammo,
        Integer gold,
        Integer strength,
        Integer dexterity,
        Integer motion1,
        Integer motion2,
        Integer frameScale,
        String spriteSheetPath
) {
}
