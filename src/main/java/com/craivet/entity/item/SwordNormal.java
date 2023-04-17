package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class SwordNormal extends Item {

	public SwordNormal(Game game, World world) {
		super(game, world);
		name = "Normal Sword";
		type = TYPE_SWORD;
		image = Utils.scaleImage(item_sword_normal, tile_size, tile_size);
		itemDescription = "[" + name + "]\nAn old sword.";
		price = 20;
		attackValue = 2;
		knockBackPower = 2;
	}

}
