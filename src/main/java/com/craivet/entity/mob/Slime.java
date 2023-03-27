package com.craivet.entity.mob;

import com.craivet.Game;
import com.craivet.entity.item.Coin;
import com.craivet.entity.item.StickyBall;
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
		name = "Slime";
		speed = 1;
		maxLife = 4;
		life = maxLife;
		exp = 2;

		attack = 2;
		defense = 1;

		tileArea.x = 3;
		tileArea.y = 18;
		tileArea.width = 42;
		tileArea.height = 30;

		bodyArea.x = 3;
		bodyArea.y = 18;
		bodyArea.width = 42;
		bodyArea.height = 30;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;
		projectile = new StickyBall(game);

		initMovementImages(entity_slime, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
	}

	public void update() {
		super.update();

		int xDistance = Math.abs(worldX - game.player.worldX);
		int yDistance = Math.abs(worldY - game.player.worldY);
		int tileDistance = (xDistance + yDistance) / tile_size;

		/* El slime se vuelve agresivo si todavia no es agresivo y si el player esta a 5 tiles de distancia y si el
		 * numero random es mayor a 50 (este ultimo, para no hacerlo robotico). */
		if (!onPath && tileDistance <= 5 && Utils.azar(100) > 50) onPath = true;

		// El slime deja de ser agresivo cuando el player se aleja 20 tiles
		// if (onPath && tileDistance > 20) onPath = false;
	}

	public void setAction() {
		if (onPath) {
			int goalRow = (game.player.worldY + game.player.bodyArea.y) / tile_size;
			int goalCol = (game.player.worldX + game.player.bodyArea.x) / tile_size;
			searchPath(goalRow, goalCol);
			shootProjectile();
		} else timer.timeDirection(this, INTERVAL_DIRECTION);
	}

	/**
	 * Pequeña IA en donde el Slime "huye" (cambia a la direccion actual del player) del Player cuando es atacado.
	 */
	public void damageReaction() {
		timer.directionCounter = 0;
		// direction = game.player.direction;
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
			game.projectiles.add(projectile);
			timer.projectileCounter = 0;
		}
	}

}
