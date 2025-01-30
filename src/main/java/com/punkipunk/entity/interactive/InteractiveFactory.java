package com.punkipunk.entity.interactive;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class InteractiveFactory {

    private final Game game;
    private final World world;

    public InteractiveFactory(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public Interactive createEntity(InteractiveID id, int... pos) {
        return switch (id) {
            case DESTRUCTIBLE_WALL -> new DestructibleWall(game, world, pos);
            case DRY_TREE -> new DryTree(game, world, pos);
            case METAL_PLATE -> new MetalPlate(game, world, pos);
        };
    }

}
