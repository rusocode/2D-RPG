package com.craivet.world.entity;

/**
 * Las "flags" son variables o bits individuales utilizados como indicadores booleanos para representar y controlar el
 * estado o las condiciones especificas dentro del juego.
 *
 * @author Juan Debenedetti
 */

public final class Flags {

    public boolean hitting;
    public boolean alive = true;
    public boolean colliding;
    public boolean collidingOnMob;
    public boolean dead;
    public boolean invincible;
    public boolean knockback;
    public boolean onPath;
    public boolean shooting;

}
