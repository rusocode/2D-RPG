package com.punkipunk.config.json;

/**
 * Maneja todo lo relacionado con la configuracion del item, es decir los valores intrinsecos.
 */

public record ItemConfig(
        String name,
        String type,
        String description,
        Integer price,
        Integer attackValue,
        Integer defenseValue,
        Integer knockbackValue,
        Integer points,
        Integer lightRadius,
        Boolean stackable,
        Boolean solid,
        String texture,
        String spriteSheet
) {

    // Constructor con valores por defecto para campos opcionales
    public ItemConfig {
        // Si no se especifican, usar valores por defecto
        if (price == null) price = 0;
        if (attackValue == null) attackValue = 0;
        if (defenseValue == null) defenseValue = 0;
        if (knockbackValue == null) knockbackValue = 0;
        if (points == null) points = 0;
        if (lightRadius == null) lightRadius = 0;
        if (stackable == null) stackable = false;
        if (solid == null) solid = false;
    }

}
