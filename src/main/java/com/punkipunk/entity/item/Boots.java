package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Boots extends Item {

    public Boots(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public ItemType getType() {
        return ItemType.BOOTS;
    }

}
