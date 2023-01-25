package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class ShieldBlue extends Entity {

	public ShieldBlue(Game game) {
		super(game);

		type = typeShield;
		name = "Blue Shield";
		movementDown1 = Utils.scaleImage(Assets.shield_blue, game.tileSize, game.tileSize);
		itemDescription = "[" + name + "]\nA shiny blue shield.";
		defenseValue = 2;
	}

}
