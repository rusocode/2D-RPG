package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class ShieldBlue extends Item {

	public ShieldBlue(Game game) {
		super(game);
		name = "Blue Shield";
		type = TYPE_SHIELD;
		image = Utils.scaleImage(item_shield_blue, tile_size, tile_size);
		itemDescription = "[" + name + "]\nA shiny blue shield.";
		defenseValue = 2;
		price = 250;
	}

}
