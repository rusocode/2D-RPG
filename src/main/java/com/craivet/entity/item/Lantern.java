package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Lantern extends Item {

    public Lantern(Game game, World world) {
        super(game, world);
        name = "Lantern";
        type = TYPE_LIGHT;
        image = Utils.scaleImage(item_lantern, tile_size, tile_size);
        description = "[" + name + "]\nIlluminaties your \nsurroundings.";
        price = 200;
        lightRadius = 250;
    }

    public Lantern(Game game, World world, int x, int y) {
        super(game, world);
        this.x = x * tile_size;
        this.y = y * tile_size;
        name = "Lantern";
        type = TYPE_LIGHT;
        image = Utils.scaleImage(item_lantern, tile_size, tile_size);
        description = "[" + name + "]\nIlluminaties your \nsurroundings.";
        price = 200;
        lightRadius = 250;
    }

}
