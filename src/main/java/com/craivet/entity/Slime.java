package com.craivet.entity;

import com.craivet.Game;
import com.craivet.gfx.Assets;

import static com.craivet.utils.Constants.*;

/**
 * El Slime usa dos frames para todos los movimientos.
 */

public class Slime extends Entity {

	public Slime(Game game) {
		super(game);
		setDefaultValues();
	}

	private void setDefaultValues() {
		name = "Slime";
		direction = "down";
		speed = 1;
		type = 2;
		maxLife = 4;
		life = maxLife;

		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		initMovementImages(Assets.slime, ENTITY_WIDTH, ENTITY_HEIGHT);
	}

	public void setAction() {
		timer.timeActionLock(this, 120);
	}

	/**
	 * Pequeña IA en donde el Slime "huye" (cambia a la direccion actual del player) del Player cuando es atacado.
	 */
	public void damageReaction() {
		timer.actionLockCounter = 0;
		direction = game.player.direction;
	}

}
