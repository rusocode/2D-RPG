package com.craivet.entity.mob;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;

public abstract class Mob extends Entity {

    public Mob(Game game, World world, int x, int y) {
        super(game, world, x, y);
    }

}
