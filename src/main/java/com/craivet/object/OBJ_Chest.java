package com.craivet.object;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Chest extends SuperObject {

	Game game;

	public OBJ_Chest(Game game) {
		this.game = game;
		name = "Chest";
		image = Assets.chest;
		Utils.scaleImage(image, game.tileSize, game.tileSize);
	}

}
