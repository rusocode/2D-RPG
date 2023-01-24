package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class SwordNormal extends Entity {

	public SwordNormal(Game game) {
		super(game);

		type = typeSword;
		name = "Normal Sword";
		movementDown1 = Utils.scaleImage(Assets.sword_normal, game.tileSize, game.tileSize);
		attackValue = 1;
		itemDescription = "[" + name + "]\nAn old sword.";
	}

}
