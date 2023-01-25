package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

/**
 * Esta imagen puede ser parte de la UI o un objeto.
 */

public class Heart extends Entity {

	public Heart(Game game) {
		super(game);

		name = "Heart";
		heartFull = Utils.scaleImage(Assets.heart_full, game.tileSize, game.tileSize);
		heartHalf = Utils.scaleImage(Assets.heart_half, game.tileSize, game.tileSize);
		heartBlank = Utils.scaleImage(Assets.heart_blank, game.tileSize, game.tileSize);
	}

}
