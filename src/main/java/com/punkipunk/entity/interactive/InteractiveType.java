package com.punkipunk.entity.interactive;


public enum InteractiveType {

    DESTRUCTIBLE_WALL("destructibleWall"),
    DRY_TREE("dryTree"),
    METAL_PLATE("metalPlate");

    private final String name;

    InteractiveType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
