package com.punkipunk.controllers;

import com.punkipunk.gui.container.ContainerDragDrop;
import com.punkipunk.gui.container.ContainerMouse;
import com.punkipunk.gui.container.inventory.InventoryDragDrop;
import com.punkipunk.gui.container.inventory.InventoryMouse;
import com.punkipunk.world.entity.Player;
import javafx.fxml.FXML;

/**
 * <p>
 * Clase que implementa el controlador especifico para el inventario del jugador. Extiende de ContainerController para manejar la
 * interfaz grafica y los eventos relacionados con el inventario.
 */

public class InventoryController extends ContainerController {

    /** Referencia al jugador propietario del inventario */
    private Player player;

    @FXML
    private HotbarController hotbarViewController;

    /**
     * Inicializa el controlador del inventario y de la hotbar con un jugador especifico.
     * <p>
     * Configura el controlador base con el inventario del jugador.
     *
     * @param player el jugador cuyo inventario sera controlado
     */
    public void initialize(Player player) {
        this.player = player;
        initialize(player.inventory);
        hotbarViewController.initialize(player, this);
    }

    /**
     * Crea el manejador de eventos de arrastrar y soltar especifico para el inventario.
     *
     * @return una nueva instancia de InventoryDragDrop configurada para el inventario del jugador
     */
    @Override
    protected ContainerDragDrop createDragDrop() {
        return new InventoryDragDrop(player.inventory, this, player.hotbar, hotbarViewController);
    }

    /**
     * Crea el manejador de eventos del mouse especifico para el inventario.
     *
     * @return una nueva instancia de InventoryMouse configurada para el inventario del jugador
     */
    @Override
    protected ContainerMouse createMouse() {
        return new InventoryMouse(player.inventory, this, player);
    }

}

