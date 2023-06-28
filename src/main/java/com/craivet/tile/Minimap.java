package com.craivet.tile;

import com.craivet.World;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class Minimap {

    private final World world;
    private BufferedImage[] minimap;
    public boolean miniMapOn;

    public Minimap(World world) {
        this.world = world;
    }

    public void createMinimap() {
        minimap = new BufferedImage[MAX_MAP];
        int width = tile_size * MAX_MAP_COL;
        int height = tile_size * MAX_MAP_ROW;
        for (int map = 0; map < MAX_MAP; map++) {
            minimap[map] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = minimap[map].createGraphics();
            for (int row = 0; row < MAX_MAP_ROW; row++) {
                for (int col = 0; col < MAX_MAP_COL; col++) {
                    int tileIndex = world.tileIndex[map][row][col];
                    int x = tile_size * col;
                    int y = tile_size * row;
                    g2.drawImage(world.tileData[tileIndex].texture, x, y, null);
                }
            }
            g2.dispose();
        }
    }

    public void render(final Graphics2D g2) {
        if (miniMapOn) {
            int width = 200;
            int height = 200;
            int x = SCREEN_WIDTH - width - 20;
            int y = 20;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
            g2.setColor(Color.black);
            g2.drawRect(x - 1, y - 1, width + 1, height + 1);
            g2.drawImage(minimap[world.map], x, y, width, height, null);

            // Dibuja un cuadrado rojo que representa la posicion del player
            double scale = (double) (tile_size * MAX_MAP_COL) / width;
            int playerX = (int) (x + world.player.x / scale);
            int playerY = (int) (y + world.player.y / scale);
            int playerSize = (int) (tile_size / scale);
            g2.setColor(Color.red);
            g2.fillRect(playerX, playerY, playerSize, playerSize);
            // g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Restablece el valor alpha
        }
    }

}
