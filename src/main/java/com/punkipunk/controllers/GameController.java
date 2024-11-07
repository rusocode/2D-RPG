package com.punkipunk.controllers;

import com.punkipunk.Game;
import com.punkipunk.audio.AudioManager;
import com.punkipunk.managers.ViewManager;
import com.punkipunk.utils.ViewState;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.punkipunk.utils.Global.WINDOW_HEIGHT;
import static com.punkipunk.utils.Global.WINDOW_WIDTH;

/**
 * Controla la logica de la escena con respecto a todo lo que tiene que ver con los componentes graficos.
 */

public class GameController implements Initializable {

    @FXML
    private StackPane root; // @FXML crea una conexion entre el elemento en el archivo FXML y la variable en el controlador
    @FXML
    private Canvas canvas;
    @FXML
    private VBox statsView; // Se inyecta automaticamente
    @FXML
    private VBox optionsView;
    @FXML
    private VBox controlsView;
    /**
     * <p>
     * Si el controlador de StatsView es null se debe a un problema comun cuando se usa fx:include. Para obtener el controlador
     * del FXML incluido, necesitas usar una convencion de nombres especifica en el controlador. El campo debe nombrarse
     * concatenando el fx:id del include con la palabra "Controller".
     */
    @FXML
    private StatsController statsViewController;
    @FXML
    private OptionsController optionsViewController;
    @FXML
    private ControlsController controlsViewController;

    private ViewManager viewManager;
    private AudioManager audioManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewManager = new ViewManager(statsView, optionsView, controlsView);
        optionsViewController.setGameController(this);
        statsViewController.setGameController(this);
        controlsViewController.setGameController(this);
        canvas.setWidth(WINDOW_WIDTH);
        canvas.setHeight(WINDOW_HEIGHT);
        configureButtons(root);
    }

    public void setup(Game game) {
        audioManager = game.systems.audioManager;
        optionsViewController.setAudioSystems(game.systems.music, game.systems.ambient, game.systems.sound);
    }

    /**
     * Configura todos los botones.
     *
     * @param node nodo del arbol de la interfaz grafica.
     */
    private void configureButtons(Node node) {
        /* Comprueba si el nodo es de tipo Label. Usa pattern matching (caracteristica de Java moderno) que permite hacer el
         * casting a Label y asignarlo a la variable label en la misma linea: if (node instanceof Label) { Label label = (Label) node; } */
        if (node instanceof Label label) {
            String id = label.getId();
            // Verifica que el id termine con la palabra "Button"
            if (id != null && id.endsWith("Button")) {
                // Guarda el manejador de eventos de clic original del Label (si existe)
                EventHandler<? super MouseEvent> originalHandler = label.getOnMouseClicked();
                label.setStyle("-fx-cursor: hand;");
                /* Configura el nuevo manejador de eventos. Crea un nuevo manejador de eventos que primero reproduce un sonido de
                 * clic y luego ejecuta el manejador original si existia uno. Esto preserva cualquier funcionalidad que el Label
                 * ya tuviera. */
                label.setOnMouseClicked(event -> {
                    audioManager.playClickSound();
                    if (originalHandler != null) originalHandler.handle(event); // Si habia un handler original, lo ejecuta
                });
            }
        }

        /* Hace un recorrido recursivo en donde verifica si el nodo es un contenedor (Parent). Si lo es, recorre recursivamente
         * todos sus hijos. Usa method reference (this::configureButtons) para aplicar este mismo metodo a cada hijo. */
        if (node instanceof Parent parent) parent.getChildrenUnmodifiable().forEach(this::configureButtons);

    }

    public void toggleStatsView() {
        viewManager.toggleView(ViewState.STATS);
    }

    public void toggleOptionsView() {
        viewManager.toggleView(ViewState.OPTIONS);
    }

    public void toggleControlsView() {
        viewManager.toggleView(ViewState.CONTROLS);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public StatsController getStatsController() {
        return statsViewController;
    }

}
