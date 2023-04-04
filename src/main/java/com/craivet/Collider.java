package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.Player;

import static com.craivet.utils.Constants.*;

/**
 * Fisica y deteccion de colisiones.
 */

public class Collider {

	private final Game game;
	private final World world;

	public Collider(Game game, World world) {
		this.game = game;
		this.world = world;
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
				tileNum1 = world.tileIndex[world.map][entityBottomRow][entityLeftCol];
				tileNum2 = world.tileIndex[world.map][entityBottomRow][entityRightCol];
				if (world.tile[tileNum1].solid || world.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case UP:
				entityTopRow = (entityTopWorldY - entity.speed) / tile_size;
				tileNum1 = world.tileIndex[world.map][entityTopRow][entityLeftCol];
				tileNum2 = world.tileIndex[world.map][entityTopRow][entityRightCol];
				if (world.tile[tileNum1].solid || world.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case LEFT:
				entityLeftCol = (entityLeftWorldX - entity.speed) / tile_size;
				tileNum1 = world.tileIndex[world.map][entityTopRow][entityLeftCol];
				tileNum2 = world.tileIndex[world.map][entityBottomRow][entityLeftCol];
				if (world.tile[tileNum1].solid || world.tile[tileNum2].solid)
					entity.collisionOn = true;
				break;
			case RIGHT:
				entityRightCol = (entityRightWorldX + entity.speed) / tile_size;
				tileNum1 = world.tileIndex[world.map][entityTopRow][entityRightCol];
				tileNum2 = world.tileIndex[world.map][entityBottomRow][entityRightCol];
				if (world.tile[tileNum1].solid || world.tile[tileNum2].solid)
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
		for (int i = 0; i < world.items[1].length; i++) {
			if (world.items[world.map][i] != null) {
				// Obtiene la posicion del area del cuerpo de la entidad y del objeto
				entity.hitbox.x += entity.worldX;
				entity.hitbox.y += entity.worldY;
				world.items[world.map][i].hitbox.x += world.items[world.map][i].worldX;
				world.items[world.map][i].hitbox.y += world.items[world.map][i].worldY;
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

				if (entity.hitbox.intersects(world.items[world.map][i].hitbox)) {
					if (world.items[world.map][i].collision) entity.collisionOn = true;
					if (entity instanceof Player) index = i;
				}

				entity.hitbox.x = entity.hitboxDefaultX;
				entity.hitbox.y = entity.hitboxDefaultY;
				world.items[world.map][i].hitbox.x = world.items[world.map][i].hitboxDefaultX;
				world.items[world.map][i].hitbox.y = world.items[world.map][i].hitboxDefaultY;
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
			if (otherEntity[world.map][i] != null) {
				// Obtiene la posicion del area del cuerpo de la entidad y de la otra entidad
				entity.hitbox.x += entity.worldX;
				entity.hitbox.y += entity.worldY;
				otherEntity[world.map][i].hitbox.x += otherEntity[world.map][i].worldX;
				otherEntity[world.map][i].hitbox.y += otherEntity[world.map][i].worldY;
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

				if (entity.hitbox.intersects(otherEntity[world.map][i].hitbox)) {
					if (otherEntity[world.map][i] != entity) { // Evita la colision en si misma
						entity.collisionOn = true;
						index = i;
						// TODO No tendria que romper el bucle una vez que hay colision?
					}
				}

				entity.hitbox.x = entity.hitboxDefaultX;
				entity.hitbox.y = entity.hitboxDefaultY;
				otherEntity[world.map][i].hitbox.x = otherEntity[world.map][i].hitboxDefaultX;
				otherEntity[world.map][i].hitbox.y = otherEntity[world.map][i].hitboxDefaultY;
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
