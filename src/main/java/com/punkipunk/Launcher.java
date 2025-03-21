package com.punkipunk;

import com.punkipunk.scene.SceneDirector;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneDirector.getInstance().initialize(stage); // Inicializa el SceneManager con el stage principal
        stage.setScene(SceneDirector.getInstance().createMainScene());
        WindowManager windowManager = new WindowManager(stage, SceneDirector.getInstance().game);
        windowManager.configureWindow("2D-RPG");
        windowManager.setOnCloseRequest();
        stage.show();
    }

}