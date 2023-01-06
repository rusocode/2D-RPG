package com.craivet.entity;

import com.craivet.Game;
import com.craivet.gfx.Assets;

import java.util.Random;

import static com.craivet.utils.Constants.ENTITY_HEIGHT;
import static com.craivet.utils.Constants.ENTITY_WIDTH;

public class Slime extends Entity {

	public Slime(Game game) {
		super(game);
		setDefaultValues();
	}

	private void setDefaultValues() {
		name = "Slime";
		type = 2;
		speed = 1;
		maxLife = 4;
		life = maxLife;

		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		initMovementImages(Assets.slime, ENTITY_WIDTH, ENTITY_HEIGHT); // Usa dos imagenes para todas las direcciones

	}

	public void setAction() {
		actionLockCounter++;
		if (actionLockCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100) + 1;
			if (i <= 25) direction = "down";
			if (i > 25 && i <= 50) direction = "up";
			if (i > 50 && i <= 75) direction = "left";
			if (i > 75 && i <= 100) direction = "right";
			actionLockCounter = 0;
		}
	}

}
