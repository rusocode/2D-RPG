package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;

import static com.punkipunk.utils.Global.tile;

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
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.WOOD), tile, tile);
    }

}