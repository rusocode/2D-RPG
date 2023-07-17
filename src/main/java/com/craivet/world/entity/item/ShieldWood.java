package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.mob.Type;
import com.craivet.util.Utils;

import static com.craivet.util.Global.*;
import static com.craivet.gfx.Assets.*;

public class ShieldWood extends Item {

	public static final String NAME = "Wood Shield";

	public ShieldWood(Game game, World world, int... pos) {
		super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
		name = NAME;
		type = Type.SHIELD;
		image = Utils.scaleImage(shield_wood, tile_size, tile_size);
		description = "[" + name + "]\nMade by wood.";
		price = 25;
		defenseValue = 1;
	}

}
