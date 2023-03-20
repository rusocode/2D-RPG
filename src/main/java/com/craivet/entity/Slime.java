package com.craivet.entity;

import com.craivet.Game;
import com.craivet.items.Coin;
import com.craivet.items.Rock;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

/**
 * El Slime usa dos frames para todos los movimientos.
 */

public class Slime extends Mob {

	public Slime(Game game, int x, int y) {
		super(game);
		worldX = x * TILE_SIZE;
		worldY = y * TILE_SIZE;
		initDefaultValues();
	}

	private void initDefaultValues() {
		type = TYPE_MOB;
		name = "Slime";
		speed = 1;
		maxLife = 4;
		life = maxLife;
		exp = 2;

		attack = 2;
		defense = 0;

		bodyArea.x = 3;
		bodyArea.y = 18;
		bodyArea.width = 42;
		bodyArea.height = 30;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;

		projectile = new Rock(game);

		initMovementImages(entity_slime, ENTITY_WIDTH, ENTITY_HEIGHT);
	}

	public void setAction() {
		timer.timeDirection(this, INTERVAL_DIRECTION);
		shootProjectile();
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

	private void shootProjectile() {
		if (Utils.azar(100) > 99 && !projectile.alive && timer.projectileCounter == INTERVAL_PROJECTILE_ATTACK) {
			projectile.set(worldX, worldY, direction, true, this);
			game.projectiles.add(projectile);
			timer.projectileCounter = 0;
		}
	}

}
