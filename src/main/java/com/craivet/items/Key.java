package com.craivet.items;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Key extends Item {

	public Key(Game game) {
		super(game);
		name = "Key";
		image = Utils.scaleImage(item_key, TILE_SIZE, TILE_SIZE);
		itemDescription = "[" + name + "]\nIt opens a door.";
		price = 100;
	}

}
