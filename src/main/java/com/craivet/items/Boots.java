package com.craivet.items;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Boots extends Item {

	public Boots(Game game) {
		super(game);
		name = "Boots";
		image = Utils.scaleImage(item_boots, TILE_SIZE, TILE_SIZE);
	}

}
