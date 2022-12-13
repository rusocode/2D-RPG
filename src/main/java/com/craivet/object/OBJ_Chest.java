package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Chest extends Entity {

	public OBJ_Chest(Game game) {
		super(game);
		name = "Chest";
		down1 = Utils.scaleImage(Assets.chest, game.tileSize, game.tileSize);
	}

}
