package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.TextureAssets;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;

public class Axe extends Item {

    public static final String NAME = "Axe";

    public Axe(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.AXE;
        stats.name = NAME;
        stats.knockbackValue = 8;
        description = "[" + stats.name + "]\nA bit rusty but still \ncan cut some trees.";
        price = 75;
        attackValue = 1;
        // Aplicando el uso de la fachada
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.AXE), tile, tile);
    }

}
