package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class IronShield extends Item {

    public static final String NAME = "Iron Shield";

    public IronShield(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    protected ItemType getType() {
        return ItemType.IRON_SHIELD;
    }

}
