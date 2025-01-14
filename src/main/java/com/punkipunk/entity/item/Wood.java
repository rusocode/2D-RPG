package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Wood extends Item {

    public static final String NAME = "Wood";

    public Wood(Game game, World world, int amount, int... pos) {
        super(game, world, pos);
        this.amount = amount;
    }

    @Override
    protected ItemType getType() {
        return ItemType.WOOD;
    }

}