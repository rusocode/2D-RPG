package com.punkipunk.json.model;

public record ItemData(
        String name,
        String description,
        Integer price,
        Integer attack,
        Integer defense,
        Integer knockback,
        Integer points,
        Integer lightRadius,
        Integer frameWidth,
        Integer frameHeight,
        Integer frameScale,
        Boolean stackable,
        Boolean solid,
        String texturePath,
        String spriteSheetPath,
        HitboxData hitbox) {

    // Constructor con valores por defecto para campos opcionales
    public ItemData {
        if (price == null) price = 0;
        if (attack == null) attack = 0;
        if (defense == null) defense = 0;
        if (knockback == null) knockback = 0;
        if (points == null) points = 0;
        if (lightRadius == null) lightRadius = 0;
        if (frameWidth == null) frameWidth = 0;
        if (frameHeight == null) frameHeight = 0;
        if (frameScale == null) frameScale = 0;
        if (stackable == null) stackable = false;
        if (solid == null) solid = false;
    }

}
