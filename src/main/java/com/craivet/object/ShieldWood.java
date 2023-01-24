package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class ShieldWood extends Entity {

	public ShieldWood(Game game) {
		super(game);

		type = typeShield;
		name = "Wood Shield";
		movementDown1 = Utils.scaleImage(Assets.shield_wood, game.tileSize, game.tileSize);
		defenseValue = 1;
		itemDescription = "[" + name + "]\nMade by wood.";
	}

}
