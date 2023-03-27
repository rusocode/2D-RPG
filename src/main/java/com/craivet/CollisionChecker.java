package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.Player;

import static com.craivet.utils.Constants.*;

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

		int entityBottomWorldY = entity.worldY + entity.bodyArea.y + entity.bodyArea.height;
		int entityTopWorldY = entity.worldY + entity.bodyArea.y;
		int entityLeftWorldX = entity.worldX + entity.bodyArea.x;
		int entityRightWorldX = entity.worldX + entity.bodyArea.x + entity.bodyArea.width;

		int entityBottomRow = entityBottomWorldY / tile_size;
		int entityTopRow = entityTopWorldY / tile_size;
		int entityLeftCol = entityLeftWorldX / tile_size;
		int entityRightCol = entityRightWorldX / tile_size;

		// En caso de que la entidad colisione en el medio de dos tiles
		int tileNum1, tileNum2;

		switch (entity.direction) {
			case DIR_DOWN:
				entityBottomRow = (entityBottomWorldY + entity.speed) / tile_size;
				tileNum1 = game.tileManager.tileIndex[game.currentMap][entityBottomRow][entityLeftCol];
				tileNum2 = game.tileManager.tileIndex[game.currentMap][entityBottomRow][entityRightCol];
				if (game.tileManager.tile[tileNum1].solid || game.tileManager.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case DIR_UP:
				entityTopRow = (entityTopWorldY - entity.speed) / tile_size;
				tileNum1 = game.tileManager.tileIndex[game.currentMap][entityTopRow][entityLeftCol];
				tileNum2 = game.tileManager.tileIndex[game.currentMap][entityTopRow][entityRightCol];
				if (game.tileManager.tile[tileNum1].solid || game.tileManager.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case DIR_LEFT:
				entityLeftCol = (entityLeftWorldX - entity.speed) / tile_size;
				tileNum1 = game.tileManager.tileIndex[game.currentMap][entityTopRow][entityLeftCol];
				tileNum2 = game.tileManager.tileIndex[game.currentMap][entityBottomRow][entityLeftCol];
				if (game.tileManager.tile[tileNum1].solid || game.tileManager.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case DIR_RIGHT:
				entityRightCol = (entityRightWorldX + entity.speed) / tile_size;
				tileNum1 = game.tileManager.tileIndex[game.currentMap][entityTopRow][entityRightCol];
				tileNum2 = game.tileManager.tileIndex[game.currentMap][entityBottomRow][entityRightCol];
				if (game.tileManager.tile[tileNum1].solid || game.tileManager.tile[tileNum2].solid)
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
		for (int i = 0; i < game.items[1].length; i++) {
			if (game.items[game.currentMap][i] != null) {
				// Obtiene la posicion del area del cuerpo de la entidad y del objeto
				entity.bodyArea.x += entity.worldX;
				entity.bodyArea.y += entity.worldY;
				game.items[game.currentMap][i].bodyArea.x += game.items[game.currentMap][i].worldX;
				game.items[game.currentMap][i].bodyArea.y += game.items[game.currentMap][i].worldY;
				switch (entity.direction) {
					case DIR_DOWN:
						entity.bodyArea.y += entity.speed;
						break;
					case DIR_UP:
						entity.bodyArea.y -= entity.speed;
						break;
					case DIR_LEFT:
						entity.bodyArea.x -= entity.speed;
						break;
					case DIR_RIGHT:
						entity.bodyArea.x += entity.speed;
						break;
				}

				if (entity.bodyArea.intersects(game.items[game.currentMap][i].bodyArea)) {
					if (game.items[game.currentMap][i].collision) entity.collisionOn = true;
					if (entity instanceof Player) index = i;
				}

				entity.bodyArea.x = entity.bodyAreaDefaultX;
				entity.bodyArea.y = entity.bodyAreaDefaultY;
				game.items[game.currentMap][i].bodyArea.x = game.items[game.currentMap][i].bodyAreaDefaultX;
				game.items[game.currentMap][i].bodyArea.y = game.items[game.currentMap][i].bodyAreaDefaultY;
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
	public int checkEntity(Entity entity, Entity[][] otherEntity) {
		int index = -1;
		for (int i = 0; i < otherEntity[1].length; i++) {
			if (otherEntity[game.currentMap][i] != null) {
				// Obtiene la posicion del area del cuerpo de la entidad y de la otra entidad
				entity.bodyArea.x += entity.worldX;
				entity.bodyArea.y += entity.worldY;
				otherEntity[game.currentMap][i].bodyArea.x += otherEntity[game.currentMap][i].worldX;
				otherEntity[game.currentMap][i].bodyArea.y += otherEntity[game.currentMap][i].worldY;
				switch (entity.direction) {
					case DIR_DOWN:
						entity.bodyArea.y += entity.speed;
						break;
					case DIR_UP:
						entity.bodyArea.y -= entity.speed;
						break;
					case DIR_LEFT:
						entity.bodyArea.x -= entity.speed;
						break;
					case DIR_RIGHT:
						entity.bodyArea.x += entity.speed;
						break;
				}

				if (entity.bodyArea.intersects(otherEntity[game.currentMap][i].bodyArea)) {
					if (otherEntity[game.currentMap][i] != entity) { // Evita la colision en si misma
						entity.collisionOn = true;
						index = i;
						// TODO No tendria que romper el bucle una vez que hay colision?
					}
				}

				entity.bodyArea.x = entity.bodyAreaDefaultX;
				entity.bodyArea.y = entity.bodyAreaDefaultY;
				otherEntity[game.currentMap][i].bodyArea.x = otherEntity[game.currentMap][i].bodyAreaDefaultX;
				otherEntity[game.currentMap][i].bodyArea.y = otherEntity[game.currentMap][i].bodyAreaDefaultY;
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
			case DIR_DOWN:
				entity.bodyArea.y += entity.speed;
				break;
			case DIR_UP:
				entity.bodyArea.y -= entity.speed;
				break;
			case DIR_LEFT:
				entity.bodyArea.x -= entity.speed;
				break;
			case DIR_RIGHT:
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
