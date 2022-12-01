package com.craivet;

import com.craivet.entity.Entity;

public class CollisionChecker {

	public Game game;

	public CollisionChecker(Game game) {
		this.game = game;
	}

	/**
	 * Verifica si la entidad colisiona con el tile.
	 *
	 * @param entity la entidad con la que colisiona el tile.
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
	 * @param entity la entidad con la que colisiona el objeto.
	 * @param player solo el player puede recoger objetos.
	 * @return index el indice del objeto en caso de que sea el player el que colisione con este.
	 */
	public int checkObject(Entity entity, boolean player) {
		int index = 999;
		for (int i = 0; i < game.objs.length; i++) {
			if (game.objs[i] != null) {
				// Obtiene la posicion del area solida de la entidad
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;
				// Obtiene la posicion del area solida del objeto
				game.objs[i].solidArea.x = game.objs[i].worldX + game.objs[i].solidArea.x;
				game.objs[i].solidArea.y = game.objs[i].worldY + game.objs[i].solidArea.y;
				switch (entity.direction) {
					case "down":
						entity.solidArea.y += entity.speed;
						if (entity.solidArea.intersects(game.objs[i].solidArea)) {
							if (game.objs[i].collision) entity.collisionOn = true;
							if (player) index = i;
						}
						break;
					case "up":
						entity.solidArea.y -= entity.speed;
						if (entity.solidArea.intersects(game.objs[i].solidArea)) {
							if (game.objs[i].collision) entity.collisionOn = true;
							if (player) index = i;
						}
						break;
					case "left":
						entity.solidArea.x -= entity.speed;
						if (entity.solidArea.intersects(game.objs[i].solidArea)) {
							if (game.objs[i].collision) entity.collisionOn = true;
							if (player) index = i;
						}
						break;
					case "right":
						entity.solidArea.x += entity.speed;
						if (entity.solidArea.intersects(game.objs[i].solidArea)) {
							if (game.objs[i].collision) entity.collisionOn = true;
							if (player) index = i;
						}
						break;
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				game.objs[i].solidArea.x = game.objs[i].solidAreaDefaultX;
				game.objs[i].solidArea.y = game.objs[i].solidAreaDefaultY;
			}
		}
		return index;
	}

}
