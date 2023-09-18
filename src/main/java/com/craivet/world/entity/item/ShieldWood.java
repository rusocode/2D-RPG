package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class ShieldWood extends Item {

	public static final String NAME = "Wood Shield";

	public ShieldWood(Game game, World world, int... pos) {
		super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
		stats.name = NAME;
		stats.type = Type.SHIELD;
		sheet.frame = Utils.scaleImage(shield_wood, tile, tile);
		stats.description = "[" + stats.name + "]\nMade by wood.";
		stats.price = 25;
		stats.defenseValue = 1;
	}

}
