package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Key extends Item {

	public Key(Game game, World world) {
		super(game, world);
		name = "Key";
		image = Utils.scaleImage(item_key, tile_size, tile_size);
		itemDescription = "[" + name + "]\nIt opens a door.";
		price = 100;
	}

}
