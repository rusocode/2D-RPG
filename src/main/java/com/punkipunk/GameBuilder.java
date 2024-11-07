package com.punkipunk;

import com.punkipunk.controllers.GameController;
import javafx.scene.Scene;

public class GameBuilder {

    private Scene gameScene;
    private GameController controller;

    public GameBuilder withScene(Scene scene) {
        this.gameScene = scene;
        return this;
    }

    public GameBuilder withController(GameController controller) {
        this.controller = controller;
        return this;
    }

    public Game build() {
        Game game = new Game(gameScene, controller);
        new GameSetup(game, controller).setup();
        return game;
    }

}
