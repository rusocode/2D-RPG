package com.punkipunk.controllers;

import com.punkipunk.audio.Audio;
import com.punkipunk.managers.SceneManager;
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

    private final SceneManager sceneManager = SceneManager.getInstance();

    @FXML
    private Slider musicSlider;
    @FXML
    private Slider ambientSlider;
    @FXML
    private Slider soundSlider;
    @FXML
    private Label closeButton;
    @FXML
    private Label controlsButton;
    @FXML
    private Label quitToMainMenuButton;

    private GameController gameController;
    private Audio music, ambient, sound;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeButton.setOnMouseClicked(mouseEvent -> gameController.toggleOptionsView());
        controlsButton.setOnMouseClicked(mouseEvent -> gameController.toggleControlsView());
        quitToMainMenuButton.setOnMouseClicked(mouseEvent -> sceneManager.switchScene(sceneManager.getMainScene()));
        setupSliders();
    }

    @FXML
    public void handleSaveGameClicked() {
        gameController.saveGame();
    }

    /**
     * Actualiza los valores de los sliders segun los valores actuales de los objetos Audio.
     */
    private void updateSliderValues() {
        musicSlider.setValue(music.volumeScale);
        ambientSlider.setValue(ambient.volumeScale);
        soundSlider.setValue(sound.volumeScale);
    }

    /**
     * Configura un "listener" (escuchador) para los cambios de valor en un slider que controla el volumen del audio.
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
    private void setupSliders() {
        musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Actualiza la propiedad volumeScale del objeto musica con el nuevo valor del slider
            music.volumeScale = newValue.intValue();
            // Llama al metodo checkVolume() para aplicar los cambios de volumen
            music.checkVolume();
        });

        ambientSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ambient.volumeScale = newValue.intValue();
            ambient.checkVolume();
        });

        soundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sound.volumeScale = newValue.intValue();
            sound.checkVolume();
        });
    }

    public void setAudioSystems(Audio music, Audio ambient, Audio sound) {
        this.music = music;
        this.ambient = ambient;
        this.sound = sound;
        updateSliderValues(); // Actualizar los sliders con los valores actuales
    }

    /**
     * Establece el controlador del juego.
     *
     * @param gameController controlador del juego.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

}
