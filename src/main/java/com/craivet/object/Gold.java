package com.craivet.object;

import com.craivet.Game;
import com.craivet.Sound;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Gold extends Item {

	private final Game game;

	public Gold(Game game) {
		super(game);
		this.game = game;

		type = TYPE_PICKUP_ONLY;
		name = "Gold";
		movementDown1 = Utils.scaleImage(Assets.gold, TILE_SIZE, TILE_SIZE);

		value = 1;

	}

	public void use(Entity entity) {
		Sound.play(Assets.power_up);
		game.ui.addMessage("Coin +" + value);
		game.player.coin += value;
	}

}
