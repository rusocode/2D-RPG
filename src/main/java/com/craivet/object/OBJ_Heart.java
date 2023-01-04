package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Heart extends Entity {

	public OBJ_Heart(Game game) {
		super(game);
		name = "Heart";
		image1 = Utils.scaleImage(Assets.heart_full, game.tileSize, game.tileSize);
		image2 = Utils.scaleImage(Assets.heart_half, game.tileSize, game.tileSize);
		image3 = Utils.scaleImage(Assets.heart_blank, game.tileSize, game.tileSize);
	}

}
