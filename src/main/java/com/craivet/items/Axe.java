package com.craivet.items;

import com.craivet.Game;
import com.craivet.entity.Item;
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
		image = Utils.scaleImage(item_axe, TILE_SIZE, TILE_SIZE);
		attackArea.width = 30;
		attackArea.height = 30;
		itemDescription = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
		attackValue = 1;
		price = 75;
	}

	public Axe(Game game, int x, int y) {
		super(game);
		worldX = TILE_SIZE * x;
		worldY = TILE_SIZE * y;
		name = "Axe";
		type = TYPE_AXE;
		image = Utils.scaleImage(item_axe, TILE_SIZE, TILE_SIZE);
		attackArea.width = 30;
		attackArea.height = 30;
		itemDescription = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
		attackValue = 1;
	}

}
