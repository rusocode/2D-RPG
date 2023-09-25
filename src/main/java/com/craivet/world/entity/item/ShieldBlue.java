package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class ShieldBlue extends Item {

	public static final String NAME = "Blue Shield";

	public ShieldBlue(Game game, World world, int... pos) {
		super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
		type = Type.SHIELD;
		stats.name = NAME;
		description = "[" + stats.name + "]\nA shiny blue shield.";
		price = 250;
		defenseValue = 2;
		sheet.frame = Utils.scaleImage(shield_blue, tile, tile);
	}

}
