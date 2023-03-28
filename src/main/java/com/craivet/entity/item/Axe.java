package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Axe extends Item {

	/**
	 * Constructor solo para agregar Axe al inventario.
	 */
	public Axe(Game game) {
		super(game);
		name = "Axe";
		type = TYPE_AXE;
		image = Utils.scaleImage(item_axe, tile_size, tile_size);
		attackbox.width = 30;
		attackbox.height = 30;
		itemDescription = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
		attackValue = 1;
		price = 75;
	}

	public Axe(Game game, int x, int y) {
		super(game);
		worldX = x * tile_size;
		worldY = y * tile_size;
		name = "Axe";
		type = TYPE_AXE;
		image = Utils.scaleImage(item_axe, tile_size, tile_size);
		attackbox.width = 30;
		attackbox.height = 30;
		itemDescription = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
		attackValue = 1;
		price = 75;
	}

}
