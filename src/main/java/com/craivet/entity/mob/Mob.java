package com.craivet.entity.mob;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;

public class Mob extends Entity {

    public static final int HOSTIL = 1;

    public Mob(Game game, World world, int x, int y) {
        super(game, world, x, y);
    }

}
