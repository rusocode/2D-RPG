package com.craivet.items;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Mana extends Item {

	public Mana(Game game) {
		super(game);
		name = "Mana";
		manaFull = Utils.scaleImage(mana_full, TILE_SIZE, TILE_SIZE);
		manaBlank = Utils.scaleImage(mana_blank, TILE_SIZE, TILE_SIZE);
	}

}
