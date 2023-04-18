package com.craivet.states;

import com.craivet.Game;
import com.craivet.UI;
import com.craivet.World;
import com.craivet.tile.Minimap;

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
        this.minimap =  minimap;
        game.state = PLAY_STATE;
    }

    public void update() {
        world.update();
    }

    public void render(Graphics2D g2) {
        if (game.getKey().t) renderStart = System.nanoTime();

        world.render(g2);
        minimap.render(g2);
        ui.render(g2);

        // Debug mode
        if (game.getKey().t) {
            g2.setFont(new Font("Consolas", Font.ITALIC, 20));
            g2.setColor(Color.white);
            int x = 8, y = SCREEN_HEIGHT - 55, gap = 25;
            String map = game.getWorld().maps.get(game.getWorld().map);
            int posX = (game.getWorld().player.x + game.getWorld().player.hitbox.x) / tile_size;
            int posY = (game.getWorld().player.y + game.getWorld().player.hitbox.y) / tile_size;
            g2.drawString(map + " (" + posX + ", " + posY + ")", x, y);
            y += gap;
            if (game.showFPS) {
                g2.drawString("FPS: " + game.framesInRender, x, y);
                lastFrames = game.framesInRender;
                game.showFPS = false;
            } else
                g2.drawString("FPS: " + lastFrames, x, y); // Muestra los ultimos fps hasta que se complete el segundo
            y += gap;
            g2.drawString("Draw time: " + (System.nanoTime() - renderStart) / 1_000_000 + " ms", x, y);
        }
    }

}
