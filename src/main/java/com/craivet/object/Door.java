package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class Door extends Entity {

	public Door(Game game) {
		super(game);
		name = "Door";
		collision = true;
		movementDown1 = Utils.scaleImage(Assets.door, game.tileSize, game.tileSize);

		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

	}

}