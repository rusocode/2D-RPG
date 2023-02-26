package com.craivet.utils;

import java.awt.*;
import java.util.Random;

import com.craivet.entity.Entity;

import static com.craivet.utils.Constants.INTERVAL_PROJECTILE_ATTACK;

/**
 * Temporiza las acciones del juego.
 *
 * @author Juan Debenedetti
 */

public class Timer {

	public int movementCounter;
	public int projectileCounter;
	public int attackCounter;
	public int attackAnimationCounter;
	public int naturalStopWalkingCounter;
	public int invincibleCounter;
	public int directionCounter;
	public int deadCounter;
	public int hpBarCounter;

	/**
	 * Temporiza el movimiento.
	 *
	 * <p>Si se completo el interval y si la entidad esta en el frame de movimiento 1, cambia al frame de movimiento 2 y
	 * resetea el contador. Si se completo el interval y si la entidad esta en el frame de movimiento 2, cambia al frame
	 * de movimiento 1 y resetea el contador.
	 */
	public void timeMovement(Entity entity, final int interval) {
		if (movementCounter++ >= interval - entity.speed) {
			if (entity.movementNum == 1) entity.movementNum = 2;
			else if (entity.movementNum == 2) entity.movementNum = 1;
			movementCounter = 0;
		}
	}

	public void timeNaturalStopWalking(Entity entity, final int interval) {
		naturalStopWalkingCounter++;
		if (naturalStopWalkingCounter >= interval) {
			entity.movementNum = 1;
			naturalStopWalkingCounter = 0;
		}
	}

	/**
	 * Temporiza la invencibilidad.
	 *
	 * <p>Si se completo el interval, deja de ser invencible y resetea el contador.
	 */
	public void timeInvincible(Entity entity, final int interval) {
		if (invincibleCounter++ >= interval) {
			entity.invincible = false;
			invincibleCounter = 0;
		}
	}

	/**
	 * Temporiza la direccion.
	 *
	 * <p>Si se completo el interval, calcula un numero aleatorio entre 1 y 100 para determinar la nueva direccion. Si
	 * el numero es menor o igual a 25, cambia a "down" y resetea el contador. Si el numero esta entre 26 y 50, ambos
	 * incluidos, cambia a "up" y resetea el contador. Si el numero esta entre 51 y 75, ambos incluidos, cambia a "left"
	 * y resetea el contador. Si el numero es mayor a 75, cambia a "right" y resetea el contador.
	 */
	public void timeDirection(Entity entity, final int interval) {
		if (directionCounter++ >= interval) {
			int i = Utils.azar(100);
			if (i <= 25) entity.direction = "down";
			if (i > 25 && i <= 50) entity.direction = "up";
			if (i > 50 && i <= 75) entity.direction = "left";
			if (i > 75) entity.direction = "right";
			directionCounter = 0;
		}
	}

	/**
	 * Temporiza la animacion de muerte.
	 *
	 * <p>Si el contador es menor o igual al interval, cambia la transparencia a 0. Si el contador esta entre el
	 * interval y el interval * 2, cambia la transparencia a 1. Si el contador esta entre el interval * 2 y el interval
	 * * 3, cambia la transparencia a 0. Si el contador esta entre el interval * 3 y el interval * 4, cambia la
	 * transparencia a 1. Si el contador esta entre el interval * 4 y el interval * 5, cambia la transparencia a 0. Si
	 * el contador esta entre el interval * 5 y el interval * 6, cambia la transparencia a 1. Si el contador esta entre
	 * el interval * 6 y el interval * 7, cambia la transparencia a 0. Si el contador esta entre el interval * 7 y el
	 * interval * 8, cambia la transparencia a 1. Si el contador es mayor al interval * 8, establece el estado alive en
	 * false.
	 */
	public void timeDeadAnimation(Entity entity, final int interval, Graphics2D g2) {
		deadCounter++;
		if (deadCounter <= interval) Utils.changeAlpha(g2, 0);
		if (deadCounter > interval && deadCounter <= interval * 2) Utils.changeAlpha(g2, 1);
		if (deadCounter > interval * 2 && deadCounter <= interval * 3) Utils.changeAlpha(g2, 0);
		if (deadCounter > interval * 3 && deadCounter <= interval * 4) Utils.changeAlpha(g2, 1);
		if (deadCounter > interval * 4 && deadCounter <= interval * 5) Utils.changeAlpha(g2, 0);
		if (deadCounter > interval * 5 && deadCounter <= interval * 6) Utils.changeAlpha(g2, 1);
		if (deadCounter > interval * 6 && deadCounter <= interval * 7) Utils.changeAlpha(g2, 0);
		if (deadCounter > interval * 7 && deadCounter <= interval * 8) Utils.changeAlpha(g2, 1);
		if (deadCounter > interval * 8) entity.alive = false;
	}

	/**
	 * Temporiza la barra de vida.
	 *
	 * <p>Si se completo el interval, desactiva la barra de vida y resetea el contador.
	 */
	public void timeHpBar(Entity entity, final int interval) {
		if (hpBarCounter++ >= interval) {
			entity.hpBarOn = false;
			hpBarCounter = 0;
		}
	}

}
