package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.util.Utils;

import static com.craivet.util.Global.*;
import static com.craivet.gfx.Assets.*;

public class SwordNormal extends Item {

	public static final String NAME = "Normal Sword";

	public SwordNormal(Game game, World world, int... pos) {
		super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
		name = NAME;
		type = Type.SWORD;
		image = Utils.scaleImage(sword_normal, tile_size, tile_size);
		description = "[" + name + "]\nAn old sword.";
		price = 20;
		attackValue = 1;
		knockbackValue = 2;
	}

}
