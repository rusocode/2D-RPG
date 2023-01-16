package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.Player;

/**
 * Fisica y deteccion de colisiones.
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

		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;

		int entityBottomRow = entityBottomWorldY / game.tileSize;
		int entityTopRow = entityTopWorldY / game.tileSize;
		int entityLeftCol = entityLeftWorldX / game.tileSize;
		int entityRightCol = entityRightWorldX / game.tileSize;

		// En caso de que la entidad colisione en el medio de dos tiles
		int tileNum1, tileNum2;

		switch (entity.direction) {
			case "down":
				entityBottomRow = (entityBottomWorldY + entity.speed) / game.tileSize;
				tileNum1 = game.tileManager.map[entityLeftCol][entityBottomRow];
				tileNum2 = game.tileManager.map[entityRightCol][entityBottomRow];
				if (game.tileManager.tile[tileNum1].collision || game.tileManager.tile[tileNum2].collision)
					entity.collisionOn = true;
				break;
			case "up":
				entityTopRow = (entityTopWorldY - entity.speed) / game.tileSize;
				tileNum1 = game.tileManager.map[entityLeftCol][entityTopRow];
				tileNum2 = game.tileManager.map[entityRightCol][entityTopRow];
				if (game.tileManager.tile[tileNum1].collision || game.tileManager.tile[tileNum2].collision)
					entity.collisionOn = true;
				break;
			case "left":
				entityLeftCol = (entityLeftWorldX - entity.speed) / game.tileSize;
				tileNum1 = game.tileManager.map[entityLeftCol][entityTopRow];
				tileNum2 = game.tileManager.map[entityLeftCol][entityBottomRow];
				if (game.tileManager.tile[tileNum1].collision || game.tileManager.tile[tileNum2].collision)
					entity.collisionOn = true;
				break;
			case "right":
				entityRightCol = (entityRightWorldX + entity.speed) / game.tileSize;
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
				// Obtiene la posicion del area solida de la entidad y del objeto
				entity.solidArea.x += entity.worldX;
				entity.solidArea.y += entity.worldY;
				game.objs[i].solidArea.x += game.objs[i].worldX;
				game.objs[i].solidArea.y += game.objs[i].worldY;
				switch (entity.direction) {
					case "down":
						entity.solidArea.y += entity.speed;
						break;
					case "up":
						entity.solidArea.y -= entity.speed;
						break;
					case "left":
						entity.solidArea.x -= entity.speed;
						break;
					case "right":
						entity.solidArea.x += entity.speed;
						break;
				}

				if (entity.solidArea.intersects(game.objs[i].solidArea)) {
					if (game.objs[i].collision) entity.collisionOn = true;
					if (entity instanceof Player) index = i;
				}

				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				game.objs[i].solidArea.x = game.objs[i].solidAreaDefaultX;
				game.objs[i].solidArea.y = game.objs[i].solidAreaDefaultY;
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
				// Obtiene la posicion del area solida de la entidad y de la otra entidad
				entity.solidArea.x += entity.worldX;
				entity.solidArea.y += entity.worldY;
				otherEntity[i].solidArea.x += otherEntity[i].worldX;
				otherEntity[i].solidArea.y += otherEntity[i].worldY;
				switch (entity.direction) {
					case "down":
						entity.solidArea.y += entity.speed;
						break;
					case "up":
						entity.solidArea.y -= entity.speed;
						break;
					case "left":
						entity.solidArea.x -= entity.speed;
						break;
					case "right":
						entity.solidArea.x += entity.speed;
						break;
				}

				if (entity.solidArea.intersects(otherEntity[i].solidArea)) {
					if (otherEntity[i] != entity) { // Evita la colision en si misma
						entity.collisionOn = true;
						index = i;
					}
				}

				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				otherEntity[i].solidArea.x = otherEntity[i].solidAreaDefaultX;
				otherEntity[i].solidArea.y = otherEntity[i].solidAreaDefaultY;
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
		entity.solidArea.x += entity.worldX;
		entity.solidArea.y += entity.worldY;
		game.player.solidArea.x += game.player.worldX;
		game.player.solidArea.y += game.player.worldY;
		switch (entity.direction) {
			case "down":
				entity.solidArea.y += entity.speed;
				break;
			case "up":
				entity.solidArea.y -= entity.speed;
				break;
			case "left":
				entity.solidArea.x -= entity.speed;
				break;
			case "right":
				entity.solidArea.x += entity.speed;
				break;
		}

		if (entity.solidArea.intersects(game.player.solidArea)) {
			entity.collisionOn = true;
			contact = true;
		}

		entity.solidArea.x = entity.solidAreaDefaultX;
		entity.solidArea.y = entity.solidAreaDefaultY;
		game.player.solidArea.x = game.player.solidAreaDefaultX;
		game.player.solidArea.y = game.player.solidAreaDefaultY;

		return contact;

	}

}
