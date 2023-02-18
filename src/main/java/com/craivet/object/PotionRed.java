package com.craivet.object;

import com.craivet.Game;
import com.craivet.Sound;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class PotionRed extends Item {

	private final Game game;

	public PotionRed(Game game) {
		super(game);

		this.game = game;

		type = TYPE_CONSUMABLE;
		name = "Red Potion";
		movementDown1 = Utils.scaleImage(Assets.potion_red, TILE_SIZE, TILE_SIZE);

		itemDescription = "[Red Potion]\nHeals your life by " + value + ".";
		value = 5;
	}

	public void use(Entity entity) {
		game.gameState = DIALOGUE_STATE;
		game.ui.currentDialogue = "Your drink the " + name + "!\n"
				+ "Your life has been recovered by " + value + ".";
		entity.life += value;
		if (game.player.life > game.player.maxLife) game.player.life = game.player.maxLife;
		game.playSE(Assets.power_up);
	}

}
