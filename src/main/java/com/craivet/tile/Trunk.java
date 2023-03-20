package com.craivet.tile;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Trunk extends InteractiveTile {

	public Trunk(Game game, int x, int y) {
		super(game);
		this.worldX = TILE_SIZE * x;
		this.worldY = TILE_SIZE * y;
		image = Utils.scaleImage(Assets.itile_trunk, TILE_SIZE, TILE_SIZE);
		// Resetea a 0 ya que no se podria caminar sobre el tronco
		bodyArea.x = 0;
		bodyArea.y = 0;
		bodyArea.width = 0;
		bodyArea.height = 0;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;

	}

}
