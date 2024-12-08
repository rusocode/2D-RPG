package com.punkipunk.controllers;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.scene.SceneDirector;
import com.punkipunk.scene.ViewState;
import com.punkipunk.scene.ViewToggle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

    private final SceneDirector sceneManager = SceneDirector.getInstance();

    @FXML
    private AnchorPane root; // @FXML crea una conexion entre el elemento en el archivo FXML y la variable en el controlador
    @FXML
    private Canvas canvas;
    @FXML
    private VBox statsView; // Se inyecta automaticamente
    @FXML
    private VBox optionsView;
    @FXML
    private VBox controlsView;
    @FXML
    private VBox inventoryView;
    @FXML
    private VBox HUDView;
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
    @FXML
    private InventoryController inventoryViewController;

    private Game game;
    private ViewToggle viewToggle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewToggle = new ViewToggle(statsView, optionsView, controlsView, inventoryView);
        statsViewController.setGameController(this);
        optionsViewController.setGameController(this);
        controlsViewController.setGameController(this);
        canvas.setWidth(WINDOW_WIDTH);
        canvas.setHeight(WINDOW_HEIGHT);
        configureButtons(root);
    }

    /**
     * Inicializa el controlador.
     *
     * @param game game.
     */
    public void initialize(Game game) {
        this.game = game;
        // TODO Es raro que se establescan los audios desde aca, aunque es necesario hacerlo antes de inicializar el controlodar del juego para evitar un NPE
        optionsViewController.setAudioSystems(game.system.audio.getMusic(), game.system.audio.getAmbient(), game.system.audio.getSound());
        inventoryViewController.initialize(game.system.world.entities.player);
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
                    game.system.audio.playSound(Assets.getAudio(AudioAssets.CLICK2));
                    if (originalHandler != null) originalHandler.handle(event); // Si habia un handler original, lo ejecuta
                });
            }
        }

        /* Hace un recorrido recursivo en donde verifica si el nodo es un contenedor (Parent). Si lo es, recorre recursivamente
         * todos sus hijos. Usa method reference (this::configureButtons) para aplicar este mismo metodo a cada hijo. */
        if (node instanceof Parent parent) parent.getChildrenUnmodifiable().forEach(this::configureButtons);

    }

    public void toggleStatsView() {
        viewToggle.toggleView(ViewState.STATS);
    }

    public void toggleOptionsView() {
        viewToggle.toggleView(ViewState.OPTIONS);
    }

    public void toggleControlsView() {
        viewToggle.toggleView(ViewState.CONTROLS);
    }

    public void toggleInventoryView() {
        viewToggle.toggleView(ViewState.INVENTORY);
    }

    public void saveGame() {
        game.system.file.saveData();
    }

    public void quitToMainMenu() {
        sceneManager.switchScene(sceneManager.getMainScene());
        game.system.audio.stop();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public StatsController getStatsViewController() {
        return statsViewController;
    }

    public InventoryController getInventoryViewController() {
        return inventoryViewController;
    }

}
