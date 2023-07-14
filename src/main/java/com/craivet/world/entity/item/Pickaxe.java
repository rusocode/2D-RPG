package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.util.Utils;

import static com.craivet.gfx.Assets.pickaxe;
import static com.craivet.util.Global.*;

public class Pickaxe extends Item {

    public static final String NAME = "Pickaxe";

    public Pickaxe(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.PICKAXE;
        image = Utils.scaleImage(pickaxe, tile_size, tile_size);
        attackbox.width = 30;
        attackbox.height = 30;
        description = "[" + name + "]\nYou will big it!";
        price = 75;
        attackValue = 1;
        knockbackValue = 8;
    }

}
