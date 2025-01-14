package com.punkipunk.world.management;

import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static com.punkipunk.utils.Global.*;

public class TileManager {

    private final World world;

    public TileManager(World world) {
        this.world = world;
    }


    /**
     * Renders the tiles within the camera view by applying offsets to each one.
     */
    public void render(GraphicsContext context) {

        // TODO It could be calculated from a Camera class
        // Calculate the offsets
        int xOffset = world.entities.player.position.x - X_OFFSET;
        int yOffset = world.entities.player.position.y - Y_OFFSET;

        // Calculates the tiles that are within the camera view
        int yStart = Math.max(0, yOffset / tile);
        int yEnd = Math.min(MAX_MAP_ROW, (yOffset + WINDOW_HEIGHT) / tile + 1);
        int xStart = Math.max(0, xOffset / tile);
        int xEnd = Math.min(MAX_MAP_COL, (xOffset + WINDOW_WIDTH) / tile + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                final int tileIndex = world.map.tileIndex[world.map.num][y][x];
                final Image tileImage = world.map.tileData[tileIndex].texture;
                context.drawImage(tileImage, x * tile - xOffset, y * tile - yOffset);
                // context.setStroke(Color.GREEN);
                // context.strokeRect(x * tile - xOffset, y * tile - yOffset, tile, tile);
            }
        }

    }

}
