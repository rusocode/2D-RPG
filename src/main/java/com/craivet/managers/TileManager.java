package com.craivet.managers;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.states.State;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class TileManager implements State {

    private final Game game;
    private final World world;

    public TileManager(Game game, World world) {
        this.game = game;
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
        // Calcula los desplazamientos
        int xOffset = world.player.x - world.player.screenX; // 1104 - 456 = 648
        int yOffset = world.player.y - world.player.screenY;

        // Calcula los tiles que estan dentro de la vista de la camara
        int yStart = Math.max(0, yOffset / tile_size);
        int yEnd = Math.min(MAX_MAP_ROW, (yOffset + SCREEN_HEIGHT) / tile_size + 1);
        int xStart = Math.max(0, xOffset / tile_size); // 648 / 48 = 13
        int xEnd = Math.min(MAX_MAP_COL, (xOffset + SCREEN_WIDTH) / tile_size + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                final int tileIndex = world.tileIndex[world.map][y][x];
                final BufferedImage tileImage = world.tileData[tileIndex].texture;
                g2.drawImage(tileImage, x * tile_size - xOffset, y * tile_size - yOffset, null);
                g2.drawRect(x * tile_size - xOffset, y * tile_size - yOffset, tile_size, tile_size); // Dibuja una grilla
            }
        }

        if (world.drawPath) {
            g2.setColor(new Color(255, 0, 0, 70));
            for (int i = 0; i < game.aStar.pathList.size(); i++) {
                int worldX = game.aStar.pathList.get(i).col * tile_size;
                int worldY = game.aStar.pathList.get(i).row * tile_size;
                int screenX = worldX - world.player.x + world.player.screenX;
                int screenY = worldY - world.player.y + world.player.screenY;
                g2.fillRect(screenX, screenY, tile_size, tile_size);
            }
        }
    }

}
