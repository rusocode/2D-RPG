package com.craivet.tile;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;

import static com.craivet.utils.Global.*;

public class Interactive extends Entity {

    public boolean destructible;

    public Interactive(Game game, World world) {
        super(game, world);
    }

    public void update() {
        if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_INTERACTIVE);
    }

    /**
     * Comprueba si el weapon seleccionado es el correcto para interactuar con el tile.
     *
     * @param weapon weapon seleccionado.
     * @return true si el weapon seleccionado es el correcto o false.
     */
    public boolean isCorrectWeapon(Entity weapon) {
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

    public void playSound() {
    }

}
