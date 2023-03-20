package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class ShieldWood extends Item {

	public ShieldWood(Game game) {
		super(game);
		name = "Wood Shield";
		type = TYPE_SHIELD;
		image = Utils.scaleImage(Assets.item_shield_wood, tile_size, tile_size);
		itemDescription = "[" + name + "]\nMade by wood.";
		defenseValue = 1;
		price = 25;
	}

}
