package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Boots extends Entity {

	public OBJ_Boots(Game game) {
		super(game);
		name = "Boots";
		down1 = Utils.scaleImage(Assets.boots, game.tileSize, game.tileSize);
	}

}
