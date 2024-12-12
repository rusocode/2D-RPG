package com.punkipunk.gui.container.inventory;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.ContainerDragDrop;
import com.punkipunk.gui.container.SlotPosition;
import com.punkipunk.world.entity.item.Item;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Implementa la logica de drag & drop para los items del inventario.
 * <p>
 * TODO Agregar sonido de intercambio de items
 */

public class InventoryDragDrop extends ContainerDragDrop {

    public InventoryDragDrop(Container container, ContainerController controller) {
        super(container, controller);
    }

    @Override
    protected boolean onItemDropped(DragEvent event, StackPane slot) {
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

        Item sourceItem = container.get(sourceRow, sourceCol);
        Item destItem = container.get(destRow, destCol);

        if (sourceItem == null) return false;

        SlotPosition sourcePos = new SlotPosition(sourceRow, sourceCol);
        SlotPosition destPos = new SlotPosition(destRow, destCol);

        if (destItem == null) move(sourcePos, destPos, sourceItem);
        else swap(sourcePos, destPos, destItem, sourceItem);

        // Reaciona a los cambios (mover o cambiar items) ya que este controlador (InventoryController) extiende de ContainerController
        controller.onContainerChanged();

        return true;
    }

    private void move(SlotPosition sourcePos, SlotPosition destPos, Item sourceItem) {
        container.getSlots().remove(sourcePos);
        container.getSlots().put(destPos, sourceItem);
        // Marca la posicion de destino como ocupada
        int destIndex = destPos.row() * container.getCols() + destPos.col();
        container.getOccupiedSlots().set(destIndex);
        // Marca la posicion de origen como libre
        int sourceIndex = sourcePos.row() * container.getCols() + sourcePos.col();
        container.getOccupiedSlots().clear(sourceIndex);
    }

    private void swap(SlotPosition sourcePos, SlotPosition destPos, Item destItem, Item sourceItem) {
        container.getSlots().put(sourcePos, destItem);
        container.getSlots().put(destPos, sourceItem);
    }

}
