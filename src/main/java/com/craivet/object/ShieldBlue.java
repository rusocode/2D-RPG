package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class ShieldBlue extends Item {

	public ShieldBlue(Game game) {
		super(game);

		type = TYPE_SHIELD;
		name = "Blue Shield";
		movementDown1 = Utils.scaleImage(Assets.shield_blue, TILE_SIZE, TILE_SIZE);

		itemDescription = "[" + name + "]\nA shiny blue shield.";
		defenseValue = 2;
	}

}
