package com.punkipunk;

import com.punkipunk.scene.SceneDirector;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.punkipunk.utils.Global.VERSION;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneDirector.getInstance().initialize(stage); // Inicializa el SceneManager con el stage principal
        WindowManager windowManager = new WindowManager(stage);
        windowManager.configureWindow("2D-RPG " + VERSION);
        windowManager.setOnCloseRequest();
        stage.setScene(SceneDirector.getInstance().createMainScene());
        stage.show();
    }

}