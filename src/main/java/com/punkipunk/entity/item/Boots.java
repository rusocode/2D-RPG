package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.world.World;

public class Boots extends Item {

    public Boots(IGame game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public ItemID getID() {
        return ItemID.BOOTS;
    }

}
