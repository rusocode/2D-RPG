package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Boots extends Item {

	public Boots(Game game) {
		super(game);

		name = "Boots";
		movementDown1 = Utils.scaleImage(Assets.boots, TILE_SIZE, TILE_SIZE);
	}

}
