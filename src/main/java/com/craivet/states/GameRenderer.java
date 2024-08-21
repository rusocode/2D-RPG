package com.craivet.states;

import com.craivet.Game;
import com.craivet.UI;
import com.craivet.input.keyboard.Key;
import com.craivet.world.World;
import com.craivet.Minimap;

import java.awt.*;

import static com.craivet.utils.Global.*;

public class GameRenderer implements Renderable {

    private final Game game;
    private final World world;
    private final UI ui;
    private final Minimap minimap;

    private long renderStart;
    private int lastFrames;

    public GameRenderer(Game game, World world, UI ui, Minimap minimap) {
        this.game = game;
        this.world = world;
        this.ui = ui;
        this.minimap = minimap;
    }

    public void update() {
        world.update();
    }

    public void render(Graphics2D g2) {
        if (game.keyboard.isKeyToggled(Key.DEBUG)) renderStart = System.nanoTime();

        world.render(g2);
        minimap.render(g2);
        ui.render(g2);

        if (game.keyboard.isKeyToggled(Key.TEST)) {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 17));
            String text = "Test Mode";
            int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            int x = WINDOW_WIDTH / 2 - textLength / 2;
            int y = 25;
            g2.drawString(text, x, y);
        }

        if (game.keyboard.isKeyToggled(Key.DEBUG)) {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 8));
            int x = 8, y = (int) (WINDOW_HEIGHT - tile * 2.8), gap = 15;
            String map = game.file.maps.get(game.world.map.num);
            int posX = (game.world.entities.player.pos.x + game.world.entities.player.hitbox.x) / tile;
            int posY = (game.world.entities.player.pos.y + game.world.entities.player.hitbox.y) / tile;
            g2.drawString(map + " (" + posX + ", " + posY + ")", x, y);
            y += gap;
            g2.drawString("x: " + (game.world.entities.player.pos.x + game.world.entities.player.hitbox.x) + " y: " + (game.world.entities.player.pos.y + game.world.entities.player.hitbox.y), x, y);
            y += gap;
            if (game.loop.showFPS) {
                g2.drawString("FPS: " + game.loop.framesInRender, x, y);
                lastFrames = game.loop.framesInRender;
                game.loop.showFPS = false;
            } else
                g2.drawString("FPS: " + lastFrames, x, y); // Shows the last fps until the second is completed
            y += gap;
            g2.drawString("Render time: " + (System.nanoTime() - renderStart) / 1_000_000 + " ms", x, y);
        }

    }

}
