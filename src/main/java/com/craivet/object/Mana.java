package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Mana extends Item {

	public Mana(Game game) {
		super(game);

		name = "Mana";
		manaFull = Utils.scaleImage(Assets.mana_full, TILE_SIZE, TILE_SIZE);
		manaBlank = Utils.scaleImage(Assets.mana_blank, TILE_SIZE, TILE_SIZE);

	}

}
