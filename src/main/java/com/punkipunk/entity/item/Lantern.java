package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Lantern extends Item {

    public Lantern(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public ItemType getType() {
        return ItemType.LANTERN;
    }

}
