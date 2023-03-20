package com.craivet.items;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Coin extends Item {

	private final Game game;

	public Coin(Game game) {
		super(game);
		this.game = game;
		name = "Coin";
		type = TYPE_PICKUP_ONLY;
		image = Utils.scaleImage(item_coin, 32, 32);
		value = 1;
	}

	public void use(Entity entity) {
		game.playSound(sound_coin);
		game.ui.addMessage("Coin +" + value);
		game.player.coin += value;
	}

}
