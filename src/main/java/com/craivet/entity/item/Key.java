package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Key extends Item {
	
	public Key(Game game, World world) {
		super(game, world);
		name = "Key";
		type = TYPE_CONSUMABLE;
		image = Utils.scaleImage(item_key, tile_size, tile_size);
		itemDescription = "[" + name + "]\nIt opens a door.";
		price = 100;
	}
	
	public boolean use(Entity entity) {
		// Si detecta una puerta, entonces puede usar la llave
		int itemIndex = getDetected(entity, world.items, "Door");
		if (itemIndex != -1) {
			game.playSound(sound_door_opening);
			world.items[world.map][itemIndex] = null;
			return true;
		}
		return false;
	}
	
}
