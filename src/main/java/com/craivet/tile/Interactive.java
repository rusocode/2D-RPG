package com.craivet.tile;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;

import static com.craivet.utils.Global.*;

// TODO Esta clase tiene que ir en el paquete de entidades
public class Interactive extends Entity {

    public boolean destructible;

    public Interactive(Game game, World world, int x, int y) {
        super(game, world, x, y);
    }

    public void update() {
        if (flags.invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_INTERACTIVE);
    }

    /**
     * Comprueba si el arma seleccionada es la correcta para usar con el tile interactivo.
     *
     * @param weapon arma seleccionada.
     * @return true si el arma seleccionada es la correcta.
     */
    public boolean isCorrectWeapon(Entity weapon) {
        return false;
    }

    /**
     * Reemplaza el tile interactivo (cuando es destruido) por el nuevo tile interactivo.
     *
     * @return el nuevo tile interactivo.
     */
    public Interactive replaceBy() {
        return null;
    }

    public void playSound() {
    }

}
