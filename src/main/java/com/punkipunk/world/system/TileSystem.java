package com.punkipunk.world.system;

import com.punkipunk.gfx.Renderer2D;
import com.punkipunk.gfx.opengl.Texture;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.*;

public record TileSystem(World world) {

    /**
     * Renders the tiles within the camera view by applying offsets to each one.
     */
    public void render(Renderer2D renderer) {

        // TODO It could be calculated from a Camera class
        // Calculate the offsets
        int xOffset = world.entitySystem.player.position.x - X_OFFSET;
        int yOffset = world.entitySystem.player.position.y - Y_OFFSET;

        // Calculates the tiles that are within the camera view
        int yStart = Math.max(0, yOffset / tile);
        int yEnd = Math.min(MAX_MAP_ROW, (yOffset + WINDOW_HEIGHT) / tile + 1);
        int xStart = Math.max(0, xOffset / tile);
        int xEnd = Math.min(MAX_MAP_COL, (xOffset + WINDOW_WIDTH) / tile + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                int tileIndex = world.map.tileIndex[world.map.id.ordinal()][y][x];
                Texture tileTexture = world.map.tiles.get(tileIndex).texture();
                renderer.drawImage(tileTexture, x * tile - xOffset, y * tile - yOffset);
                // renderer.setStroke(Color.GREEN);
                // renderer.strokeRect(x * tile - xOffset, y * tile - yOffset, tile, tile);
            }
        }

    }

}
