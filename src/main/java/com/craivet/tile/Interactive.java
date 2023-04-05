package com.craivet.tile;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;

import static com.craivet.utils.Constants.*;

public class Interactive extends Entity {

	public boolean destructible;

	public Interactive(Game game, World world) {
		super(game, world);
	}

	/**
	 * Comprueba si el item seleccionado es el correcto para interactuar con el tile.
	 *
	 * @param item el item seleccionado.
	 * @return true si el item seleccionado es el correcto o false.
	 */
	public boolean isCorrectItem(Entity item) {
		return false;
	}

	/**
	 * Reemplaza el tile interactivo (cuando es destruido) por el nuevo tile interactivo en la misma posicion.
	 *
	 * @return el nuevo tile interactivo en la misma posicion.
	 */
	public Interactive getDestroyedForm() {
		return null;
	}

	public void update() {
		if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_TREE);
	}

}
