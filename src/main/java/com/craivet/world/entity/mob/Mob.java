package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;

/**
 * Los mobs se dividen en
 * <ul>
 * <li>PLAYER: jugador principal.
 * <li>NPC: entidades con las que se interactura, comercia, etc.
 * <li>HOSTILE: son entidades agresivas que pueden atacar y ser atacadas.
 * </ul>
 * <p>
 * TODO Es posible crear el enum aca?
 */

public class Mob extends Entity {

    public Mob(Game game, World world, int x, int y) {
        super(game, world, x, y);
    }

    /**
     * Mueve al Mob.
     *
     * @param direction direccion hacia donde se mueve el Mob.
     */
    protected void move(int direction) {
    }

    public void speak() {
    }

    public void damageReaction() {
    }

}
