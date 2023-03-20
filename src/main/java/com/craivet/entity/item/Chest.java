package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Chest extends Item {

	public Chest(Game game) {
		super(game);
		name = "Chest";
		image = Utils.scaleImage(item_chest, tile_size, tile_size);
	}

}
