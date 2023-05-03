package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class SwordNormal extends Item {

	public static final String item_name = "Normal Sword";

	public SwordNormal(Game game, World world) {
		super(game, world);
		name = item_name;
		type = TYPE_SWORD;
		image = Utils.scaleImage(item_sword_normal, tile_size, tile_size);
		description = "[" + name + "]\nAn old sword.";
		price = 20;
		attackValue = 0;
		knockbackValue = 2;
	}

}
