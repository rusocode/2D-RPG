package com.punkipunk.world;

import javafx.scene.image.Image;

public class Tile {

    public final Image texture;
    public final boolean solid;

    public Tile(Image texture, boolean solid) {
        this.texture = texture;
        this.solid = solid;
    }

}
