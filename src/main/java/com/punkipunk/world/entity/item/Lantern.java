package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;

import static com.punkipunk.utils.Global.tile;

public class Lantern extends Item {

    public static final String NAME = "Lantern";

    public Lantern(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        itemType = ItemType.LIGHT;
        stats.name = NAME;
        description = "[" + stats.name + "]\nIlluminaties your \nsurroundings.";
        price = 45;
        lightRadius = 350;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.LANTERN), tile, tile);
    }

}
