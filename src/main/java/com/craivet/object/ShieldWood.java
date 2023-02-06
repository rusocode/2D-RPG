package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class ShieldWood extends Item {

	public ShieldWood(Game game) {
		super(game);

		type = TYPE_SHIELD;
		name = "Wood Shield";
		movementDown1 = Utils.scaleImage(Assets.shield_wood, TILE_SIZE, TILE_SIZE);

		itemDescription = "[" + name + "]\nMade by wood.";
		defenseValue = 1;
	}

}
