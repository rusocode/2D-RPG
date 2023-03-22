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
		/* Especificar el ancho y alto del area de ataque desde aca, no es una buena forma, ya que el tamaño puede
		 * variar dependiendo de la direccion hacia donde se ataque. */
		// attackArea.width = 36;
		// attackArea.height = 36;
		itemDescription = "[" + name + "]\nAn old sword.";
		attackValue = 2;
		price = 20;
	}

}
