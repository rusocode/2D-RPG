package com.punkipunk.json.model;

public record SpellData(
        String name,
        Integer speed,
        Integer hp,
        Integer attack,
        Integer knockback,
        Integer cost,
        Integer animationSpeed,
        Integer frameWidth,
        Integer frameHeight,
        Integer frameScale,
        Boolean alive,
        String texturePath,
        String spriteSheetPath) {

    public SpellData {
        if (speed == null) speed = 0;
        if (hp == null) hp = 0;
        if (attack == null) attack = 0;
        if (knockback == null) knockback = 0;
        if (cost == null) cost = 0;
        if (animationSpeed == null) animationSpeed = 0;
        if (frameWidth == null) frameWidth = 0;
        if (frameHeight == null) frameHeight = 0;
        if (frameScale == null) frameScale = 0;
        if (alive == null) alive = false;
    }


}
