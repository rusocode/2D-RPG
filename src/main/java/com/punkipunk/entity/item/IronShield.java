package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

public class IronShield extends Item {

    public static final String NAME = "Iron Shield";

    public IronShield(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        itemType = ItemType.SHIELD;
        stats.name = NAME;
        description = "[" + stats.name + "]\nA shiny iron shield.";
        price = 250;
        defenseValue = 2;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.SHIELD_IRON), tile, tile);
    }

}
