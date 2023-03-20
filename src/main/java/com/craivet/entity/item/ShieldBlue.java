package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class ShieldBlue extends Item {

	public ShieldBlue(Game game) {
		super(game);
		name = "Blue Shield";
		type = TYPE_SHIELD;
		image = Utils.scaleImage(Assets.item_shield_blue, tile_size, tile_size);
		itemDescription = "[" + name + "]\nA shiny blue shield.";
		defenseValue = 2;
		price = 250;
	}

}
