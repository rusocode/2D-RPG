package com.punkipunk.json.model;

public record ItemData(
        String name,
        String description,
        String sound,
        Integer price,
        Integer attackValue,
        Integer defenseValue,
        Integer knockbackValue,
        Integer points,
        Integer lightRadius,
        Integer frameWidth,
        Integer frameHeight,
        Integer frameScale,
        Boolean stackable,
        Boolean solid,
        String texturePath,
        String spriteSheetPath) {

    // Constructor con valores por defecto para campos opcionales
    public ItemData {
        // Si no se especifican, usar valores por defecto
        if (price == null) price = 0;
        if (attackValue == null) attackValue = 0;
        if (defenseValue == null) defenseValue = 0;
        if (knockbackValue == null) knockbackValue = 0;
        if (points == null) points = 0;
        if (lightRadius == null) lightRadius = 0;
        if (frameWidth == null) frameWidth = 0;
        if (frameHeight == null) frameHeight = 0;
        if (frameScale == null) frameScale = 0;
        if (stackable == null) stackable = false;
        if (solid == null) solid = false;
    }

}
