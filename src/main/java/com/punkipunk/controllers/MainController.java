package com.punkipunk.controllers;

import com.punkipunk.Game;
import com.punkipunk.GameBuilder;
import com.punkipunk.loaders.GameViewLoader;
import com.punkipunk.managers.SceneManager;
import com.punkipunk.states.State;
import javafx.fxml.FXML;
import javafx.scene.Scene;

/**
 * Controla la logica de la pantalla principal, gestionando la transicion a la escena del juego.
 */

public class MainController {

    private final SceneManager sceneManager = SceneManager.getInstance();
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
        sceneManager.switchScene(sceneManager.getGameScene());
        game.systems.file.loadData();
        game.systems.audioManager.playSpawnSound();
        game.systems.audioManager.playZoneAmbient();
        game.start();
        State.setState(State.PLAY);
    }

    /**
     * Abandona el juego.
     */
    @FXML
    private void handleQuitClicked() {
        game.systems.audioManager.playClickSound();
        System.exit(0);
    }

    /**
     * Maneja los elemento de menu al pasar el mouse.
     */
    @FXML
    private void handleMenuItemHovered() {
        game.systems.audioManager.playHoverSound();
    }

    /**
     * Inicia el juego utilizando las instancias creadas en SceneManager.
     */
    private void initGame() {
        sceneManager.switchScene(gameScene);
        startGame(game);
    }

    /**
     * Crea el juego por segunda vez (por ejemplo, cuando vuelve al menu principal desde opciones ya en el juego).
     */
    private void createGame() {
        GameViewLoader gameViewLoader = new GameViewLoader();
        Scene gameScene = sceneManager.createGameScene(gameViewLoader);

        Game game = new GameBuilder()
                .withScene(gameScene)
                .withController(gameViewLoader.getController())
                .build();

        sceneManager.switchScene(gameScene);
        startGame(game);
    }

    private void startGame(Game game) {
        game.systems.audioManager.playSpawnSound();
        game.systems.audioManager.stopMusic();
        game.systems.audioManager.playZoneAmbient();
        game.start();
        State.setState(State.PLAY);
    }

}
