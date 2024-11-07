package com.punkipunk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlsController implements Initializable {

    @FXML
    private Label closeButton;
    @FXML
    private Label backButton;

    private GameController gameController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeButton.setOnMouseClicked(event -> gameController.toggleControlsView());
        backButton.setOnMouseClicked(event -> gameController.toggleOptionsView());
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