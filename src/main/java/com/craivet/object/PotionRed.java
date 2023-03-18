package com.craivet.object;

import com.craivet.Game;
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
		name = "Red Potion";
		type = TYPE_CONSUMABLE;
		image = Utils.scaleImage(Assets.potion_red, TILE_SIZE, TILE_SIZE);
		value = 5;
		itemDescription = "[" + name + "]\nHeals your life by " + value + ".";
		price = 25;
	}

	public void use(Entity entity) {
		game.gameState = DIALOGUE_STATE;
		game.ui.currentDialogue = "Your drink the " + name + "!\n"
				+ "Your life has been recovered by " + value + ".";
		entity.life += value;
		if (game.player.life > game.player.maxLife) game.player.life = game.player.maxLife;
		game.playSound(Assets.potion);
	}

}
