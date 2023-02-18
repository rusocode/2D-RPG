package com.craivet.object;

import com.craivet.Game;
import com.craivet.Sound;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Coin extends Item {

	private final Game game;

	public Coin(Game game) {
		super(game);
		this.game = game;

		type = TYPE_PICKUP_ONLY;
		name = "Coin";
		movementDown1 = Utils.scaleImage(Assets.obj_coin, 32, 32);

		value = 1;

	}

	public void use(Entity entity) {
		game.playSE(Assets.coin);
		game.ui.addMessage("Coin +" + value);
		game.player.coin += value;
	}

}
