package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class SwordNormal extends Item {

	public SwordNormal(EntityManager game) {
		super(game);
		name = "Normal Sword";
		type = TYPE_SWORD;
		image = Utils.scaleImage(item_sword_normal, tile_size, tile_size);
		itemDescription = "[" + name + "]\nAn old sword.";
		price = 20;
		attackValue = 2;
		knockBackPower = 2;
	}

}
