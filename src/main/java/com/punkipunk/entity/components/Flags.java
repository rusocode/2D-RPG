package com.punkipunk.entity.components;

/**
 * <p>
 * Los "flags" son variables o bits individuales que se utilizan como indicadores booleanos para representar y controlar estados o
 * condiciones especificas dentro del juego.
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

    public void reset() {
        hitting = false;
        invincible = false;
        knockback = false;
    }

}
