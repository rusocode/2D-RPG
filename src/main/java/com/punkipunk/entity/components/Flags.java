package com.punkipunk.entity.components;

/**
 * <p>
 * "Flags" are variables or individual bits used as boolean indicators to represent and control specific state or conditions
 * within the game.
 */

public final class Flags {

    public boolean hitting;
    public boolean alive = true;
    public boolean colliding;
    public boolean collidingOnMob;
    public boolean dead;
    public boolean invincible;
    public boolean knockback;
    public boolean following;
    public boolean shooting;
    public boolean hpBar;
    public boolean boss;

    /**
     * Reset the flags.
     */
    public void reset() {
        hitting = false;
        invincible = false;
        knockback = false;
    }

}
