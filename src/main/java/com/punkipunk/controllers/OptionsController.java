package com.punkipunk.controllers;

import com.punkipunk.audio.AudioSource;
import com.punkipunk.core.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controla la logica de la vista de opciones, incluyendo la configuracion de los controles de audio.
 */

public class OptionsController implements Initializable {

    @FXML
    private Slider musicSlider;
    @FXML
    private Slider ambientSlider;
    @FXML
    private Slider soundSlider;
    @FXML
    private Label statsButton;
    @FXML
    private Label controlsButton;
    @FXML
    private Label quitToMainMenuButton;

    private Game game;
    private GameController gameController;
    private AudioSource music, ambient, sound;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statsButton.setOnMouseClicked(mouseEvent -> gameController.toggleStatsView());
        controlsButton.setOnMouseClicked(mouseEvent -> gameController.toggleControlsView());
        quitToMainMenuButton.setOnMouseClicked(mouseEvent -> gameController.quitToMainMenu());
        setupVolumeControls();
    }

    @FXML
    public void handleSaveGameClicked() {
        gameController.saveGame();
    }

    public void setAudioSystems(AudioSource music, AudioSource ambient, AudioSource sound) {
        this.music = music;
        this.ambient = ambient;
        this.sound = sound;
        // TODO Se podria establecer el volumen desde fxml?
        musicSlider.setValue(music.volume);
        ambientSlider.setValue(ambient.volume);
        soundSlider.setValue(sound.volume);
    }

    /**
     * Configura un listener (escuchador) para los cambios de valor en un slider que controla el volumen del audio.
     * <p>
     * La linea {@code musicSlider.valueProperty().addListener()} añade un listener a la propiedad de valor del slider.
     * <p>
     * {@code (observable, oldValue, newValue) ->} - Esta es la sintaxis de la funcion lambda que recibe tres parametros:
     * <ul>
     * <li>{@code observable}: La propiedad que esta siendo observada.
     * <li>{@code oldValue}: El valor anterior del slider.
     * <li>{@code newValue}: El nuevo valor del slider despues del cambio.
     * </ul>
     */
    private void setupVolumeControls() {
        musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // TODO Se podria hacer dos en 1 (actualizar el volumen y guardar la configuracion)
            // Actualiza el nivel de volumen de la musica con el nuevo valor del slider
            music.setVolume(newValue.intValue(), true);
            // Guarda el nuevo valor del volumen en volume.json
            game.gameSystem.audio.saveVolume();
        });

        ambientSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ambient.setVolume(newValue.intValue(), true);
            game.gameSystem.audio.saveVolume();
        });

        soundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sound.setVolume(newValue.intValue(), true);
            game.gameSystem.audio.saveVolume();
        });
    }

    /**
     * Establece el controlador del juego.
     *
     * @param gameController controlador del juego.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
