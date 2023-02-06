package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Axe extends Item {

	public Axe(Game game) {
		super(game);

		type = TYPE_AXE;
		name = "Woodcutter's Axe";
		movementDown1 = Utils.scaleImage(Assets.axe, TILE_SIZE, TILE_SIZE);

		attackArea.width = 30;
		attackArea.height = 30;

		itemDescription = "[Woodcutter's Axe]\nA bit rusty but still \ncan cut some trees.";
		attackValue = 2;

	}
}
