package com.craivet.tile;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Trunk extends Interactive {

	public Trunk(Game game, World world, int x, int y) {
		super(game, world);
		this.worldX = tile_size * x;
		this.worldY = tile_size * y;
		image = Utils.scaleImage(Assets.itile_trunk, tile_size, tile_size);
		// Resetea a 0 ya que no se podria caminar sobre el tronco
		hitbox.x = 0;
		hitbox.y = 0;
		hitbox.width = 0;
		hitbox.height = 0;
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;

	}

}
