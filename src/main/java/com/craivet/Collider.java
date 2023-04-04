package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.EntityManager;
import com.craivet.entity.Player;

import static com.craivet.utils.Constants.*;

/**
 * Fisica y deteccion de colisiones.
 */

public class Collider {

	private final EntityManager game;

	public Collider(EntityManager game) {
		this.game = game;
	}

	/**
	 * Verifica si la entidad colisiona con el tile.
	 *
	 * @param entity la entidad.
	 */
	public void checkTile(Entity entity) {

		int entityBottomWorldY = entity.worldY + entity.hitbox.y + entity.hitbox.height;
		int entityTopWorldY = entity.worldY + entity.hitbox.y;
		int entityLeftWorldX = entity.worldX + entity.hitbox.x;
		int entityRightWorldX = entity.worldX + entity.hitbox.x + entity.hitbox.width;

		int entityBottomRow = entityBottomWorldY / tile_size;
		int entityTopRow = entityTopWorldY / tile_size;
		int entityLeftCol = entityLeftWorldX / tile_size;
		int entityRightCol = entityRightWorldX / tile_size;

		// En caso de que la entidad colisione en el medio de dos tiles
		int tileNum1, tileNum2;

		switch (entity.direction) {
			case DOWN:
				entityBottomRow = (entityBottomWorldY + entity.speed) / tile_size;
				tileNum1 = game.world.tileIndex[game.map][entityBottomRow][entityLeftCol];
				tileNum2 = game.world.tileIndex[game.map][entityBottomRow][entityRightCol];
				if (game.world.tile[tileNum1].solid || game.world.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case UP:
				entityTopRow = (entityTopWorldY - entity.speed) / tile_size;
				tileNum1 = game.world.tileIndex[game.map][entityTopRow][entityLeftCol];
				tileNum2 = game.world.tileIndex[game.map][entityTopRow][entityRightCol];
				if (game.world.tile[tileNum1].solid || game.world.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case LEFT:
				entityLeftCol = (entityLeftWorldX - entity.speed) / tile_size;
				tileNum1 = game.world.tileIndex[game.map][entityTopRow][entityLeftCol];
				tileNum2 = game.world.tileIndex[game.map][entityBottomRow][entityLeftCol];
				if (game.world.tile[tileNum1].solid || game.world.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case RIGHT:
				entityRightCol = (entityRightWorldX + entity.speed) / tile_size;
				tileNum1 = game.world.tileIndex[game.map][entityTopRow][entityRightCol];
				tileNum2 = game.world.tileIndex[game.map][entityBottomRow][entityRightCol];
				if (game.world.tile[tileNum1].solid || game.world.tile[tileNum2].solid)
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
			if (game.items[game.map][i] != null) {
				// Obtiene la posicion del area del cuerpo de la entidad y del objeto
				entity.hitbox.x += entity.worldX;
				entity.hitbox.y += entity.worldY;
				game.items[game.map][i].hitbox.x += game.items[game.map][i].worldX;
				game.items[game.map][i].hitbox.y += game.items[game.map][i].worldY;
				switch (entity.direction) {
					case DOWN:
						entity.hitbox.y += entity.speed;
						break;
					case UP:
						entity.hitbox.y -= entity.speed;
						break;
					case LEFT:
						entity.hitbox.x -= entity.speed;
						break;
					case RIGHT:
						entity.hitbox.x += entity.speed;
						break;
				}

				if (entity.hitbox.intersects(game.items[game.map][i].hitbox)) {
					if (game.items[game.map][i].collision) entity.collisionOn = true;
					if (entity instanceof Player) index = i;
				}

				entity.hitbox.x = entity.hitboxDefaultX;
				entity.hitbox.y = entity.hitboxDefaultY;
				game.items[game.map][i].hitbox.x = game.items[game.map][i].hitboxDefaultX;
				game.items[game.map][i].hitbox.y = game.items[game.map][i].hitboxDefaultY;
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
			if (otherEntity[game.map][i] != null) {
				// Obtiene la posicion del area del cuerpo de la entidad y de la otra entidad
				entity.hitbox.x += entity.worldX;
				entity.hitbox.y += entity.worldY;
				otherEntity[game.map][i].hitbox.x += otherEntity[game.map][i].worldX;
				otherEntity[game.map][i].hitbox.y += otherEntity[game.map][i].worldY;
				switch (entity.direction) {
					case DOWN:
						entity.hitbox.y += entity.speed;
						break;
					case UP:
						entity.hitbox.y -= entity.speed;
						break;
					case LEFT:
						entity.hitbox.x -= entity.speed;
						break;
					case RIGHT:
						entity.hitbox.x += entity.speed;
						break;
				}

				if (entity.hitbox.intersects(otherEntity[game.map][i].hitbox)) {
					if (otherEntity[game.map][i] != entity) { // Evita la colision en si misma
						entity.collisionOn = true;
						index = i;
						// TODO No tendria que romper el bucle una vez que hay colision?
					}
				}

				entity.hitbox.x = entity.hitboxDefaultX;
				entity.hitbox.y = entity.hitboxDefaultY;
				otherEntity[game.map][i].hitbox.x = otherEntity[game.map][i].hitboxDefaultX;
				otherEntity[game.map][i].hitbox.y = otherEntity[game.map][i].hitboxDefaultY;
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
		entity.hitbox.x += entity.worldX;
		entity.hitbox.y += entity.worldY;
		game.player.hitbox.x += game.player.worldX;
		game.player.hitbox.y += game.player.worldY;
		switch (entity.direction) {
			case DOWN:
				entity.hitbox.y += entity.speed;
				break;
			case UP:
				entity.hitbox.y -= entity.speed;
				break;
			case LEFT:
				entity.hitbox.x -= entity.speed;
				break;
			case RIGHT:
				entity.hitbox.x += entity.speed;
				break;
		}

		if (entity.hitbox.intersects(game.player.hitbox)) {
			entity.collisionOn = true;
			contact = true;
		}

		entity.hitbox.x = entity.hitboxDefaultX;
		entity.hitbox.y = entity.hitboxDefaultY;
		game.player.hitbox.x = game.player.hitboxDefaultX;
		game.player.hitbox.y = game.player.hitboxDefaultY;

		return contact;

	}

}
