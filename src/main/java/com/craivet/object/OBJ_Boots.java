package com.craivet.object;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Boots extends SuperObject {

	Game game;

	public OBJ_Boots(Game game) {
		this.game = game;
		name = "Boots";
		image = Assets.boots;
		Utils.scaleImage(image, game.tileSize, game.tileSize);
	}

}
