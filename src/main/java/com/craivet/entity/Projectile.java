package com.craivet.entity;

import com.craivet.Game;

public class Projectile extends Entity {

	private Entity entity;

	public Projectile(Game game) {
		super(game);
	}

	public void set(int worldX, int worldY, String direction, boolean alive, Entity entity) {
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.alive = alive;
		this.entity = entity;
		this.life = this.maxLife; // Resetea la vida al valor maximo cada vez que lanza un proyectil
	}

	/**
	 * Actualiza la posicion del fireball y si colisiona con un mob o cuando se termina la vida, deja de estar vivo.
	 */
	public void update() {
		if (entity instanceof Player) {
			int mobIndex = game.cChecker.checkEntity(this, game.mobs);
			if (mobIndex != -1) {
				game.player.damageMob(mobIndex, attack);
				alive = false;
			}
		}

		switch (direction) {
			case "down":
				worldY += speed;
				break;
			case "up":
				worldY -= speed;
				break;
			case "left":
				worldX -= speed;
				break;
			case "right":
				worldX += speed;
				break;
		}

		life--;
		if (life <= 0) alive = false;

		if (alive) {
			movementCounter++;
			if (movementCounter > 10) {
				if (entity.movementNum == 1) entity.movementNum = 2;
				else if (entity.movementNum == 2) entity.movementNum = 1;
				movementCounter = 0;
			}
			// timer.timeMovement(this, 10);
		}

	}

}
