package com.punkipunk.entity.interactive;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class MetalPlate extends Interactive {

    public MetalPlate(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public InteractiveID getID() {
        return InteractiveID.METAL_PLATE;
    }

}
