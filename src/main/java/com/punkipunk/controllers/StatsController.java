package com.punkipunk.controllers;

import com.punkipunk.world.entity.Stats;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controla la logica de la vista de estadisticas, actualizando la informacion del jugador.
 */

public class StatsController {

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

    /**
     * <p>
     * De esta manera, separa la logica de inicializacion y configuracion del controlador y la logica de manejo de eventos
     * especificos, como el clic en el boton cerrar.
     * <p>
     * No es necesario configurar un listener de eventos adicional en el FXML, ya que eso se manejara directamente en el metodo
     * del controlador
     */
    @FXML
    public void handleCloseButtonClicked() {
        gameController.toggleStatsView();
    }

    /**
     * Actualiza las estadisticas del jugador.
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