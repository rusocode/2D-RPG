package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Door extends Item {

	public Door(Game game) {
		super(game);
		name = "Door";
		image = Utils.scaleImage(item_door, tile_size, tile_size);
		collision = true;
		bodyArea.x = 0;
		bodyArea.y = 16;
		bodyArea.width = 48;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;
	}

}
