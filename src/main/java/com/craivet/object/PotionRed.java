package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class PotionRed extends Entity {

	Game game;
	int value = 5;

	public PotionRed(Game game) {
		super(game);

		this.game = game;

		type = typeConsumable;
		name = "Red Potion";
		movementDown1 = Utils.scaleImage(Assets.potion_red, game.tileSize, game.tileSize);
		itemDescription = "[Red Potion]\nHeals your life by " + value + ".";
	}

	public void use(Entity entity) {
		game.gameState = game.dialogueState;
		game.ui.currentDialogue = "Your drink the " + name + "!\n"
				+ "Your life has been recovered by " + value + ".";
		entity.life += value;
		if (game.player.life > game.player.maxLife) game.player.life = game.player.maxLife;
		game.playSound(Assets.power_up);
	}

}