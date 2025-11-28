package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.world.World;

public class Lantern extends Item {

    public Lantern(IGame game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public ItemID getID() {
        return ItemID.LANTERN;
    }

}
