package com.punkipunk;

import com.punkipunk.managers.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.punkipunk.utils.Global.VERSION;

/**
 * <h3>Notes</h3>
 * # This program depends on the CPU for rendering, so the graphical performance will be weaker than that of the games that use
 * GPU. To use the GPU, we need to take a step forward and access OpenGL.
 * <p>
 * # Rendering with Canvas seems to be more powerful unlike JPanel with respect to the amount of FPS.
 */

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneManager.getInstance().initialize(stage); // Inicializa el SceneManager con el stage principal
        WindowManager windowManager = new WindowManager(stage);
        windowManager.configureWindow("2D-RPG " + VERSION);
        windowManager.setOnCloseRequest();
        stage.setScene(SceneManager.getInstance().createMainScene());
        stage.show();
    }

}