package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class Key extends Entity {

	public Key(Game game) {
		super(game);
		name = "Key";
		movementDown1 = Utils.scaleImage(Assets.key, game.tileSize, game.tileSize);
	}

}
