package com.punkipunk;

import com.punkipunk.entity.Entity;
import com.punkipunk.input.keyboard.Key;

public enum Direction {

    DOWN,
    UP,
    LEFT,
    RIGHT,
    ANY;

    /* Variable temporal que almacena la direccion del atacante en el momento del ataque para actualizar la posicion de la entidad
     * mientras su frame permanece en la misma direccion. */
    public Direction knockbackDirection;

    /**
     * Obtiene la direccion dependiendo de la tecla presionada.
     */
    public void get(Entity entity) {
        if (entity.game.getGameSystem().keyboard.isKeyPressed(Key.DOWN)) entity.direction = DOWN;
        else if (entity.game.getGameSystem().keyboard.isKeyPressed(Key.UP)) entity.direction = UP;
        else if (entity.game.getGameSystem().keyboard.isKeyPressed(Key.LEFT)) entity.direction = LEFT;
        else if (entity.game.getGameSystem().keyboard.isKeyPressed(Key.RIGHT)) entity.direction = RIGHT;
    }

}
