package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.tile.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class ShieldWood extends Item {

	public ShieldWood(Game game, World world, EntityManager entityManager) {
		super(game, world, entityManager);
		name = "Wood Shield";
		type = TYPE_SHIELD;
		image = Utils.scaleImage(item_shield_wood, tile_size, tile_size);
		itemDescription = "[" + name + "]\nMade by wood.";
		price = 25;
		defenseValue = 1;
	}

}
