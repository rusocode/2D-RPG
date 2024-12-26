package com.punkipunk.gui.container.inventory;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.controllers.HotbarController;
import com.punkipunk.entity.item.Item;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.ContainerDragDrop;
import com.punkipunk.gui.container.SlotPosition;
import com.punkipunk.gui.container.hotbar.Hotbar;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * <p>
 * Clase que implementa la funcionalidad especifica de arrastrar y soltar para el inventario. Extiende de ContainerDragDrop para
 * manejar las operaciones de movimiento e intercambio de items entre la hotbar y el inventario, asi como dentro del propio
 * inventario.
 */

public class InventoryDragDrop extends ContainerDragDrop {

    private final Hotbar hotbar;
    private final HotbarController hotbarController;

    public InventoryDragDrop(Container container, ContainerController controller, Hotbar hotbar, HotbarController hotbarController) {
        super(container, controller);
        this.hotbar = hotbar;
        this.hotbarController = hotbarController;
    }

    @Override
    protected boolean onItemDropped(DragEvent event, StackPane slot) {
        if (isInvalidDrag(event)) return false;
        DragInfo dragInfo = extractDragInfo(event, slot);
        return isSourceFromHotbar(event) ? handleItemFromHotbar(dragInfo) : handleItemInInventory(dragInfo);
    }

    private boolean isSourceFromHotbar(DragEvent event) {
        Node source = (Node) event.getGestureSource();
        GridPane sourceGrid = (GridPane) source.getParent();
        return sourceGrid.equals(hotbarController.getGrid());
    }

    private boolean handleItemFromHotbar(DragInfo dragInfo) {
        Item sourceItem = hotbar.get(dragInfo.sourceRow(), dragInfo.sourceCol());
        Item destItem = container.get(dragInfo.destRow(), dragInfo.destCol());

        if (sourceItem == null) return false;

        if (destItem == null) moveItemFromHotbar(sourceItem, dragInfo);
        else swapItemFromHotbar(sourceItem, destItem, dragInfo);

        controller.onContainerChanged();
        hotbarController.onContainerChanged();

        return true;
    }

    private void moveItemFromHotbar(Item sourceItem, DragInfo dragInfo) {
        hotbar.remove(dragInfo.sourceRow(), dragInfo.sourceCol());
        container.put(sourceItem, dragInfo.destRow(), dragInfo.destCol());
        int destIndex = dragInfo.destRow() * container.getCols() + dragInfo.destCol();
        container.getOccupiedSlots().set(destIndex);
    }

    private void swapItemFromHotbar(Item sourceItem, Item destItem, DragInfo dragInfo) {
        hotbar.remove(dragInfo.sourceRow(), dragInfo.sourceCol());
        hotbar.put(destItem, dragInfo.sourceRow(), dragInfo.sourceCol());
        container.getSlots().remove(new SlotPosition(dragInfo.destRow(), dragInfo.destCol()));
        container.getSlots().put(new SlotPosition(dragInfo.destRow(), dragInfo.destCol()), sourceItem);
    }

    private boolean handleItemInInventory(DragInfo dragInfo) {
        Item sourceItem = container.get(dragInfo.sourceRow(), dragInfo.sourceCol());
        Item destItem = container.get(dragInfo.destRow(), dragInfo.destCol());

        if (sourceItem == null) return false;

        SlotPosition sourcePos = new SlotPosition(dragInfo.sourceRow(), dragInfo.sourceCol());
        SlotPosition destPos = new SlotPosition(dragInfo.destRow(), dragInfo.destCol());

        if (destItem == null) moveItemInInventory(sourcePos, destPos, sourceItem);
        else swapItemInInventory(sourcePos, destPos, destItem, sourceItem);

        controller.onContainerChanged();
        return true;
    }

    private void moveItemInInventory(SlotPosition sourcePos, SlotPosition destPos, Item sourceItem) {
        container.getSlots().remove(sourcePos);
        container.getSlots().put(destPos, sourceItem);
        int destIndex = destPos.row() * container.getCols() + destPos.col();
        container.getOccupiedSlots().set(destIndex);
        int sourceIndex = sourcePos.row() * container.getCols() + sourcePos.col();
        container.getOccupiedSlots().clear(sourceIndex);
    }

    private void swapItemInInventory(SlotPosition sourcePos, SlotPosition destPos, Item destItem, Item sourceItem) {
        container.getSlots().put(sourcePos, destItem);
        container.getSlots().put(destPos, sourceItem);
    }

}
