package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class SwordNormal extends Item {

	public SwordNormal(Game game) {
		super(game);
		name = "Normal Sword";
		type = TYPE_SWORD;
		image = Utils.scaleImage(Assets.item_sword_normal, tile_size, tile_size);
		attackArea.width = 36;
		attackArea.height = 36;
		itemDescription = "[" + name + "]\nAn old sword.";
		attackValue = 2;
		price = 20;
	}

}
