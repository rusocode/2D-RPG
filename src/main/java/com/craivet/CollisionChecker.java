package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.Player;

import static com.craivet.utils.Constants.*;

/**
 * Fisica y deteccion de colisiones.
 *
 * <p>TODO Hay un bug con la colision de objetos (key) en la posicion 25, 23
 */

public class CollisionChecker {

	private final Game game;

	public CollisionChecker(Game game) {
		this.game = game;
	}

	/**
	 * Verifica si la entidad colisiona con el tile.
	 *
	 * @param entity la entidad.
	 */
	public void checkTile(Entity entity) {

		int entityBottomWorldY = entity.worldY + entity.bodyArea.y + entity.bodyArea.height;
		int entityTopWorldY = entity.worldY + entity.bodyArea.y;
		int entityLeftWorldX = entity.worldX + entity.bodyArea.x;
		int entityRightWorldX = entity.worldX + entity.bodyArea.x + entity.bodyArea.width;

		int entityBottomRow = entityBottomWorldY / TILE_SIZE;
		int entityTopRow = entityTopWorldY / TILE_SIZE;
		int entityLeftCol = entityLeftWorldX / TILE_SIZE;
		int entityRightCol = entityRightWorldX / TILE_SIZE;

		// En caso de que la entidad colisione en el medio de dos tiles
		int tileNum1, tileNum2;

		switch (entity.direction) {
			case "down":
				entityBottomRow = (entityBottomWorldY + entity.speed) / TILE_SIZE;
				tileNum1 = game.tileManager.map[entityLeftCol][entityBottomRow];
				tileNum2 = game.tileManager.map[entityRightCol][entityBottomRow];
				if (game.tileManager.tile[tileNum1].collision || game.tileManager.tile[tileNum2].collision)
					entity.collisionOn = true;
				break;
			case "up":
				entityTopRow = (entityTopWorldY - entity.speed) / TILE_SIZE;
				tileNum1 = game.tileManager.map[entityLeftCol][entityTopRow];
				tileNum2 = game.tileManager.map[entityRightCol][entityTopRow];
				if (game.tileManager.tile[tileNum1].collision || game.tileManager.tile[tileNum2].collision)
					entity.collisionOn = true;
				break;
			case "left":
				entityLeftCol = (entityLeftWorldX - entity.speed) / TILE_SIZE;
				tileNum1 = game.tileManager.map[entityLeftCol][entityTopRow];
				tileNum2 = game.tileManager.map[entityLeftCol][entityBottomRow];
				if (game.tileManager.tile[tileNum1].collision || game.tileManager.tile[tileNum2].collision)
					entity.collisionOn = true;
				break;
			case "right":
				entityRightCol = (entityRightWorldX + entity.speed) / TILE_SIZE;
				tileNum1 = game.tileManager.map[entityRightCol][entityTopRow];
				tileNum2 = game.tileManager.map[entityRightCol][entityBottomRow];
				if (game.tileManager.tile[tileNum1].collision || game.tileManager.tile[tileNum2].collision)
					entity.collisionOn = true;
				break;
		}

	}

	/**
	 * Verifica si la entidad colisiona con el objeto.
	 *
	 * @param entity la entidad.
	 * @return el indice del objeto en caso de que el player colisione con este.
	 */
	public int checkObject(Entity entity) {
		int index = -1;
		for (int i = 0; i < game.objs.length; i++) {
			if (game.objs[i] != null) {
				// Obtiene la posicion del area del cuerpo de la entidad y del objeto
				entity.bodyArea.x += entity.worldX;
				entity.bodyArea.y += entity.worldY;
				game.objs[i].bodyArea.x += game.objs[i].worldX;
				game.objs[i].bodyArea.y += game.objs[i].worldY;
				switch (entity.direction) {
					case "down":
						entity.bodyArea.y += entity.speed;
						break;
					case "up":
						entity.bodyArea.y -= entity.speed;
						break;
					case "left":
						entity.bodyArea.x -= entity.speed;
						break;
					case "right":
						entity.bodyArea.x += entity.speed;
						break;
				}

				if (entity.bodyArea.intersects(game.objs[i].bodyArea)) {
					if (game.objs[i].collision) entity.collisionOn = true;
					if (entity instanceof Player) index = i;
				}

				entity.bodyArea.x = entity.bodyAreaDefaultX;
				entity.bodyArea.y = entity.bodyAreaDefaultY;
				game.objs[i].bodyArea.x = game.objs[i].bodyAreaDefaultX;
				game.objs[i].bodyArea.y = game.objs[i].bodyAreaDefaultY;
			}
		}
		return index;
	}

	/**
	 * Verifica la colision entre entidades.
	 *
	 * @param entity      la entidad.
	 * @param otherEntity la otra entidad.
	 * @return el indice de la otra entidad en caso de que la entidad colisione con esta.
	 */
	public int checkEntity(Entity entity, Entity[] otherEntity) {
		int index = -1;
		for (int i = 0; i < otherEntity.length; i++) {
			if (otherEntity[i] != null) {
				// Obtiene la posicion del area del cuerpo de la entidad y de la otra entidad
				entity.bodyArea.x += entity.worldX;
				entity.bodyArea.y += entity.worldY;
				otherEntity[i].bodyArea.x += otherEntity[i].worldX;
				otherEntity[i].bodyArea.y += otherEntity[i].worldY;
				switch (entity.direction) {
					case "down":
						entity.bodyArea.y += entity.speed;
						break;
					case "up":
						entity.bodyArea.y -= entity.speed;
						break;
					case "left":
						entity.bodyArea.x -= entity.speed;
						break;
					case "right":
						entity.bodyArea.x += entity.speed;
						break;
				}

				if (entity.bodyArea.intersects(otherEntity[i].bodyArea)) {
					if (otherEntity[i] != entity) { // Evita la colision en si misma
						entity.collisionOn = true;
						index = i;
					}
				}

				entity.bodyArea.x = entity.bodyAreaDefaultX;
				entity.bodyArea.y = entity.bodyAreaDefaultY;
				otherEntity[i].bodyArea.x = otherEntity[i].bodyAreaDefaultX;
				otherEntity[i].bodyArea.y = otherEntity[i].bodyAreaDefaultY;
			}
		}
		return index;
	}

	/**
	 * Verifica si la entidad colisiona con el player.
	 *
	 * <p>TODO Se puede fucionar con el metodo checkEntity()?
	 *
	 * @param entity la entidad.
	 * @return true si la entidad colisiona con el player.
	 */
	public boolean checkPlayer(Entity entity) {
		boolean contact = false;
		// Obtiene la posicion del area solida de la entidad y del player
		entity.bodyArea.x += entity.worldX;
		entity.bodyArea.y += entity.worldY;
		game.player.bodyArea.x += game.player.worldX;
		game.player.bodyArea.y += game.player.worldY;
		switch (entity.direction) {
			case "down":
				entity.bodyArea.y += entity.speed;
				break;
			case "up":
				entity.bodyArea.y -= entity.speed;
				break;
			case "left":
				entity.bodyArea.x -= entity.speed;
				break;
			case "right":
				entity.bodyArea.x += entity.speed;
				break;
		}

		if (entity.bodyArea.intersects(game.player.bodyArea)) {
			entity.collisionOn = true;
			contact = true;
		}

		entity.bodyArea.x = entity.bodyAreaDefaultX;
		entity.bodyArea.y = entity.bodyAreaDefaultY;
		game.player.bodyArea.x = game.player.bodyAreaDefaultX;
		game.player.bodyArea.y = game.player.bodyAreaDefaultY;

		return contact;

	}

}
