package com.craivet.states;

import com.craivet.Game;
import com.craivet.UI;
import com.craivet.world.World;
import com.craivet.Minimap;

import java.awt.*;

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
            int x = 8, y = (int) (WINDOW_HEIGHT - tile * 3.2), gap = 15;
            String map = game.world.maps.get(game.world.map);
            int posX = (game.world.player.pos.x + game.world.player.stats.hitbox.x) / tile;
            int posY = (game.world.player.pos.y + game.world.player.stats.hitbox.y) / tile;
            g2.drawString("God Mode: " + game.keyboard.godMode, x, y);
            y += gap;
            g2.drawString(map + " (" + posX + ", " + posY + ")", x, y);
            y += gap;
            g2.drawString("x: " + (game.world.player.pos.x + game.world.player.stats.hitbox.x) + " y: " + (game.world.player.pos.y + game.world.player.stats.hitbox.y), x, y);
            y += gap;
            if (game.gameTimer.showFPS) {
                g2.drawString("FPS: " + game.gameTimer.framesInRender, x, y);
                lastFrames = game.gameTimer.framesInRender;
                game.gameTimer.showFPS = false;
            } else
                g2.drawString("FPS: " + lastFrames, x, y); // Muestra los ultimos fps hasta que se complete el segundo
            y += gap;
            g2.drawString("Render time: " + (System.nanoTime() - renderStart) / 1_000_000 + " ms", x, y);
        }
    }

}
