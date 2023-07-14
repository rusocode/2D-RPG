package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.util.Utils;

import static com.craivet.util.Global.*;
import static com.craivet.gfx.Assets.*;

public class Boots extends Item {

	public static final String NAME = "Boots";

	public Boots(Game game, World world, int... pos) {
		super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
		name = NAME;
		image = Utils.scaleImage(boots, tile_size, tile_size);
	}

}
