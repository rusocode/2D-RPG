package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

public class IronPickaxe extends Item {

    public static final String NAME = "Iron Pickaxe";

    public IronPickaxe(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        itemType = ItemType.PICKAXE;
        stats.name = NAME;
        stats.knockbackValue = 8;
        description = "[" + stats.name + "]\nYou will big it!";
        price = 60;
        attackValue = 1;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.PICKAXE), tile, tile);
    }

}
