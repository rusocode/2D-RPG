package com.craivet.tile;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;

import static com.craivet.utils.Constants.*;

public class InteractiveTile extends Entity {

	public boolean destructible;

	public InteractiveTile(Game game) {
		super(game);
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
	public InteractiveTile getDestroyedForm() {
		return null;
	}

	public void update() {
		if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_TREE);
	}

}
