package com.punkipunk.controllers;

import com.punkipunk.inventory.InventoryListener;
import com.punkipunk.inventory.PlayerInventory;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.item.Item;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Maneja la interfaz grafica y las interacciones del usuario.
 * <p>
 * TODO En vez de recibir un objeto Player podria recibir un tipo generico como Entity (chest, trader, etc.) para que la clase sea mas abstracta
 */

public class InventoryController implements InventoryListener {

    @FXML
    private GridPane inventory;
    private GameController gameController;
    private Player player;

    public void initialize(Player player) {
        this.player = player;
        player.inventory.addListener(this); // Registra el listener
        initializeGrid();
    }

    @Override
    public void onInventoryChanged() {
        updateInventory();
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
                /* Agrega el slot (imagen y label superpuestos) en la columna y fila especificada del inventario. El slot puede
                 * estar vacio o con un item. */
                inventory.add(slot, col, row);
            }
        }
        // Actualiza el inventario en caso de que hayan items inciales (para que se puedan ver)
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
        StackPane slot = (StackPane) getNodeFromGridPane(inventory, row, col);
        if (slot != null) {
            ImageView imageView = (ImageView) slot.getChildren().get(0);
            Label amount = (Label) slot.getChildren().get(1);

            Item item = player.inventory.get(row, col);
            if (item != null) {
                imageView.setImage(item.sheet.frame);
                amount.setText(item.amount > 1 ? String.valueOf(item.amount) : "");
            } else {
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
        slot.getStyleClass().add("inventory-slot");

        ImageView image = new ImageView();
        // TODO No deberia ir en CSS?
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
        Tooltip tooltip = new Tooltip();
        tooltip.getStyleClass().add("tooltip");


        slot.setOnMouseMoved(event -> {
            Item item = player.inventory.get(row, col);
            if (item != null) {
                tooltip.setText(item.stats.name);
                // Obtiene las coordenadas del slot en la pantalla
                Bounds bounds = slot.localToScreen(slot.getBoundsInLocal());
                // Calcula la posicion del tooltip basada en los limites del slot y la posicion del mouse
                double tooltipX = bounds.getMinX() + event.getX() + bounds.getWidth() / 2 - 5;
                double tooltipY = bounds.getMinY() + event.getY() - bounds.getHeight() / 2 - 5;
                tooltip.show(slot, tooltipX, tooltipY);
            }
        });

        slot.setOnMouseExited(event -> tooltip.hide());

        slot.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Item itemClicked = player.inventory.get(row, col);
                if (itemClicked != null) {
                    if (itemClicked.type == Type.CONSUMABLE) {
                        player.inventory.consume(itemClicked, row, col);
                        if (itemClicked.amount < 1)
                            tooltip.hide(); // Despues de consumir el item verifica si la cantidad es menor a 1 para ocultar el tooltip
                    } else {
                        player.inventory.equip(itemClicked);
                        updateSlotBackground(slot, itemClicked);
                    }
                }
                updateSlot(row, col);
            }
        });
    }

    private void updateSlotBackground(StackPane slot, Item itemClicked) {
        clearSimilarSlotBackgrounds(itemClicked);
        // Establece el estilo de la clase equipped al slot si el item clickeado esta equipado o remueve el estilo de la clase equipped si no esta equipado
        if (isItemEquipped(itemClicked)) slot.getStyleClass().add("equipped");
        else slot.getStyleClass().remove("equipped");
    }

    /**
     * Limpia los fondos de los items del mismo tipo.
     *
     * @param itemClicked item clickeado.
     */
    private void clearSimilarSlotBackgrounds(Item itemClicked) {
        for (int row = 0; row < PlayerInventory.ROWS; row++) {
            for (int col = 0; col < PlayerInventory.COLS; col++) {
                Item itemIndexed = player.inventory.get(row, col);
                if (itemIndexed != null && isSameItemType(itemIndexed, itemClicked)) {
                    StackPane slot = (StackPane) getNodeFromGridPane(inventory, row, col);
                    if (slot != null) slot.getStyleClass().remove("equipped");
                    // TODO Se podria hacer un break?
                }
            }
        }
    }

    /**
     * Verifica si el item clickeado esta equipado.
     *
     * @param itemClicked item clickeado del inventario.
     * @return true si el item clickeado esta equipado o false.
     */
    private boolean isItemEquipped(Item itemClicked) {
        return switch (itemClicked.type) {
            case AXE, SWORD, PICKAXE -> player.weapon == itemClicked;
            case SHIELD -> player.shield == itemClicked;
            case LIGHT -> player.light == itemClicked;
            default -> false;
        };
    }

    /**
     * Verifica si el item clickeado es igual al item indexado.
     *
     * @param itemIndexed item indexado del invetario.
     * @param itemClicked item clickeado del inventario.
     * @return true si el item clickeado es igual al item indexado o false.
     */
    private boolean isSameItemType(Item itemIndexed, Item itemClicked) {
        return switch (itemClicked.type) {
            case AXE, SWORD, PICKAXE -> Type.WEAPON.contains(itemIndexed.type);
            case SHIELD -> itemIndexed.type == Type.SHIELD;
            case LIGHT -> itemIndexed.type == Type.LIGHT;
            default -> false;
        };
    }

    /**
     * Obtiene el nodo del la grilla.
     */
    private Node getNodeFromGridPane(GridPane inventoryGrid, int row, int col) {
        for (Node node : inventoryGrid.getChildren())
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col)
                return node;
        return null;
    }

    /**
     * Limpia los recursos cuando se cierre la ventana.
     */
    public void cleanup() {
        if (player != null && player.inventory != null)
            player.inventory.removeListener(this);
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

