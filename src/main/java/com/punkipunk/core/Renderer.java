package com.punkipunk.core;

import com.punkipunk.Minimap;
import com.punkipunk.UI;
import com.punkipunk.core.api.Renderable;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.world.World;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.lang.System;

import static com.punkipunk.utils.Global.*;

public class Renderer implements Renderable {

    public final World world;
    private final Game game;
    private final UI ui;
    private final Minimap minimap;
    private long renderStart;
    private int lastFrames;

    public Renderer(Game game, World world, UI ui, Minimap minimap) {
        this.game = game;
        this.world = world;
        this.ui = ui;
        this.minimap = minimap;
    }

    @Override
    public void render(GraphicsContext context) {
        if (game.system.keyboard.isKeyToggled(Key.DEBUG)) renderStart = System.nanoTime();

        world.render(context);
        minimap.render(context);
        ui.render(context);

        if (game.system.keyboard.isKeyToggled(Key.TEST)) {

            context.setFont(Font.font(context.getFont().getFamily(), 24));
            // g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 17));

            String text = "Test Mode";
            Text theText = new Text(text);
            theText.setFont(context.getFont());
            double textLength = theText.getLayoutBounds().getWidth();
            // String text = "Test Mode";
            // int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

            double x = (double) WINDOW_WIDTH / 2 - textLength / 2;
            double y = 25;
            /* En AWT/Swing, la coordenada Y es la linea base del texto. En JavaFX, por defecto, es la parte superior del texto.
             * Para que se comporte de manera similar a drawString(), necesitas ajustar la coordenada Y. */
            context.setTextBaseline(VPos.BASELINE);
            context.fillText(text, x, y);

        }

        if (game.system.keyboard.isKeyToggled(Key.DEBUG)) {
            context.setFont(Font.font(context.getFont().getFamily(), FontWeight.NORMAL, 8));
            int x = 8, y = (int) (WINDOW_HEIGHT - tile * 2.8), gap = 15;
            String map = game.system.file.maps.get(game.system.world.map.num);
            int posX = (int) ((game.system.world.entities.player.pos.x + game.system.world.entities.player.hitbox.getX()) / tile);
            int posY = (int) ((game.system.world.entities.player.pos.y + game.system.world.entities.player.hitbox.getY()) / tile);
            context.setTextBaseline(VPos.BASELINE);
            context.fillText(map + " (" + posX + ", " + posY + ")", x, y);
            y += gap;
            context.fillText("x: " + (game.system.world.entities.player.pos.x + game.system.world.entities.player.hitbox.getX()) + " y: " + (game.system.world.entities.player.pos.y + game.system.world.entities.player.hitbox.getY()), x, y);
            y += gap;
            if (game.system.oldGameLoop.showFPS) {
                context.fillText("FPS: " + game.system.oldGameLoop.framesInRender, x, y);
                lastFrames = game.system.oldGameLoop.framesInRender;
                game.system.oldGameLoop.showFPS = false;
            } else context.fillText("FPS: " + lastFrames, x, y); // Shows the last fps until the second is completed
            y += gap;
            context.fillText("Render time: " + (System.nanoTime() - renderStart) / 1_000_000 + " ms", x, y);
        }

    }

}
