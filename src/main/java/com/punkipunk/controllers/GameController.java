package com.punkipunk.controllers;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.input.keyboard.Key;
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
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.punkipunk.utils.Global.*;

/**
 * Controla la logica de la escena con respecto a todo lo que tiene que ver con los componentes graficos.
 * <p>
 * JavaFX con FXML necesita poder crear una instancia del controlador usando un constructor sin parametros (constructor por
 * defecto) cuando usas fx:controller en un archivo FXML. Este es un requerimiento del FXMLLoader. El ciclo tipico es que al
 * cargar el FXML, JavaFX crea una instancia del controlador usando el constructor sin parametros, inyecta las variables marcadas
 * con @FXML y llama al metodo initialize() si el controlador implementa Initializable. Despues de cargar el FXML, puedes llamar a
 * metodos adicionales de inicializacion para pasar datos o configurar el controlador con la informacion que necesita. Por eso en
 * nuestro caso necesitamos tener un constructor vacio requerido por FXML y un metodo initialize() para la inicializacion real. Si
 * intentaramos usar un constructor con parametros, JavaFX fallaria al cargar el FXML porque no puede encontrar un constructor sin
 * parametros. Esta es la razon por la que dividimos la inicializacion en dos partes: primero un constructor vacio para que JavaFX
 * pueda crear la instancia y luego un metodo initialize() para la inicializacion real con los datos necesarios.
 */

public class GameController implements Initializable {

    private final SceneDirector sceneManager = SceneDirector.getInstance();
    @FXML
    public Label fpsLabel;
    @FXML
    public Label cycleLabel;
    @FXML
    public Label testModeLabel;
    @FXML
    private VBox debug;
    @FXML
    private Label positionLabel;
    @FXML
    private Label versionLabel;

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
        optionsViewController.setGame(game);
        optionsViewController.setAudioSystems(game.gameSystem.audio.getMusic(), game.gameSystem.audio.getAmbient(), game.gameSystem.audio.getSound());
        inventoryViewController.initialize(game.gameSystem.world.entitySystem.player);
    }

    /**
     * <a href="https://minecraft.wiki/w/Debug_screen">MC Debug Screen</a>
     */
    public void showDebugInfo(int fps, double cycleTime) {

        int x = (int) ((game.gameSystem.world.entitySystem.player.position.x + game.gameSystem.world.entitySystem.player.hitbox.getX()) / tile);
        int y = (int) ((game.gameSystem.world.entitySystem.player.position.y + game.gameSystem.world.entitySystem.player.hitbox.getY()) / tile);

        if (game.gameSystem.keyboard.isKeyToggled(Key.DEBUG)) {
            versionLabel.setText(String.format("2D-RPG %s", VERSION));
            fpsLabel.setText(String.format("%d FPS", fps));
            positionLabel.setText(String.format("X: %d  Y: %d", x, y));
            cycleLabel.setText(String.format("Cycle time: %.2f ms", cycleTime));
            debug.setVisible(true);
        } else debug.setVisible(false);

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
                    game.gameSystem.audio.playSound(AudioID.Sound.CLICK);
                    if (originalHandler != null) originalHandler.handle(event); // Si habia un handler original, lo ejecuta
                });
            }
        }

        /* Hace un recorrido recursivo en donde verifica si el nodo es un contenedor (Parent). Si lo es, recorre recursivamente
         * todos sus hijos. Usa method reference (this::configureButtons) para aplicar este mismo metodo a cada hijo. */
        if (node instanceof Parent parent) parent.getChildrenUnmodifiable().forEach(this::configureButtons);

    }

    public void toggleTestMode() {
        testModeLabel.setVisible(game.gameSystem.keyboard.isKeyToggled(Key.TEST));
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
        game.gameSystem.file.saveData();
    }

    public void quitToMainMenu() {
        sceneManager.switchScene(sceneManager.getMainScene());
        game.gameSystem.audio.stopPlayback();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public StatsController getStatsViewController() {
        return statsViewController;
    }

}
