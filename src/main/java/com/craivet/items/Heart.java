package com.craivet.items;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

/**
 * Esta imagen puede ser parte de la UI o un objeto.
 */

public class Heart extends Item {

	public Heart(Game game) {
		super(game);
		name = "Heart";
		heartFull = Utils.scaleImage(heart_full, TILE_SIZE, TILE_SIZE);
		heartHalf = Utils.scaleImage(heart_half, TILE_SIZE, TILE_SIZE);
		heartBlank = Utils.scaleImage(heart_blank, TILE_SIZE, TILE_SIZE);
	}

}
