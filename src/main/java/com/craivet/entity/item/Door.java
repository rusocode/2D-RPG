package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.tile.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Door extends Item {

	public Door(Game game, World world, EntityManager entityManager) {
		super(game, world, entityManager);
		name = "Door";
		image = Utils.scaleImage(item_door, tile_size, tile_size);
		collision = true;
		hitbox.x = 0;
		hitbox.y = 16;
		hitbox.width = 48;
		hitbox.height = 32;
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
	}

}
