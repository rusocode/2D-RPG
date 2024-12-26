package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

public class IronAxe extends Item {

    public static final String NAME = "Iron Axe";

    public IronAxe(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        itemType = ItemType.AXE;
        stats.name = NAME;
        stats.knockbackValue = 8;
        description = "[" + stats.name + "]\nA bit rusty but still \ncan cut some trees.";
        price = 75;
        attackValue = 1;
        // Aplicando el uso de la fachada
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.AXE), tile, tile);
    }

}
