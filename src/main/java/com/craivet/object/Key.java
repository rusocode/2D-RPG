package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Key extends Item {

	public Key(Game game) {
		super(game);
		name = "Key";
		image = Utils.scaleImage(Assets.key, TILE_SIZE, TILE_SIZE);
		itemDescription = "[" + name + "]\nIt opens a door.";
	}

}
