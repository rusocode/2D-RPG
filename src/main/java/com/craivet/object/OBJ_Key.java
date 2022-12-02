package com.craivet.object;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Key extends SuperObject {

	Game game;

	public OBJ_Key(Game game) {
		this.game = game;
		name = "Key";
		image = Assets.key;
		Utils.scaleImage(image, game.tileSize, game.tileSize);
	}

}
