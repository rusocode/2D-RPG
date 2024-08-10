package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.TextureAssets;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;

public class Stone extends Item {

    public static final String NAME = "Stone";

    public Stone(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        description = "[" + stats.name + "]\nIt's just a stone.";
        price = 7;
        this.amount = amount;
        stackable = true;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.Type.STONE), tile, tile);
    }

}
