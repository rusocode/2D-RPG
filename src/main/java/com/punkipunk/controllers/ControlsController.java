package com.punkipunk.controllers;

import javafx.fxml.FXML;

public class ControlsController {

    private GameController gameController;

    @FXML
    public void handleCloseButtonClicked() {
        gameController.toggleControlsView();
    }

    public void handleBackButtonClicked() {
        gameController.toggleOptionsView();
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