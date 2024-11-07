package com.punkipunk.controllers;

import com.punkipunk.world.entity.Stats;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controla la logica de la vista de estadisticas, actualizando la informacion del jugador.
 */

public class StatsController implements Initializable {

    @FXML
    private Label closeButton;
    @FXML
    private Label lvlLabel;
    @FXML
    private Label expLabel;
    @FXML
    private Label nextExpLabel;
    @FXML
    private Label maxHpLabel;
    @FXML
    private Label maxManaLabel;
    @FXML
    private Label strengthLabel;
    @FXML
    private Label dexterityLabel;
    @FXML
    private Label attackLabel;
    @FXML
    private Label defenseLabel;
    @FXML
    private Label goldLabel;

    private GameController gameController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeButton.setOnMouseClicked(event -> gameController.toggleStatsView());
    }

    /**
     * Actualiza las estadisticas del jugador en la interfaz.
     *
     * @param stats estadisticas del jugador.
     */
    public void update(Stats stats) {
        lvlLabel.setText(String.valueOf(stats.lvl));
        expLabel.setText(String.valueOf(stats.exp));
        nextExpLabel.setText(String.valueOf(stats.nextExp));
        maxHpLabel.setText(stats.hp + "/" + stats.maxHp);
        maxManaLabel.setText(stats.mana + "/" + stats.maxMana);
        strengthLabel.setText(String.valueOf(stats.strength));
        dexterityLabel.setText(String.valueOf(stats.dexterity));
        attackLabel.setText(String.valueOf(stats.attack));
        defenseLabel.setText(String.valueOf(stats.defense));
        goldLabel.setText(String.valueOf(stats.gold));
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