package com.punkipunk.managers;

import com.punkipunk.Game;
import com.punkipunk.GameBuilder;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.controllers.GameController;
import com.punkipunk.loaders.GameViewLoader;
import com.punkipunk.loaders.MainViewLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.punkipunk.utils.Global.WINDOW_HEIGHT;
import static com.punkipunk.utils.Global.WINDOW_WIDTH;

public class SceneManager {

    private static SceneManager instance;

    private Stage stage;
    private Scene mainScene;
    private Scene gameScene;

    private GameController gameController;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    public void initialize(Stage stage) {
        this.stage = stage;
    }

    public Scene createMainScene() {
        GameViewLoader gameViewLoader = new GameViewLoader();
        gameScene = createGameScene(gameViewLoader);
        gameController = gameViewLoader.getController();

        MainViewLoader mainViewLoader = new MainViewLoader();
        mainViewLoader.getController().initialize(createGame(), gameScene);
        mainScene = new Scene(mainViewLoader.getParent(), WINDOW_WIDTH, WINDOW_HEIGHT);
        return mainScene;
    }

    public Scene createGameScene(GameViewLoader gameViewLoader) {
        return new Scene(gameViewLoader.getParent(), WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private Game createGame() {
        Game game = new GameBuilder()
                .withScene(gameScene)
                .withController(gameController)
                .withFont("/font/BlackPearl.ttf", 18)
                .withRenderingComponents(gameController)
                .build();
        // Configura el juego una vez creado, obviamente!
        game.createSystem();
        game.initializeSystem();
        game.playMusic(Assets.getAudio(AudioAssets.MUSIC_MAIN));
        game.setup();
        return game;
    }

    public void switchScene(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public Scene getGameScene() {
        return gameScene;
    }

}
