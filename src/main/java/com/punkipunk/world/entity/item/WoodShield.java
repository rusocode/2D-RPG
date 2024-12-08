package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

public class WoodShield extends Item {

    public static final String NAME = "Wood Shield";

    public WoodShield(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        itemType = ItemType.SHIELD;
        stats.name = NAME;
        description = "[" + stats.name + "]\nMade by wood.";
        price = 150;
        defenseValue = 1;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.SHIELD_WOOD), tile, tile);
    }

}
