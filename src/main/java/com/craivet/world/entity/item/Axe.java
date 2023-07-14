package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.util.Utils;

import static com.craivet.util.Global.*;
import static com.craivet.gfx.Assets.*;

public class Axe extends Item {

    public static final String NAME = "Axe";

    /**
     * Crea el objeto en el inventario o en el World (utiliza varargs para especificar la posicion).
     */
    public Axe(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.AXE;
        image = Utils.scaleImage(axe, tile_size, tile_size);
        attackbox.width = 30;
        attackbox.height = 30;
        description = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
        price = 75;
        attackValue = 1;
        knockbackValue = 8;
    }

}
