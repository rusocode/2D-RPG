package com.punkipunk;

import com.punkipunk.controllers.GameController;
import com.punkipunk.core.Game;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class GameBuilder {

    private Scene scene;
    private GameController gameController;
    private Canvas canvas;
    private GraphicsContext context;

    public GameBuilder withScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    public GameBuilder withController(GameController gameController) {
        this.gameController = gameController;
        return this;
    }

    public GameBuilder withFont(String fontPath, int fontSize) {
        Font.loadFont(getClass().getResourceAsStream(fontPath), fontSize);
        return this;
    }

    public GameBuilder withRenderingComponents(GameController gameController) {
        canvas = gameController.getCanvas();
        context = canvas.getGraphicsContext2D();
        return this;
    }

    public Game build() {
        return new Game(scene, gameController, canvas, context);
    }

}
