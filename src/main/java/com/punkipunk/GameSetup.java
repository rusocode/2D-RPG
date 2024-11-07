package com.punkipunk;

import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.controllers.GameController;
import com.punkipunk.engine.core.Systems;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class GameSetup {

    private final Game game;
    private final GameController gameController;

    public GameSetup(Game game, GameController gameController) {
        this.game = game;
        this.gameController = gameController;
    }

    public void setup() {
        createSystems();
        initializeSystems();
        setupRendering();
        setupControllers();
        setupFont();
    }

    private void createSystems() {
        game.systems = new Systems(game);
    }

    private void initializeSystems() {
        game.systems.file.load();
        game.systems.minimap.create();
        game.systems.event.create();
        game.playMusic(Assets.getAudio(AudioAssets.MUSIC_MAIN));
    }

    private void setupRendering() {
        Canvas canvas = gameController.getCanvas();
        GraphicsContext context = canvas.getGraphicsContext2D();
        game.setRenderingComponents(canvas, context);
    }

    private void setupControllers() {
        gameController.setup(game);
        gameController.getStatsController().setGameController(gameController);
        game.systems.updater.setStatsController(gameController.getStatsController());
    }

    private void setupFont() {
        Font.loadFont(getClass().getResourceAsStream("/font/BlackPearl.ttf"), 18);
    }

}
