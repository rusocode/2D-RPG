package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.TextureAssets;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Type;

import static com.craivet.utils.Global.*;

public class Wood extends Item {

    public static final String NAME = "Wood";

    public Wood(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        description = "[" + stats.name + "]\nForest wood.";
        price = 5;
        this.amount = amount;
        stackable = true;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.Type.WOOD), tile, tile);
    }

}
