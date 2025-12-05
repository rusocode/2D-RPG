package com.punkipunk.scene;

import com.punkipunk.GameBuilder;
import com.punkipunk.controllers.GameController;
import com.punkipunk.core.Game;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.punkipunk.utils.Global.WINDOW_HEIGHT;
import static com.punkipunk.utils.Global.WINDOW_WIDTH;

public class SceneDirector {

    private static SceneDirector instance;
    public Game game;
    private Stage stage;
    private Scene mainScene;
    private Scene gameScene;
    private GameController gameController;

    private SceneDirector() {
    }

    public static SceneDirector getInstance() {
        if (instance == null) instance = new SceneDirector();
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

    public void switchScene(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public Scene getGameScene() {
        return gameScene;
    }

    private Game createGame() {
        Game game = new GameBuilder()
                .withScene(gameScene)
                .withController(gameController)
                .withFont("/font/BlackPearl.ttf", 18)
                .withRenderingComponents(gameController)
                .build();
        game.setup(); // Configura el juego una vez creado, obviamente!
        this.game = game;
        return game;
    }


}
