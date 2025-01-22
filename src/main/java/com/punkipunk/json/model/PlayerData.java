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
        Integer frameScale,
        String spriteSheetPath) {
}
