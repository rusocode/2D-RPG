package com.punkipunk.controllers;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.engine.setup.GameSetupConfiguration;
import com.punkipunk.states.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.punkipunk.utils.Global.*;

public class MainController implements Initializable {

    // @FXML crea una conexion entre el elemento en el archivo FXML y la variable en el controlador
    @FXML
    public VBox panel;
    private Game game;
    private Scene scene;

    /**
     * Crea un nuevo juego.
     * <p>
     * {@code  Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();}
     * <ol>
     * <li>{@code event.getSource()} - Obtiene el objeto que genero el evento (en este caso, probablemente un boton)
     * <li>{@code Node} - Hace un casting del source a Node, ya que los elementos de la interfaz en JavaFX heredan de Node
     * <li>{@code .getScene()} - Obtiene la Scene (escena) a la que pertenece ese Node
     * <li>{@code .getWindow()} - Obtiene la Window (ventana) que contiene esa Scene
     * <li>{@code Stage} - Hace un casting final a Stage, que es el tipo especifico de Window usado en JavaFX
     * </ol>
     * <p>
     * En resumen, esta linea sigue la jerarquia de componentes en JavaFX para obtener una referencia a la ventana actual desde
     * cualquier elemento de la interfaz que genero el evento.
     * <p>
     * Esto es necesario porque despues quieres cambiar la escena de esa ventana con: {@code stage.setScene(scene);}
     */
    public void handleNewGameLabelClicked(MouseEvent event) {
        // Cambia de scene (panel o vista) en el stage (ventana) teniendo una scene activa por stage a la vez
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        State.setState(State.PLAY);
        game.playSound(Assets.getAudio(AudioAssets.SPAWN2));
        switch (game.systems.world.map.zone) {
            case OVERWORLD -> game.playMusic(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
            case DUNGEON -> game.playMusic(Assets.getAudio(AudioAssets.AMBIENT_DUNGEON));
            case BOSS -> game.playMusic(Assets.getAudio(AudioAssets.MUSIC_BOSS));
        }
        game.start();
    }

    public void handleLoadGameLabelClicked(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        game.systems.file.loadData();
        State.setState(State.PLAY);
        game.playSound(Assets.getAudio(AudioAssets.SPAWN2));
        game.playMusic(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
        game.start();
    }

    public void handleQuitLabelClicked() {
        game.playSound(Assets.getAudio(AudioAssets.CLICK2));
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
        Parent gameView;
        try {
            gameView = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene = new Scene(gameView); // JavaFX ajusta automaticamente el tamaño de la escena para acomodar su contenido (en este caso, el StackPane que contiene el Canvas)

        // Carga el controlador de GameView para poder "comunicarse" desde este controlador
        GameController gameController = loader.getController();
        game = new Game(scene, gameController);

        // Agrega sonido al pasar el mouse por encima de los labels
        panel.getChildren().forEach(node -> {
            if (node instanceof Label label) label.setOnMouseEntered(e -> game.playSound(Assets.getAudio(AudioAssets.SLOT)));
        });

        new GameSetupConfiguration(game, gameController).configure();
        // stage.show(); // TODO Parece que no hace falta llamarlo (se muestra igual)
    }

}
