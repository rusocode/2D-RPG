package com.punkipunk.json.model;

public record MobData(
        String name,
        Integer speed,
        Integer hp,
        Integer exp,
        Integer gold,
        Integer probabilityItemDrop,
        Integer attack,
        Integer defense,
        Integer motion1,
        Integer motion2,
        Integer animationSpeed,
        Integer frameWidth,
        Integer frameHeight,
        Integer frameScale,
        Boolean boss,
        Boolean sleep,
        String texturePath,
        String spriteSheetPath,
        HitboxData hitbox,
        Integer attackboxWidth,
        Integer attackboxHeight) {

    public MobData {
        if (speed == null) speed = 0;
        if (hp == null) hp = 0;
        if (exp == null) exp = 0;
        if (gold == null) gold = 0;
        if (probabilityItemDrop == null) probabilityItemDrop = 0;
        if (attack == null) attack = 0;
        if (defense == null) defense = 0;
        if (motion1 == null) motion1 = 0;
        if (motion2 == null) motion2 = 0;
        if (animationSpeed == null) animationSpeed = 0;
        if (frameWidth == null) frameWidth = 0;
        if (frameHeight == null) frameHeight = 0;
        if (frameScale == null) frameScale = 0;
        if (boss == null) boss = false;
        if (sleep == null) sleep = false;
        if (attackboxWidth == null) attackboxWidth = 0;
        if (attackboxHeight == null) attackboxHeight = 0;
    }

}
