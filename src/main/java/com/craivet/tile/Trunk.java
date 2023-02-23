package com.craivet.tile;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Trunk extends InteractiveTile {

	public Trunk(Game game, int col, int row) {
		super(game);

		this.worldX = TILE_SIZE * col;
		this.worldY = TILE_SIZE * row;

		movementDown1 = Utils.scaleImage(Assets.trunk, TILE_SIZE, TILE_SIZE);

		// Resetea a 0 ya que no se podria caminar sobre el tronco
		bodyArea.x = 0;
		bodyArea.y = 0;
		bodyArea.width = 0;
		bodyArea.height = 0;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;

	}

}
