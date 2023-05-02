package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Stone extends Item {

    public static final String item_name = "Stone";

    public Stone(Game game, World world) {
        super(game, world);
        name = item_name;
        type = TYPE_CONSUMABLE;
        image = Utils.scaleImage(item_stone, tile_size, tile_size);
        description = "[" + name + "]\nIt's just a stone.";
        stackable = true;
    }

}
