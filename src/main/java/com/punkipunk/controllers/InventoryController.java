package com.punkipunk.controllers;

import com.punkipunk.PlayerInventory;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Controlador del inventario del player.
 * <p>
 * TODO Deberia cambiarse a PlayerInventoryController
 */

public class InventoryController {

    @FXML
    private GridPane inventoryGrid;
    private GameController gameController;
    private Player player;

    public void initialize(Player player) {
        this.player = player;
        initializeGrid();
    }

    public void handleCloseButtonClicked() {
        gameController.toggleInventoryView();
    }

    /**
     * Inicializa la grilla (de 4x4 en este caso) con los slots vacios y agregandole interaccion a cada uno.
     */
    private void initializeGrid() {
        for (int row = 0; row < PlayerInventory.ROWS; row++) {
            for (int col = 0; col < PlayerInventory.COLS; col++) {
                StackPane slot = createEmptySlot(row, col);
                addSlotInteraction(slot, row, col);
                /* Agrega el slot (imagen y label superpuestos) en la columna y fila especificada del inventario. El slot puede
                 * estar vacio o con un item. */
                inventoryGrid.add(slot, col, row);
            }
        }
        // Actualiza el inventario en caso de que hayan items inciales
        updateInventory();
    }

    /**
     * Actualiza el inventario.
     */
    public void updateInventory() {
        for (int row = 0; row < PlayerInventory.ROWS; row++)
            for (int col = 0; col < PlayerInventory.COLS; col++)
                updateSlot(row, col);
    }

    /**
     * Actualiza el inventario.
     *
     * @param row fila del slot.
     * @param col columna del slot.
     */
    private void updateSlot(int row, int col) {
        StackPane slot = (StackPane) getNodeFromGridPane(inventoryGrid, row, col);
        if (slot != null) {
            ImageView imageView = (ImageView) slot.getChildren().get(0);
            Label amount = (Label) slot.getChildren().get(1);

            Item item = player.inventory.get(row, col);
            if (item != null) {
                imageView.setImage(item.sheet.frame);
                amount.setText(item.amount > 1 ? String.valueOf(item.amount) : "");
            } else {
                System.out.println("Slot [" + row + "][" + col + "] vacio!");
                imageView.setImage(null);
                amount.setText("");
            }
        }
    }

    /**
     * Crea un slot vacio.
     *
     * @param row fila del slot.
     * @param col columna del slot.
     * @return un StackPane que representa el item con la imagen y la cantidad.
     */
    private StackPane createEmptySlot(int row, int col) {
        StackPane slot = new StackPane();

        ImageView image = new ImageView();
        image.setFitWidth(32);
        image.setFitHeight(32);
        image.setPreserveRatio(true);

        Label amount = new Label();
        amount.getStyleClass().add("amount");

        slot.getChildren().addAll(image, amount);

        addSlotInteraction(slot, row, col);

        return slot;
    }

    private void addSlotInteraction(StackPane slot, int row, int col) {
        slot.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                player.inventory.select(row, col);
                updateSlot(row, col);
            }
        });
    }

    /**
     * Obtiene el nodo del la grilla.
     *
     * @param inventoryGrid grilla.
     * @param row           fila.
     * @param col           columna.
     * @return el nodo especificado.
     */
    private Node getNodeFromGridPane(GridPane inventoryGrid, int row, int col) {
        for (Node node : inventoryGrid.getChildren())
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col)
                return node;
        return null;
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
