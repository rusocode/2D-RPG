package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Boots extends Item {

    public static final String NAME = "Boots";

    public Boots(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    protected ItemType getType() {
        return ItemType.BOOTS;
    }

}
