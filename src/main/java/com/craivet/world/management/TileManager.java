package com.craivet.world.management;

import com.craivet.world.World;
import com.craivet.states.State;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class TileManager implements State {

    private final World world;

    public TileManager(World world) {
        this.world = world;
    }

    @Override
    public void update() {

    }

    /**
     * Renderiza los tiles dentro de la vista de la camara aplicando los desplazamientos a cada uno.
     *
     * @param g2 componente grafico.
     */
    @Override
    public void render(Graphics2D g2) {
        // TODO Se podria calcular desde una clase Camera
        // Calcula los desplazamientos
        int xOffset = world.player.pos.x - world.player.stats.screenX;
        int yOffset = world.player.pos.y - world.player.stats.screenY;

        // Calcula los tiles que estan dentro de la vista de la camara
        int yStart = Math.max(0, yOffset / tile);
        int yEnd = Math.min(MAX_MAP_ROW, (yOffset + WINDOW_HEIGHT) / tile + 1);
        int xStart = Math.max(0, xOffset / tile);
        int xEnd = Math.min(MAX_MAP_COL, (xOffset + WINDOW_WIDTH) / tile + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                final int tileIndex = world.tileIndex[world.map][y][x];
                final BufferedImage tileImage = world.tileData[tileIndex].texture;
                g2.drawImage(tileImage, x * tile - xOffset, y * tile - yOffset, null);
                // g2.setStroke(new BasicStroke(0)); // Anula el grosor del borde para mantenerlo
                // g2.drawRect(x * tile - xOffset, y * tile - yOffset, tile, tile); // Dibuja una grilla
            }
        }
    }

}
