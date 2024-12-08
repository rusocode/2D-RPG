package com.punkipunk.controllers;

public class ControlsController {

    private GameController gameController;

    public void handleBackButtonClicked() {
        gameController.toggleOptionsView();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

}