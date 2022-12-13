package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Door extends Entity {

	public OBJ_Door(Game game) {
		super(game);
		name = "Door";
		down1 = Utils.scaleImage(Assets.door, game.tileSize, game.tileSize);
		collision = true;

		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

	}

}
