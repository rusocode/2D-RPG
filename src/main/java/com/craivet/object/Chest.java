package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class Chest extends Item {

	public Chest(Game game) {
		super(game);

		name = "Chest";
		movementDown1 = Utils.scaleImage(Assets.chest, game.tileSize, game.tileSize);
	}

}
