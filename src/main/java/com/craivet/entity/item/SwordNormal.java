package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class SwordNormal extends Item {

	public SwordNormal(Game game) {
		super(game);
		name = "Normal Sword";
		type = TYPE_SWORD;
		image = Utils.scaleImage(item_sword_normal, tile_size, tile_size);
		attackArea.width = 36;
		attackArea.height = 36;
		itemDescription = "[" + name + "]\nAn old sword.";
		attackValue = 0;
		price = 20;
	}

}
