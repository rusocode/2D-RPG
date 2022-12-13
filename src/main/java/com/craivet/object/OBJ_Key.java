package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class OBJ_Key extends Entity {

	public OBJ_Key(Game game) {
		super(game);
		name = "Key";
		down1 = Utils.scaleImage(Assets.key, game.tileSize, game.tileSize);
	}

}
