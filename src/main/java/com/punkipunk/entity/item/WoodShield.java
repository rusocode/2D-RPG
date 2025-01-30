package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class WoodShield extends Item {

    public WoodShield(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public ItemID getID() {
        return ItemID.WOOD_SHIELD;
    }

}
