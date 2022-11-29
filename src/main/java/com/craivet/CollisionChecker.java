package com.craivet;

import com.craivet.entity.Entity;

public class CollisionChecker {

	public Game game;

	public CollisionChecker(Game game) {
		this.game = game;
	}

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
}
