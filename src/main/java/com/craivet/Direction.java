package com.craivet;

import com.craivet.world.entity.Entity;

import java.awt.event.KeyEvent;

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
        if (entity.game.keyboard.isKeyPressed(KeyEvent.VK_S)) entity.direction = DOWN;
        else if (entity.game.keyboard.isKeyPressed(KeyEvent.VK_W)) entity.direction = UP;
        else if (entity.game.keyboard.isKeyPressed(KeyEvent.VK_A)) entity.direction = LEFT;
        else if (entity.game.keyboard.isKeyPressed(KeyEvent.VK_D)) entity.direction = RIGHT;
    }

    /* Temporary variable that stores the direction of the attacker at the time of the attack to update the position of
     * the entity while its frame remains in the same direction. */
    public Direction knockbackDirection;

}
