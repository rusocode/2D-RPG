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
		type = typeMOB;
		name = "Slime";
		direction = "down";
		speed = 1;
		maxLife = 4;
		life = maxLife;
		exp = 2;
		attack = 5;
		defense = 0;

		bodyArea.x = 3;
		bodyArea.y = 18;
		bodyArea.width = 42;
		bodyArea.height = 30;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;

		initMovementImages(Assets.slime, ENTITY_WIDTH, ENTITY_HEIGHT);
	}

	public void setAction() {
		timer.timeDirection(this, 120); // TODO Cambiar valor magico por intervalos
	}

	/**
	 * Pequeña IA en donde el Slime "huye" (cambia a la direccion actual del player) del Player cuando es atacado.
	 */
	public void damageReaction() {
		timer.directionCounter = 0;
		direction = game.player.direction;
	}

}
