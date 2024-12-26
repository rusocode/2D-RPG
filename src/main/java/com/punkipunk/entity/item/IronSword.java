package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

public class IronSword extends Item {

    public static final String NAME = "Iron Sword";

    public IronSword(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        itemType = ItemType.SWORD;
        stats.name = NAME;
        stats.knockbackValue = 2;
        description = "[" + stats.name + "]\nAn old sword.";
        price = 20;
        attackValue = 1;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.SWORD_IRON), tile, tile);
    }

}
