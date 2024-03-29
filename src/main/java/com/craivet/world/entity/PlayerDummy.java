package com.craivet.world.entity;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.mob.Mob;

import static com.craivet.gfx.Assets.*;

/**
 * Fictional character that serves to represent the boss scene. That is to say, it would be the player who remains still at the
 * boss's entrance until phase 3 ends and phase 4 begins.
 */

public class PlayerDummy extends Mob {

    public static final String NAME = "Dummy";

    public PlayerDummy(Game game, World world) {
        super(game, world);
        stats.name = NAME;
        sheet.loadPlayerMovementFrames(player, 1);
    }

}
