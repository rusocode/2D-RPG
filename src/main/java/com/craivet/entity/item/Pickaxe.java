package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.item_pickaxe;
import static com.craivet.utils.Global.*;

public class Pickaxe extends Entity {

    public static final String item_name = "Pickaxe";

    public Pickaxe(Game game, World world) {
        super(game, world);
        name = item_name;
        type = TYPE_PICKAXE;
        image = Utils.scaleImage(item_pickaxe, tile_size, tile_size);
        attackbox.width = 30;
        attackbox.height = 30;
        description = "[" + name + "]\nYou will big it!";
        price = 75;
        attackValue = 1;
        knockbackValue = 8;
    }

}