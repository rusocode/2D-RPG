package com.craivet.utils;

import java.awt.*;
import java.util.Random;

import com.craivet.entity.Entity;

/**
 * Temporizador para controlar las diferentes acciones del juego haciendolo mas natural.
 *
 * <p>Los parametros para cada metodo son la entidad y el cooldown (o intervalos).
 *
 * @author Juan Debenedetti
 */

public class Timer {

	public int movementCounter;
	public int invincibleCounter;
	public int directionCounter;
	public int deadCounter;
	public int hpBarCounter;

	/**
	 * Temporiza el movimiento.
	 *
	 * <p>Si se completo el cooldown y si la entidad esta en el frame de movimiento 1, cambia al frame de movimiento 2 y
	 * resetea el contador. Si se completo el cooldown y si la entidad esta en el frame de movimiento 2, cambia al frame
	 * de movimiento 1 y resetea el contador.
	 */
	public void timeMovement(Entity entity, int cooldown) {
		movementCounter++;
		if (movementCounter > cooldown - entity.speed) {
			if (entity.movementNum == 1) entity.movementNum = 2;
			else if (entity.movementNum == 2) entity.movementNum = 1;
			movementCounter = 0;
		}
	}

	/**
	 * Temporiza la invencibilidad.
	 *
	 * <p>Si se completo el cooldown, deja de ser invencible y resetea el contador.
	 */
	public void timeInvincible(Entity entity, int cooldown) {
		invincibleCounter++;
		if (invincibleCounter > cooldown) {
			entity.invincible = false;
			invincibleCounter = 0;
		}
	}

	/**
	 * Temporiza la direccion.
	 *
	 * <p>Si se completo el cooldown, calcula un numero aleatorio entre 1 y 100 para determinar la nueva direccion. Si
	 * el numero es menor o igual a 25, cambia a "down" y resetea el contador. Si el numero esta entre 26 y 50, ambos
	 * incluidos, cambia a "up" y resetea el contador. Si el numero esta entre 51 y 75, ambos incluidos, cambia a "left"
	 * y resetea el contador. Si el numero es mayor a 75, cambia a "right" y resetea el contador.
	 */
	public void timeDirection(Entity entity, int cooldown) {
		directionCounter++;
		if (directionCounter == cooldown) {
			Random random = new Random();
			int i = random.nextInt(100) + 1;
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
	 * <p>Si el contador es menor o igual al cooldown, cambia la transparencia a 0. Si el contador esta entre el
	 * cooldown y el cooldown * 2, cambia la transparencia a 1. Si el contador esta entre el cooldown * 2 y el cooldown
	 * * 3, cambia la transparencia a 0. Si el contador esta entre el cooldown * 3 y el cooldown * 4, cambia la
	 * transparencia a 1. Si el contador esta entre el cooldown * 4 y el cooldown * 5, cambia la transparencia a 0. Si
	 * el contador esta entre el cooldown * 5 y el cooldown * 6, cambia la transparencia a 1. Si el contador esta entre
	 * el cooldown * 6 y el cooldown * 7, cambia la transparencia a 0. Si el contador esta entre el cooldown * 7 y el
	 * cooldown * 8, cambia la transparencia a 1. Si el contador es mayor al cooldown * 8, establece el estado dead y
	 * alive en false, evitando que el mob actualice su posicion y se elimine como objeto.
	 */
	public void timeDeadAnimation(Entity entity, int cooldown, Graphics2D g2) {
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

	/**
	 * Temporiza la barra de vida.
	 *
	 * <p>Si se completo el cooldown, resetea el contador y desactiva la barra de vida.
	 */
	public void timeHpBar(Entity entity, int cooldown) {
		hpBarCounter++;
		if (hpBarCounter > cooldown) {
			hpBarCounter = 0;
			entity.hpBarOn = false;
		}
	}

}
