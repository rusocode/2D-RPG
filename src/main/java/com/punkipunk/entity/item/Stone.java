package com.punkipunk.entity.item;

import com.punkipunk.core.IGame;
import com.punkipunk.world.World;

public class Stone extends Item {

    public Stone(IGame game, World world, int amount, int... pos) {
        super(game, world, pos);
        this.amount = amount;
    }

    @Override
    public ItemID getID() {
        return ItemID.STONE;
    }

}
