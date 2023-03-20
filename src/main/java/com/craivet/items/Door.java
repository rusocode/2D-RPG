package com.craivet.items;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Door extends Item {

	public Door(Game game) {
		super(game);
		name = "Door";
		image = Utils.scaleImage(item_door, TILE_SIZE, TILE_SIZE);
		collision = true;
		bodyArea.x = 0;
		bodyArea.y = 16;
		bodyArea.width = 48;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;
	}

}
