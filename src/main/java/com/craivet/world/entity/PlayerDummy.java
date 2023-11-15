package com.craivet.world.entity;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.mob.Mob;

import static com.craivet.gfx.Assets.*;

public class PlayerDummy extends Mob {

    public static final String NAME = "Dummy";

    public PlayerDummy(Game game, World world) {
        super(game, world);
        stats.name = NAME;
        sheet.loadPlayerMovementFrames(player_movement, 1);
    }

}
