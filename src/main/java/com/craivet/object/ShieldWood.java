package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class ShieldWood extends Entity {

	public ShieldWood(Game game) {
		super(game);

		type = TYPE_SHIELD;
		name = "Wood Shield";
		movementDown1 = Utils.scaleImage(Assets.shield_wood, game.tileSize, game.tileSize);

		itemDescription = "[" + name + "]\nMade by wood.";
		defenseValue = 1;
	}

}
