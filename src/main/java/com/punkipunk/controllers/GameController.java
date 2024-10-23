package com.punkipunk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

import static com.punkipunk.utils.Global.WINDOW_HEIGHT;
import static com.punkipunk.utils.Global.WINDOW_WIDTH;

/**
 * Controla la logica de la escena con respecto a todo lo que tiene que ver con los componentes graficos.
 */

public class GameController implements Initializable {

    @FXML
    public StackPane root;

    @FXML
    private Canvas canvas;

    // Referencias a las vistas
    @FXML
    private VBox statsView; // Se inyecta automaticamente

    /**
     * <p>
     * Si el controlador de StatsView es null se debe a un problema comun cuando se usa fx:include. Para obtener el controlador
     * del FXML incluido, necesitas usar una convencion de nombres especifica en GameController. El campo debe nombrarse
     * concatenando el fx:id del include con la palabra "Controller".
     */
    // Referencias a los controladores
    @FXML
    private StatsController statsViewController; // Debe coincidir con: fx:id + "Controller"

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeCanvas();
        // Font.loadFont(getClass().getResourceAsStream("/font/Morris Roman.ttf"), 18);
    }

    public void toggleStatsView() {
        statsView.setVisible(!statsView.isVisible());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public StatsController getStatsViewController() {
        return statsViewController;
    }

    private void resizeCanvas() {
        canvas.setWidth(WINDOW_WIDTH);
        canvas.setHeight(WINDOW_HEIGHT);
    }

}
