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
        name = item_name;
        image = Utils.scaleImage(itile_metalplate, tile, tile);
        hitbox.x = 0;
        hitbox.y = 0;
        hitbox.width = 0;
        hitbox.height = 0;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }


}
