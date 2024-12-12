package com.punkipunk.gui.container;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.world.entity.item.Item;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Optional;

/**
 * Gestiona los eventos de drag & drop.
 */

public abstract class ContainerDragDrop {

    protected final Container container;
    protected final ContainerController controller;

    public ContainerDragDrop(Container container, ContainerController controller) {
        this.container = container;
        this.controller = controller;
    }

    protected abstract boolean onItemDropped(DragEvent event, StackPane slot);

    public void setEvents(StackPane slot, int row, int col) {
        setDragDetected(slot, row, col);
        setDragOver(slot);
        setDragDropped(slot);
        setDragEnterExit(slot);
    }

    private void setDragDetected(StackPane slot, int row, int col) {
        slot.setOnDragDetected(event -> {
            Item item = container.get(row, col);
            if (item != null) {
                Dragboard dragboard = prepareDragboard(slot);
                addDragboardContent(dragboard, row, col);
                event.consume();
            }
        });
    }

    private Dragboard prepareDragboard(StackPane slot) {
        Dragboard dragboard = slot.startDragAndDrop(TransferMode.MOVE);

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

    private void addDragboardContent(Dragboard dragboard, int row, int col) {
        ClipboardContent content = new ClipboardContent();
        content.putString(row + "," + col);
        dragboard.setContent(content);
    }

    private void setDragOver(StackPane slot) {
        slot.setOnDragOver(event -> {
            if (event.getGestureSource() != slot && event.getDragboard().hasString())
                event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
    }

    private void setDragDropped(StackPane slot) {
        slot.setOnDragDropped(event -> {
            boolean success = onItemDropped(event, slot);
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setDragEnterExit(StackPane slot) {
        slot.setOnDragEntered(event -> {
            if (event.getGestureSource() != slot) slot.getStyleClass().add("drag");
        });
        slot.setOnDragExited(event -> slot.getStyleClass().remove("drag"));
    }

}
