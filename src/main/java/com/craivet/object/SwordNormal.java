package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class SwordNormal extends Item {

	public SwordNormal(Game game) {
		super(game);
		name = "Normal Sword";
		type = TYPE_SWORD;
		image = Utils.scaleImage(Assets.sword_normal, TILE_SIZE, TILE_SIZE);
		attackArea.width = 36;
		attackArea.height = 36;
		itemDescription = "[" + name + "]\nAn old sword.";
		attackValue = 2;
	}

}
