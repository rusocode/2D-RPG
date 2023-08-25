package com.craivet.states;

import com.craivet.Game;
import com.craivet.UI;
import com.craivet.world.World;
import com.craivet.Minimap;

import java.awt.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class GameState implements State {

    private final Game game;
    private final World world;
    private final UI ui;
    private final Minimap minimap;

    private long renderStart;
    private int lastFrames;

    public GameState(Game game, World world, UI ui, Minimap minimap) {
        this.game = game;
        this.world = world;
        this.ui = ui;
        this.minimap = minimap;
    }

    public void update() {
        world.update();
    }

    public void render(Graphics2D g2) {
        if (game.keyboard.t) renderStart = System.nanoTime();

        world.render(g2);
        minimap.render(g2);
        ui.render(g2);

        // Debug mode
        if (game.keyboard.t) {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 8));
            int x = 8, y = (int) (SCREEN_HEIGHT - tile_size * 2.3), gap = 25;
            String map = game.world.maps.get(game.world.map);
            int posX = (game.world.player.x + game.world.player.hitbox.x) / tile_size;
            int posY = (game.world.player.y + game.world.player.hitbox.y) / tile_size;
            g2.drawString(map + " (" + posX + ", " + posY + ")", x, y);
            y += gap;
            if (game.gameTimer.showFPS) {
                g2.drawString("FPS: " + game.gameTimer.framesInRender, x, y);
                lastFrames = game.gameTimer.framesInRender;
                game.gameTimer.showFPS = false;
            } else
                g2.drawString("FPS: " + lastFrames, x, y); // Muestra los ultimos fps hasta que se complete el segundo
            y += gap;
            g2.drawString("Draw time: " + (System.nanoTime() - renderStart) / 1_000_000 + " ms", x, y);
        }
    }

}
