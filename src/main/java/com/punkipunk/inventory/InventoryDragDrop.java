package com.punkipunk.inventory;

import com.punkipunk.controllers.InventoryController;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Optional;

public class InventoryDragDrop {

    private final Player player;
    private final InventoryController inventoryController;

    public InventoryDragDrop(Player player, InventoryController inventoryController) {
        this.player = player;
        this.inventoryController = inventoryController;
    }

    /**
     * Establece los eventos de arrastre al slot.
     */
    public void setEvents(StackPane slot, int row, int col) {
        setDragDetected(slot, row, col);
        setDragOver(slot);
        setDragDropped(slot);
        setDragEnterExit(slot);
    }

    private void setDragDetected(StackPane slot, int row, int col) {
        slot.setOnDragDetected(event -> {
            Item item = player.inventory.get(row, col);
            if (item != null) {
                Dragboard dragboard = prepareDragboard(slot);
                addDragboardContent(dragboard, row, col);
                event.consume();
            }
        });
    }

    /**
     * Prepara el dragboard con la imagen del item.
     */
    private Dragboard prepareDragboard(StackPane slot) {
        Dragboard dragboard = slot.startDragAndDrop(TransferMode.MOVE);

        // Crea una instantanea de la imagen del item para obtener un feedback visual usando Optional para manejar posibles ausencias de ImageView
        Optional.ofNullable(slot.getChildren().get(0))
                .filter(node -> node instanceof ImageView)
                .map(node -> (ImageView) node)
                .ifPresent(imageView -> {
                    SnapshotParameters params = new SnapshotParameters();
                    params.setFill(Color.TRANSPARENT);
                    Image snapshot = imageView.snapshot(params, null);
                    dragboard.setDragView(snapshot);
                });

        return dragboard;
    }

    /**
     * Agrega informacion de coordenadas.
     */
    private void addDragboardContent(Dragboard dragboard, int row, int col) {
        ClipboardContent content = new ClipboardContent();
        content.putString(row + "," + col);
        dragboard.setContent(content);
    }

    private void setDragOver(StackPane slot) {
        slot.setOnDragOver(event -> {
            // Permite arrastrar sobre otros slots con contenido en el dragboard
            if (event.getGestureSource() != slot && event.getDragboard().hasString())
                event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
    }

    private void setDragDropped(StackPane slot) {
        slot.setOnDragDropped(event -> {
            boolean success = handleItemDrop(event, slot);
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Gestiona la logica de soltar el item.
     */
    private boolean handleItemDrop(DragEvent event, StackPane slot) {
        Dragboard dragboard = event.getDragboard();
        if (!dragboard.hasString()) return false;
        return moveOrSwapItems(dragboard, slot);
    }

    private boolean moveOrSwapItems(Dragboard dragboard, StackPane slot) {
        String[] coords = dragboard.getString().split(",");
        int sourceRow = Integer.parseInt(coords[0]);
        int sourceCol = Integer.parseInt(coords[1]);
        int destRow = GridPane.getRowIndex(slot);
        int destCol = GridPane.getColumnIndex(slot);

        Item sourceItem = player.inventory.get(sourceRow, sourceCol);
        Item destItem = player.inventory.get(destRow, destCol);

        if (sourceItem == null) return false;

        SlotPosition sourcePos = new SlotPosition(sourceRow, sourceCol);
        SlotPosition destPos = new SlotPosition(destRow, destCol);

        if (destItem == null) moveItem(sourcePos, destPos, sourceItem);
        else swapItems(sourcePos, destPos, destItem, sourceItem);

        inventoryController.updateInventory();
        return true;
    }

    private void moveItem(SlotPosition sourcePos, SlotPosition destPos, Item sourceItem) {
        player.inventory.getItems().remove(sourcePos);
        player.inventory.getItems().put(destPos, sourceItem);
        // Marca la posicion de destino como ocupada en el BitSet convirtiendo las coordenadas de fila y columna en un indice plano para manipular el BitSet
        int destIndex = destPos.row() * player.inventory.getCols() + destPos.col();
        player.inventory.getOccupiedPositions().set(destIndex);
        // Marca la posicion de origen como libre en el BitSet
        int sourceIndex = sourcePos.row() * player.inventory.getCols() + sourcePos.col();
        player.inventory.getOccupiedPositions().clear(sourceIndex);
    }

    private void swapItems(SlotPosition sourcePos, SlotPosition destPos, Item destItem, Item sourceItem) {
        player.inventory.getItems().put(sourcePos, destItem);
        player.inventory.getItems().put(destPos, sourceItem);
    }

    private void setDragEnterExit(StackPane slot) {
        slot.setOnDragEntered(event -> {
            if (event.getGestureSource() != slot) slot.getStyleClass().add("drag");
        });
        slot.setOnDragExited(event -> slot.getStyleClass().remove("drag"));
    }

}
