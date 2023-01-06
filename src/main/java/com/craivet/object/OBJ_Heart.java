package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Heart extends Entity {

	public OBJ_Heart(Game game) {
		super(game);
		name = "Heart";
		heart_full = Utils.scaleImage(Assets.heart_full, game.tileSize, game.tileSize);
		heart_half = Utils.scaleImage(Assets.heart_half, game.tileSize, game.tileSize);
		heart_blank = Utils.scaleImage(Assets.heart_blank, game.tileSize, game.tileSize);
	}

}
