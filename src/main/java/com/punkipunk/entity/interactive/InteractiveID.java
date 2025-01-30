package com.punkipunk.entity.interactive;


public enum InteractiveID {

    DESTRUCTIBLE_WALL("destructibleWall"),
    DRY_TREE("dryTree"),
    METAL_PLATE("metalPlate");

    private final String name;

    InteractiveID(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
