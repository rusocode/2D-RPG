package com.punkipunk.entity.item;

import com.punkipunk.core.IGame;
import com.punkipunk.world.World;

public class IronShield extends Item {

    public IronShield(IGame game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public ItemID getID() {
        return ItemID.IRON_SHIELD;
    }

}
