package com.craivet.utils;

import com.craivet.entity.Entity;

import java.awt.*;
import java.util.Random;

/**
 * Temporizador para controlar las diferentes acciones del juego (usando un cooldown especifico) haciendolo mas natural.
 */

public class Timer {

	public int movementCounter;
	public int invincibleCounter;
	public int actionLockCounter;
	public int deadCounter;
	public int hpBarCounter;

	/**
	 * Controla el movimiento.
	 */
	public void movement(Entity entity, int cooldown) {
		movementCounter++;
		if (movementCounter > cooldown - entity.speed) {
			if (entity.movementNum == 1) entity.movementNum = 2;
			else if (entity.movementNum == 2) entity.movementNum = 1;
			movementCounter = 0;
		}
	}

	/**
	 * Controla la invencibilidad.
	 */
	public void invincible(Entity entity, int cooldown) {
		invincibleCounter++;
		if (invincibleCounter > cooldown) {
			entity.invincible = false;
			invincibleCounter = 0;
		}
	}

	/**
	 * Controla el cambio de direccion.
	 */
	public void actionLock(Entity entity, int cooldown) {
		actionLockCounter++;
		if (actionLockCounter == cooldown) {
			Random random = new Random();
			int i = random.nextInt(100) + 1;
			if (i <= 25) entity.direction = "down";
			if (i > 25 && i <= 50) entity.direction = "up";
			if (i > 50 && i <= 75) entity.direction = "left";
			if (i > 75) entity.direction = "right";
			actionLockCounter = 0;
		}
	}

	/**
	 * Controla la animacion de muerte.
	 */
	public void deadAnimation(Entity entity, int cooldown, Graphics2D g2) {
		deadCounter++;
		if (deadCounter <= cooldown) Utils.changeAlpha(g2, 0);
		if (deadCounter > cooldown && deadCounter <= cooldown * 2) Utils.changeAlpha(g2, 1);
		if (deadCounter > cooldown * 2 && deadCounter <= cooldown * 3) Utils.changeAlpha(g2, 0);
		if (deadCounter > cooldown * 3 && deadCounter <= cooldown * 4) Utils.changeAlpha(g2, 1);
		if (deadCounter > cooldown * 4 && deadCounter <= cooldown * 5) Utils.changeAlpha(g2, 0);
		if (deadCounter > cooldown * 5 && deadCounter <= cooldown * 6) Utils.changeAlpha(g2, 1);
		if (deadCounter > cooldown * 6 && deadCounter <= cooldown * 7) Utils.changeAlpha(g2, 0);
		if (deadCounter > cooldown * 7 && deadCounter <= cooldown * 8) Utils.changeAlpha(g2, 1);
		if (deadCounter > cooldown * 8) {
			entity.dead = false;
			entity.alive = false;
		}
	}

	public void hpBar(Entity entity, int cooldown) {
		hpBarCounter++;
		if (hpBarCounter > cooldown) {
			hpBarCounter = 0;
			entity.hpBarOn = false;
		}
	}

}
