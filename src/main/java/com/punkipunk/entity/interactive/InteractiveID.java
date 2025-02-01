package com.punkipunk.entity.interactive;


public enum InteractiveID {

    DESTRUCTIBLE_WALL("destructibleWall"),
    DRY_TREE("dryTree"),
    METAL_PLATE("metalPlate");

    public final String name;

    InteractiveID(String name) {
        this.name = name;
    }

}
