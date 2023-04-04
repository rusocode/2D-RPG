package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.tile.World;
import com.craivet.utils.Utils;

import java.util.WeakHashMap;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Chest extends Item {

	public Chest(Game game, World world, EntityManager entityManager) {
		super(game, world, entityManager);
		name = "Chest";
		image = Utils.scaleImage(item_chest, tile_size, tile_size);
	}

}
