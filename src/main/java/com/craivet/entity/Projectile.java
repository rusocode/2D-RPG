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

	public void update() {
		// Si el player lanza un fireball
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

		// Cuando llega a cero el proyectil desaparece
		life--;
		if (life <= 0) alive = false;

		timer.timeMovement(this, 10);

	}

}
