package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Axe extends Item {

    public static final String NAME = "Axe";

    public Axe(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.AXE;
        image = Utils.scaleImage(axe, tile, tile);
        description = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
        price = 75;
        // TODO No haria falta porque se ajusta en Entity
        attackbox.width = 30;
        attackbox.height = 30;
        attackValue = 1;
        knockbackValue = 8;
    }

}
