package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

/**
 * Esta imagen puede ser parte de la UI o un objeto.
 */

public class Heart extends Item {

	public Heart(Game game) {
		super(game);

		name = "Heart";
		heartFull = Utils.scaleImage(Assets.heart_full, TILE_SIZE, TILE_SIZE);
		heartHalf = Utils.scaleImage(Assets.heart_half, TILE_SIZE, TILE_SIZE);
		heartBlank = Utils.scaleImage(Assets.heart_blank, TILE_SIZE, TILE_SIZE);
	}

}
