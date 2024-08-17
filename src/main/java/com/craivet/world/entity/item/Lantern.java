package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.TextureAssets;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;

public class Lantern extends Item {

    public static final String NAME = "Lantern";

    public Lantern(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.LIGHT;
        stats.name = NAME;
        description = "[" + stats.name + "]\nIlluminaties your \nsurroundings.";
        price = 45;
        lightRadius = 350;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.LANTERN), tile, tile);
    }

}
