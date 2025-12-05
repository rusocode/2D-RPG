package com.punkipunk.entity.interactive;

import com.punkipunk.core.IGame;
import com.punkipunk.world.World;

public class InteractiveFactory {

    private final IGame game;
    private final World world;

    public InteractiveFactory(IGame game, World world) {
        this.game = game;
        this.world = world;
    }

    public Interactive create(InteractiveID id, int... pos) {
        return switch (id) {
            case DESTRUCTIBLE_WALL -> new DestructibleWall(game, world, pos);
            case DRY_TREE -> new DryTree(game, world, pos);
            case METAL_PLATE -> new MetalPlate(game, world, pos);
        };
    }

}
