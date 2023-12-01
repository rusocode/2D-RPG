package com.craivet;

import com.craivet.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class Minimap {

    private final Game game;
    private final World world;
    private BufferedImage[] minimap;

    public Minimap(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public void create() {
        minimap = new BufferedImage[MAPS];
        int width = tile * MAX_MAP_COL;
        int height = tile * MAX_MAP_ROW;
        for (int map = 0; map < MAPS; map++) {
            minimap[map] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = minimap[map].createGraphics();
            for (int row = 0; row < MAX_MAP_ROW; row++) {
                for (int col = 0; col < MAX_MAP_COL; col++) {
                    int tileIndex = world.map.tileIndex[map][row][col];
                    int x = tile * col;
                    int y = tile * row;
                    g2.drawImage(world.map.tileData[tileIndex].texture, x, y, null);
                }
            }
            g2.dispose();
        }
    }

    public void render(final Graphics2D g2) {
        if (game.keyboard.minimap) {
            int width = 100;
            int height = 100;
            int x = WINDOW_WIDTH - width - 22;
            int y = 15;
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(0));
            g2.drawRect(x - 1, y - 1, width + 1, height + 1);
            g2.drawImage(minimap[world.map.num], x, y, width, height, null);

            // Draw a red square that represents the player's position
            double scale = (double) (tile * MAX_MAP_COL) / width;
            int playerX = (int) (x + (world.entities.player.pos.x + world.entities.player.hitbox.x) / scale);
            int playerY = (int) (y + (world.entities.player.pos.y + world.entities.player.hitbox.y) / scale);
            int playerSize = (int) (tile / scale);
            g2.setColor(Color.red);
            g2.fillRect(playerX, playerY, playerSize, playerSize);
        }
    }

}
