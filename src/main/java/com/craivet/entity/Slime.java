package com.craivet.entity;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.object.Coin;
import com.craivet.object.Rock;
import com.craivet.utils.Utils;

import java.util.Random;

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
		type = TYPE_MOB;
		name = "Slime";
		direction = "down";
		speed = 1;
		maxLife = 5;
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

		projectile = new Rock(game);

		initMovementImages(Assets.slime, ENTITY_WIDTH, ENTITY_HEIGHT);
	}

	public void setAction() {
		timer.timeDirection(this, INTERVAL_DIRECTION);
		// TODO Crear metodo
		int i = new Random().nextInt(100) + 1;
		if (i > 99 && !projectile.alive && projectileCounter == INTERVAL_PROJECTILE) {
			projectile.set(worldX, worldY, direction, true, this);
			game.projectiles.add(projectile);
			projectileCounter = 0;
		}
	}

	/**
	 * Pequeña IA en donde el Slime "huye" (cambia a la direccion actual del player) del Player cuando es atacado.
	 */
	public void damageReaction() {
		timer.directionCounter = 0;
		direction = game.player.direction;
	}

	/**
	 * Comprueba si dropeo un item.
	 */
	public void checkDrop() {
		if (Utils.azar(100) <= PROBABILIDAD_DROP_ORO) dropItem(new Coin(game));
	}

}
