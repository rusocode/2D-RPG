package com.craivet.object;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Door extends SuperObject {

	Game game;

	public OBJ_Door (Game game) {
		this.game = game;
		name = "Door";
		image = Assets.door;
		collision = true;
		Utils.scaleImage(image, game.tileSize, game.tileSize);
	}

}
