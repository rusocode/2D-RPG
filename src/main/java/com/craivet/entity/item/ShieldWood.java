package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class ShieldWood extends Item {

	public static final String NAME = "Wood Shield";

	public ShieldWood(Game game, World world, int... pos) {
		super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
		name = NAME;
		type = TYPE_SHIELD;
		image = Utils.scaleImage(item_shield_wood, tile_size, tile_size);
		description = "[" + name + "]\nMade by wood.";
		price = 25;
		defenseValue = 1;
	}

}
