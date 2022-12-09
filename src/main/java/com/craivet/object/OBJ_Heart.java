package com.craivet.object;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Heart extends SuperObject {

	Game game;

	public OBJ_Heart(Game game) {
		this.game = game;
		name = "Heart";
		image = Utils.scaleImage(Assets.heart_full, game.tileSize, game.tileSize);
		image2 = Utils.scaleImage(Assets.heart_half, game.tileSize, game.tileSize);
		image3 = Utils.scaleImage(Assets.heart_blank, game.tileSize, game.tileSize);
	}

}
