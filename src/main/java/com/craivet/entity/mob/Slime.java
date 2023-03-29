package com.craivet.entity.mob;

import com.craivet.Game;
import com.craivet.entity.item.Coin;
import com.craivet.entity.projectile.StickyBall;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

/**
 * El Slime usa dos frames para todos los movimientos.
 */

public class Slime extends Mob {

	public Slime(Game game, int x, int y) {
		super(game);
		worldX = x * tile_size;
		worldY = y * tile_size;
		initDefaultValues();
	}

	private void initDefaultValues() {
		type = TYPE_MOB;
		speed = defaultSpeed = 1;
		life = maxLife = 4;
		exp = 2;

		attack = 2;
		defense = 1;

		hitbox.x = 3;
		hitbox.y = 18;
		hitbox.width = 42;
		hitbox.height = 30;
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;

		projectile = new StickyBall(game);

		initMovementImages(entity_slime, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
		mobImage = movementDown1;
	}

	public void update() {
		super.update();

		int xDistance = Math.abs(worldX - game.player.worldX);
		int yDistance = Math.abs(worldY - game.player.worldY);
		int tileDistance = (xDistance + yDistance) / tile_size;

		/* El slime se vuelve agresivo si todavia no es agresivo y si el player esta a 5 tiles de distancia y si el
		 * numero random es mayor a 50 (este ultimo, para no hacerlo robotico). */
		if (!onPath && tileDistance < 5 && Utils.azar(100) < 50) onPath = true;

		// El slime deja de ser agresivo cuando el player se aleja 15 tiles
		if (onPath && tileDistance > 15) onPath = false;
	}

	public void setAction() {
		if (onPath) {
			int goalRow = (game.player.worldY + game.player.hitbox.y) / tile_size;
			int goalCol = (game.player.worldX + game.player.hitbox.x) / tile_size;
			searchPath(goalRow, goalCol);
			shootProjectile();
		} else timer.timeDirection(this, INTERVAL_DIRECTION);
	}

	public void damageReaction() {
		timer.directionCounter = 0;
		onPath = true;
	}

	/**
	 * Comprueba si dropeo un item.
	 */
	public void checkDrop() {
		if (Utils.azar(100) <= PROBABILIDAD_DROP_ORO) dropItem(new Coin(game));
	}

	private void shootProjectile() {
		if (Utils.azar(100) > 95 && !projectile.alive && timer.projectileCounter == INTERVAL_PROJECTILE_ATTACK) {
			projectile.set(worldX + 8, worldY + 17, direction, true, this);
			for (int i = 0; i < game.projectiles[1].length; i++) {
				if (game.projectiles[game.currentMap][i] == null) {
					game.projectiles[game.currentMap][i] = projectile;
					break;
				}
			}
			timer.projectileCounter = 0;
		}
	}

}
