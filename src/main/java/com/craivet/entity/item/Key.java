package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.tile.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Key extends Item {

	public Key(Game game, World world, EntityManager entityManager) {
		super(game, world, entityManager);
		name = "Key";
		image = Utils.scaleImage(item_key, tile_size, tile_size);
		itemDescription = "[" + name + "]\nIt opens a door.";
		price = 100;
	}

}
