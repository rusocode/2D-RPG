package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class Mana extends Entity {

	public Mana(Game game) {
		super(game);

		name = "Mana";
		manaFull = Utils.scaleImage(Assets.mana_full, game.tileSize, game.tileSize);
		manaBlank = Utils.scaleImage(Assets.mana_blank, game.tileSize, game.tileSize);

	}

}
