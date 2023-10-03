package com.craivet;

import com.craivet.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class Minimap {

    private final World world;
    private BufferedImage[] minimap;
    public boolean minimapOn;

    public Minimap(World world) {
        this.world = world;
    }

    public void createMinimap() {
        minimap = new BufferedImage[MAPS];
        int width = tile * MAX_MAP_COL;
        int height = tile * MAX_MAP_ROW;
        for (int map = 0; map < MAPS; map++) {
            minimap[map] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = minimap[map].createGraphics();
            for (int row = 0; row < MAX_MAP_ROW; row++) {
                for (int col = 0; col < MAX_MAP_COL; col++) {
                    int tileIndex = world.tileIndex[map][row][col];
                    int x = tile * col;
                    int y = tile * row;
                    g2.drawImage(world.tileData[tileIndex].texture, x, y, null);
                }
            }
            g2.dispose();
        }
    }

    public void render(final Graphics2D g2) {
        if (minimapOn) {
            int width = 100;
            int height = 100;
            int x = WINDOW_WIDTH - width - 20;
            int y = 20;
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(0));
            g2.drawRect(x - 1, y - 1, width + 1, height + 1);
            g2.drawImage(minimap[world.map], x, y, width, height, null);

            // Draw a red square that represents the player's position
            double scale = (double) (tile * MAX_MAP_COL) / width;
            int playerX = (int) (x + (world.player.pos.x + world.player.hitbox.x) / scale);
            int playerY = (int) (y + (world.player.pos.y + world.player.hitbox.y) / scale);
            int playerSize = (int) (tile / scale);
            g2.setColor(Color.red);
            g2.fillRect(playerX, playerY, playerSize, playerSize);
        }
    }

}
