package com.craivet.world.tile;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class MetalPlate extends Interactive {

    public static final String item_name = "Metal Plate";

    public MetalPlate(Game game, World world, int x, int y) {
        super(game, world, x, y);
        stats.name = item_name;
        sheet.frame = Utils.scaleImage(itile_metalplate, tile, tile);
        stats.hitbox.x = 0;
        stats.hitbox.y = 0;
        stats.hitbox.width = 0;
        stats.hitbox.height = 0;
        stats.hitboxDefaultX = stats.hitbox.x;
        stats.hitboxDefaultY = stats.hitbox.y;
    }


}
