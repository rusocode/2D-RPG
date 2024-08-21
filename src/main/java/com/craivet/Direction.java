package com.craivet;

import com.craivet.input.keyboard.Key;
import com.craivet.world.entity.Entity;

public enum Direction {

    DOWN,
    UP,
    LEFT,
    RIGHT,
    ANY;

    /**
     * Gets the direction depending on the selected key.
     */
    public void get(Entity entity) {
        if (entity.game.keyboard.isKeyPressed(Key.DOWN)) entity.direction = DOWN;
        else if (entity.game.keyboard.isKeyPressed(Key.UP)) entity.direction = UP;
        else if (entity.game.keyboard.isKeyPressed(Key.LEFT)) entity.direction = LEFT;
        else if (entity.game.keyboard.isKeyPressed(Key.RIGHT)) entity.direction = RIGHT;
    }

    /* Temporary variable that stores the direction of the attacker at the time of the attack to update the position of
     * the entity while its frame remains in the same direction. */
    public Direction knockbackDirection;

}
