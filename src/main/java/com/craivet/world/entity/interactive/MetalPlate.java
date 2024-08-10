package com.craivet.world.entity.interactive;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.TextureAssets;
import com.craivet.world.World;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.utils.Global.*;

public class MetalPlate extends Interactive {

    public static final String NAME = "Metal Plate";

    public MetalPlate(Game game, World world, int x, int y) {
        super(game, world, x, y);
        stats.name = NAME;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.Type.ITILE_METAL_PLATE), tile, tile);
        hitbox = new Rectangle(0, 0, 0, 0);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }


}
