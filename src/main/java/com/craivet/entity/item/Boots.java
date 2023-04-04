package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.tile.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Boots extends Item {

	public Boots(Game game, World world, EntityManager entityManager) {
		super(game, world, entityManager);
		name = "Boots";
		image = Utils.scaleImage(item_boots, tile_size, tile_size);
	}

}
