package com.craivet.world.management;

import com.craivet.world.World;
import com.craivet.states.Renderable;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class TileManager implements Renderable {

    private final World world;

    public TileManager(World world) {
        this.world = world;
    }

    @Override
    public void update() {

    }

    /**
     * Renders the tiles within the camera view by applying offsets to each one.
     *
     * @param g2 graphic component.
     */
    @Override
    public void render(Graphics2D g2) {

        // TODO It could be calculated from a Camera class
        // Calculate the offsets
        int xOffset = world.entities.player.pos.x - world.entities.player.screen.xOffset;
        int yOffset = world.entities.player.pos.y - world.entities.player.screen.yOffset;

        // Calculates the tiles that are within the camera view
        int yStart = Math.max(0, yOffset / tile);
        int yEnd = Math.min(MAX_MAP_ROW, (yOffset + WINDOW_HEIGHT) / tile + 1);
        int xStart = Math.max(0, xOffset / tile);
        int xEnd = Math.min(MAX_MAP_COL, (xOffset + WINDOW_WIDTH) / tile + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                final int tileIndex = world.map.tileIndex[world.map.num][y][x];
                final BufferedImage tileImage = world.map.tileData[tileIndex].texture;
                g2.drawImage(tileImage, x * tile - xOffset, y * tile - yOffset, null);
                // g2.setStroke(new BasicStroke(0)); // Anula el grosor del borde para mantenerlo
                // g2.drawRect(x * tile - xOffset, y * tile - yOffset, tile, tile); // Dibuja una grilla
            }
        }

    }

}
