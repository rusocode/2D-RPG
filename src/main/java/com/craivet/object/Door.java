package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class Door extends Item {

	public Door(Game game) {
		super(game);

		name = "Door";
		movementDown1 = Utils.scaleImage(Assets.door, game.tileSize, game.tileSize);
		collision = true;

		bodyArea.x = 0;
		bodyArea.y = 16;
		bodyArea.width = 48;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;

	}

}
