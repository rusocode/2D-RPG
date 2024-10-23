package com.punkipunk.engine.setup;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.FontAssets;
import com.punkipunk.controllers.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class GameSetupConfiguration {

    private final Game game;
    private final GameController gameController;

    public GameSetupConfiguration(Game game, GameController gameController) {
        this.game = game;
        this.gameController = gameController;
    }

    public void configure() {
        Font.loadFont(getClass().getResourceAsStream("/font/BlackPearl.ttf"), 18);
        configureRendering();
        configureStats();
        configureInput();
    }

    private void configureRendering() {
        Canvas canvas = gameController.getCanvas();
        GraphicsContext context = canvas.getGraphicsContext2D();
        game.setRenderingComponents(canvas, context);
    }

    private void configureStats() {
        game.systems.updater.setStatsController(gameController.getStatsViewController());
    }

    private void configureInput() {
    }

}
