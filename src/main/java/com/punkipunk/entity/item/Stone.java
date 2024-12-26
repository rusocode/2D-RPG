package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

public class Stone extends Item {

    public static final String NAME = "Stone";

    public Stone(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        itemType = ItemType.CONSUMABLE;
        stats.name = NAME;
        description = "[" + stats.name + "]\nIt's just a stone.";
        price = 7;
        this.amount = amount;
        stackable = true;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.STONE), tile, tile);
    }

}
