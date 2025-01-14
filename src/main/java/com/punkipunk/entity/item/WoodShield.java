package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class WoodShield extends Item {

    public static final String NAME = "Wood Shield";

    public WoodShield(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    protected ItemType getType() {
        return ItemType.WOOD_SHIELD;
    }

}
