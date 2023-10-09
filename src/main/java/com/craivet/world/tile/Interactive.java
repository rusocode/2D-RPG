package com.craivet.world.tile;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.item.Item;

import static com.craivet.utils.Global.*;

// TODO This class has to go in the entity package
public class Interactive extends Entity {

    public boolean destructible;

    public Interactive(Game game, World world, int x, int y) {
        super(game, world, x, y);
    }

    public void update() {
        if (flags.invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_INTERACTIVE);
    }

    /**
     * Check if the selected weapon is the correct one to use with the interactive tile.
     *
     * @param weapon selected weapon.
     * @return true if the selected weapon is correct.
     */
    public boolean isCorrectWeapon(Item weapon) {
        return false;
    }

    /**
     * Replaces the interactive tile (when destroyed) with the new interactive tile.
     *
     * @return the new interactive tile.
     */
    public Interactive replaceBy() {
        return null;
    }

    public void playSound() {
    }

}
