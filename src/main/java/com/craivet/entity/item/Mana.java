package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Mana extends Item {

	public Mana(Game game) {
		super(game);
		name = "Mana";
		manaFull = Utils.scaleImage(mana_full, tile_size, tile_size);
		manaBlank = Utils.scaleImage(mana_blank, tile_size, tile_size);
	}

}
