package com.punkipunk.controllers;

import com.punkipunk.Game;
import com.punkipunk.GameBuilder;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
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
        game.system.file.loadData();
        game.system.audioManager.playSpawnSound();
        game.system.audioManager.playZoneAmbient();
        game.start();
        State.setState(State.PLAY);
    }

    /**
     * Abandona el juego.
     */
    @FXML
    private void handleQuitClicked() {
        game.system.audioManager.playClickSound();
        System.exit(0);
    }

    /**
     * Maneja los elemento de menu al pasar el mouse.
     */
    @FXML
    private void handleMenuItemHovered() {
        game.system.audioManager.playHoverSound();
    }

    /**
     * Inicia el juego utilizando las instancias creadas en SceneManager.
     */
    private void initGame() {
        sceneManager.switchScene(gameScene);
        startGame(game);
    }

    /**
     * Crea el juego por segunda vez, o tercera, etc. (por ejemplo, cuando vuelve al menu principal desde opciones ya en el
     * juego).
     */
    private void createGame() {
        GameViewLoader gameViewLoader = new GameViewLoader();
        Scene gameScene = sceneManager.createGameScene(gameViewLoader);
        GameController gameController = gameViewLoader.getController();

        Game game = new GameBuilder()
                .withScene(gameScene)
                .withController(gameController)
                .withFont("/font/BlackPearl.ttf", 18)
                .withRenderingComponents(gameController)
                .build();

        game.createSystem();
        game.initializeSystem();
        game.playMusic(Assets.getAudio(AudioAssets.MUSIC_MAIN));
        game.setup();

        sceneManager.switchScene(gameScene);
        startGame(game);
    }

    private void startGame(Game game) {
        game.system.audioManager.playSpawnSound();
        game.system.audioManager.stopMusic();
        game.system.audioManager.playZoneAmbient();
        game.start();
        State.setState(State.PLAY);
    }

}
