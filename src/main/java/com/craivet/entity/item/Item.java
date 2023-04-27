package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;

/**
 * Crear un texture atlas con todos los items.
 */

public class Item extends Entity {

	protected int value;

	public Item(Game game, World world) {
		super(game, world);
	}

	public Item(Game game, World world, int x, int y) {
		super(game, world, x, y);
	}

}
