package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class Boots extends Entity {

	public Boots(Game game) {
		super(game);
		name = "Boots";
		movementDown1 = Utils.scaleImage(Assets.boots, game.tileSize, game.tileSize);
	}

}
