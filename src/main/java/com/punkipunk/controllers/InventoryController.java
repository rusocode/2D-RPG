package com.punkipunk.controllers;

import com.punkipunk.inventory.InventoryDragDrop;
import com.punkipunk.inventory.InventoryListener;
import com.punkipunk.inventory.InventoryMouse;
import com.punkipunk.inventory.SlotPosition;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Maneja la interfaz grafica y las interacciones del usuario.
 * <p>
 * TODO En vez de recibir un objeto Player podria recibir un tipo generico como Entity (chest, trader, etc.) para que la clase sea mas abstracta
 * TODO Agregar sonido de intercambio de items
 * TODO Implementar la division de items para items apilables
 */

public class InventoryController implements InventoryListener {

    private final Map<SlotPosition, StackPane> slots = new HashMap<>(); // Se reemplazo la busqueda lineal por una estructura de datos mas eficiente
    private Player player;
    private InventoryDragDrop inventoryDragDrop;
    private InventoryMouse inventoryMouse;
    @FXML
    private GridPane inventory;

    public void initialize(Player player) {
        this.player = player;
        player.inventory.addListener(this);
        inventoryMouse = new InventoryMouse(player, this);
        inventoryDragDrop = new InventoryDragDrop(player, this);
        initializeGrid();
    }

    @Override
    public void onInventoryChanged() {
        updateInventory();
    }

    /**
     * Inicializa la grilla.
     */
    private void initializeGrid() {
        for (int row = 0; row < player.inventory.getRows(); row++) {
            for (int col = 0; col < player.inventory.getCols(); col++) {
                StackPane slot = createSlot(row, col);
                slots.put(new SlotPosition(row, col), slot);
                setSlotEvents(slot, row, col);
                /* Agrega el slot (imagen y label superpuestos) en la columna y fila especificada del inventario (GridPane). El
                 * slot puede estar vacio o con un item. */
                inventory.add(slot, col, row);
            }
        }
    }

    /**
     * Crea un slot.
     *
     * @param row fila del slot.
     * @param col columna del slot.
     * @return un StackPane que representa el item con la imagen y la cantidad.
     */
    private StackPane createSlot(int row, int col) {
        if (row < 0 || row >= player.inventory.getRows() || col < 0 || col >= player.inventory.getCols())
            throw new IllegalArgumentException("Invalid slot position");

        StackPane slot = new StackPane();
        slot.getStyleClass().add("slot");

        ImageView image = new ImageView();
        image.getStyleClass().add("image");

        Label amount = new Label();
        amount.getStyleClass().add("amount");

        slot.getChildren().addAll(image, amount);

        /* Si existe un item en la lista de items, entonces lo agrega al inventario (establece la imagen y la cantidad del item
         * especifico de la lista en ImageView y Label). Esto se hace por si hay items iniciales en la lista. */
        Item item = player.inventory.get(row, col);
        if (item != null) {
            image.setImage(item.sheet.frame);
            amount.setText(item.amount > 1 ? String.valueOf(item.amount) : "");
        }

        return slot;
    }

    /**
     * Agrega los eventos al slot.
     */
    private void setSlotEvents(StackPane slot, int row, int col) {
        inventoryMouse.setEvents(slot, row, col);
        inventoryDragDrop.setEvents(slot, row, col);
    }

    /**
     * Actualiza el inventario.
     */
    public void updateInventory() {
        for (int row = 0; row < player.inventory.getRows(); row++)
            for (int col = 0; col < player.inventory.getCols(); col++)
                updateSlot(row, col);
    }

    /**
     * Actualiza el inventario.
     *
     * @param row fila del slot.
     * @param col columna del slot.
     */
    public void updateSlot(int row, int col) {
        Optional.ofNullable(getSlot(new SlotPosition(row, col)))
                .ifPresent(slot -> Optional.ofNullable(player.inventory.get(row, col))
                        .ifPresentOrElse(
                                item -> updateSlotWithItem(slot, item),
                                () -> clearSlot(slot)
                        )
                );
    }

    private void updateSlotWithItem(StackPane slot, Item item) {
        ImageView imageView = (ImageView) slot.getChildren().get(0);
        Label amount = (Label) slot.getChildren().get(1);

        imageView.setImage(item.sheet.frame);
        amount.setText(item.amount > 1 ? String.valueOf(item.amount) : "");
    }

    private void clearSlot(StackPane slot) {
        ImageView imageView = (ImageView) slot.getChildren().get(0);
        Label amount = (Label) slot.getChildren().get(1);

        imageView.setImage(null);
        amount.setText("");
    }

    /**
     * Obtiene el slot en la posicion especificada.
     *
     * @param pos posicion del slot.
     * @return el slot en la posicion especificada.
     */
    public StackPane getSlot(SlotPosition pos) {
        return slots.get(pos);
    }

}

