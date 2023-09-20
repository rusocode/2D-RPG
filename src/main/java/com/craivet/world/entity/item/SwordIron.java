package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class SwordIron extends Item {

    public static final String NAME = "Iron Sword";

    public SwordIron(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        stats.name = NAME;
        stats.type = Type.SWORD;
        sheet.frame = Utils.scaleImage(sword_iron, tile, tile);
        description = "[" + stats.name + "]\nAn old sword.";
        price = 20;
        attackValue = 1;
        knockbackValue = 2;
    }

}
