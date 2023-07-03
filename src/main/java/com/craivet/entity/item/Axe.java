package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Axe extends Item {

    public static final String item_name = "Axe";

    /**
     * Constructor solo para agregar Axe al inventario.
     */
    /* public Axe(Game game, World world) {
        super(game, world);
        name = item_name;
        type = TYPE_AXE;
        image = Utils.scaleImage(item_axe, tile_size, tile_size);
        attackbox.width = 30;
        attackbox.height = 30;
        description = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
        price = 75;
        attackValue = 1;
        knockbackValue = 8;
    } */

    /**
     * Constructor solo para agregar Axe al World.
     */
    public Axe(Game game, World world, int... pos/* int x, int y */) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1 /* x, y */);
        System.out.println(pos.length);
        name = item_name;
        type = TYPE_AXE;
        image = Utils.scaleImage(item_axe, tile_size, tile_size);
        attackbox.width = 30;
        attackbox.height = 30;
        description = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
        price = 75;
        attackValue = 1;
        knockbackValue = 8;
    }

}
