package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Door extends Item {

	public Door(Game game, World world, int x, int y) {
		super(game, world);
		worldX = x * tile_size;
		worldY = y * tile_size;
		name = "Door";
		type = TYPE_OBSTACLE;
		image = Utils.scaleImage(item_door, tile_size, tile_size);
		solid = true;
		hitbox.x = 0;
		hitbox.y = 16;
		hitbox.width = 48;
		hitbox.height = 32;
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
	}
	
	public void interact() {
		game.state = DIALOGUE_STATE;
		game.ui.currentDialogue = "You need a key to open this";
	}

}
