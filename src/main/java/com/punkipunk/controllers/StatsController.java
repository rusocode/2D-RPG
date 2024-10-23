package com.punkipunk.controllers;

import com.punkipunk.world.entity.Stats;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Vista que se superpone (no cambia de escena!) al Canvas (GameView.fxml).
 */

public class StatsController {

    @FXML
    private Label lvlLabel;
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
    private Label expLabel;
    @FXML
    private Label nextExpLabel;
    @FXML
    private Label goldLabel;

    /**
     * Actualiza las estadisticas del jugador en la interfaz.
     *
     * @param stats estadisticas del jugador.
     */
    public void update(Stats stats) {
        lvlLabel.setText(String.valueOf(stats.lvl));
        maxHpLabel.setText(stats.hp + "/" + stats.maxHp);
        maxManaLabel.setText(stats.mana + "/" + stats.maxMana);
        strengthLabel.setText(String.valueOf(stats.strength));
        dexterityLabel.setText(String.valueOf(stats.dexterity));
        attackLabel.setText(String.valueOf(stats.attack));
        defenseLabel.setText(String.valueOf(stats.defense));
        expLabel.setText(String.valueOf(stats.exp));
        nextExpLabel.setText(String.valueOf(stats.nextExp));
        goldLabel.setText(String.valueOf(stats.gold));
    }

}