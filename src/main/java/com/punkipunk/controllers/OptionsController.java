package com.punkipunk.controllers;

import com.punkipunk.audio.Audio;
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
    private Audio music, ambient, sound;

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

    public void setAudioSystems(Audio music, Audio ambient, Audio sound) {
        this.music = music;
        this.ambient = ambient;
        this.sound = sound;
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
            // Actualiza el nivel de volumen de la musica con el nuevo valor del slider
            music.volume = newValue.intValue();
            music.setVolume();
            // Guarda el nuevo valor del volumen en el archivo de configuracion de nivel de volumen
            game.gameSystem.audio.save();
        });

        ambientSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ambient.volume = newValue.intValue();
            ambient.setVolume();
            game.gameSystem.audio.save();
        });

        soundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sound.volume = newValue.intValue();
            sound.setVolume();
            game.gameSystem.audio.save();
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
