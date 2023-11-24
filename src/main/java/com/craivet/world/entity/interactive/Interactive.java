package com.craivet.world.entity.interactive;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.item.Item;

import static com.craivet.utils.Global.*;

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
