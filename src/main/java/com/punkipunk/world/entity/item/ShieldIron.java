package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;

import static com.punkipunk.utils.Global.tile;

public class ShieldIron extends Item {

    public static final String NAME = "Iron Shield";

    public ShieldIron(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.SHIELD;
        stats.name = NAME;
        description = "[" + stats.name + "]\nA shiny iron shield.";
        price = 250;
        defenseValue = 2;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.SHIELD_IRON), tile, tile);
    }

}
