package com.punkipunk.world.entity;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.mob.Mob;

/**
 * Fictional character that serves to represent the boss scene. That is to say, it would be the player who remains still at the
 * boss's entrance until phase 3 ends and phase 4 begins.
 */

public class PlayerDummy extends Mob {

    public static final String NAME = "Dummy";

    public PlayerDummy(Game game, World world) {
        super(game, world);
        stats.name = NAME;
        sheet.loadPlayerMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.PLAYER), 1);
    }

}
