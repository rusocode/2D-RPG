package com.punkipunk.controllers;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.GameBuilder;
import com.punkipunk.scene.GameViewLoader;
import com.punkipunk.scene.SceneDirector;
import com.punkipunk.states.State;
import javafx.fxml.FXML;
import javafx.scene.Scene;

/**
 * Controla la logica de la pantalla principal, gestionando la transicion a la escena del juego.
 */

public class MainController {

    private final SceneDirector sceneDirector = SceneDirector.getInstance();
    private Game game;
    private Scene gameScene;
    private boolean isInitialGame = true;

    public void initialize(Game game, Scene gameScene) {
        this.game = game;
        this.gameScene = gameScene;
    }

    @FXML
    private void handleNewGameClicked() {
        if (isInitialGame) initGame();
        else createGame();
        isInitialGame = false;
    }

    @FXML
    private void handleLoadGameClicked() {
        sceneDirector.switchScene(sceneDirector.getGameScene());
        game.gameSystem.file.loadData();
        game.gameSystem.audio.playSound(AudioID.Sound.SPAWN2);
        game.gameSystem.audio.playZoneAmbient();
        game.start();
        State.setState(State.PLAY);
    }

    /**
     * Abandona el juego.
     */
    @FXML
    private void handleQuitClicked() {
        game.gameSystem.audio.playSound(AudioID.Sound.CLICK2);
        System.exit(0);
    }

    /**
     * Maneja los elemento de menu al pasar el mouse.
     */
    @FXML
    private void handleMenuItemHovered() {
        game.gameSystem.audio.playSound(AudioID.Sound.HOVER);
    }

    /**
     * Inicia el juego utilizando las instancias creadas en SceneManager.
     */
    private void initGame() {
        sceneDirector.switchScene(gameScene);
        startGame(game);
    }

    /**
     * Crea el juego por segunda vez, o tercera, etc. (por ejemplo, cuando vuelve al menu principal desde opciones ya en el
     * juego).
     */
    private void createGame() {
        GameViewLoader gameViewLoader = new GameViewLoader();
        Scene gameScene = sceneDirector.createGameScene(gameViewLoader);
        GameController gameController = gameViewLoader.getController();

        Game game = new GameBuilder()
                .withScene(gameScene)
                .withController(gameController)
                .withFont("/font/BlackPearl.ttf", 18)
                .withRenderingComponents(gameController)
                .build();

        game.setup();

        sceneDirector.switchScene(gameScene);
        startGame(game);
    }

    private void startGame(Game game) {
        game.gameSystem.audio.stopAll();
        game.gameSystem.audio.playSound(AudioID.Sound.SPAWN2);
        game.gameSystem.audio.playZoneAmbient();
        game.start();
        State.setState(State.PLAY);
    }

}
