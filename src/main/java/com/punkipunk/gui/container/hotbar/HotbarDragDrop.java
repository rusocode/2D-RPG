package com.punkipunk.gui.container.hotbar;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.controllers.InventoryController;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.ContainerDragDrop;
import com.punkipunk.gui.container.SlotPosition;
import com.punkipunk.gui.container.inventory.Inventory;
import com.punkipunk.world.entity.item.Item;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * <p>
 * Clase que implementa la funcionalidad especifica de arrastrar y soltar para la hotbar. Extiende de ContainerDragDrop para
 * manejar las operaciones de movimiento e intercambio de items entre el inventario y la hotbar, asi como dentro de la propia
 * hotbar.
 */

public class HotbarDragDrop extends ContainerDragDrop {

    private final Inventory inventory;
    private final InventoryController inventoryController;

    public HotbarDragDrop(Container container, ContainerController controller, Inventory inventory, InventoryController inventoryController) {
        super(container, controller);
        this.inventory = inventory;
        this.inventoryController = inventoryController;
    }

    /**
     * Implementa la logica cuando un item es soltado en un slot de la hotbar.
     * <p>
     * Verifica si el dragboard contiene la informacion necesaria y procede a manejar el movimiento de items segun su origen
     * (inventario o hotbar).
     *
     * @param event el evento de arrastre que contiene la informacion de la operacion
     * @param slot  el StackPane donde se solto el item
     * @return true si la operacion fue exitosa, false en caso contrario
     */
    @Override
    protected boolean onItemDropped(DragEvent event, StackPane slot) {
        if (isInvalidDrag(event)) return false;
        DragInfo dragInfo = extractDragInfo(event, slot);
        return isSourceFromInventory(event) ? handleItemFromInventory(dragInfo) : handleItemInHotbar(dragInfo);
    }

    /**
     * Determina si el origen del drag es desde el inventario.
     *
     * @param event el evento de drag a verificar
     * @return true si el origen es el inventario, false si es la hotbar
     */
    private boolean isSourceFromInventory(DragEvent event) {
        Node source = (Node) event.getGestureSource();
        GridPane sourceGrid = (GridPane) source.getParent();
        return sourceGrid.equals(inventoryController.getGrid());
    }

    /**
     * Maneja el movimiento de items desde el inventario hacia la hotbar.
     *
     * @param dragInfo informacion sobre el origen y destino del movimiento
     * @return true si la operacion fue exitosa, false en caso contrario
     */
    private boolean handleItemFromInventory(DragInfo dragInfo) {
        Item sourceItem = inventory.get(dragInfo.sourceRow(), dragInfo.sourceCol());
        Item destItem = container.get(dragInfo.destRow(), dragInfo.destCol());

        if (sourceItem == null) return false;

        if (destItem == null) moveItemFromInventory(sourceItem, dragInfo);
        else swapItemFromInventory(sourceItem, destItem, dragInfo);

        // Notifica a ambos contenedores ya que se generaron movimientos entre los dos contenedores
        controller.onContainerChanged();
        inventoryController.onContainerChanged();

        return true;
    }

    /**
     * Mueve un item desde el inventario a un slot vacio de la hotbar.
     *
     * @param sourceItem el item a mover
     * @param dragInfo   informacion sobre el origen y destino del movimiento
     */
    private void moveItemFromInventory(Item sourceItem, DragInfo dragInfo) {
        // Elimina el item de origen del inventario
        inventory.remove(dragInfo.sourceRow(), dragInfo.sourceCol());
        // Agrega el item de origen del inventario en la posicion de destino del hotbar
        container.put(sourceItem, dragInfo.destRow(), dragInfo.destCol());
        int destIndex = dragInfo.destRow() * container.getCols() + dragInfo.destCol();
        container.getOccupiedSlots().set(destIndex);
    }

    /**
     * Intercambia items entre el inventario y la hotbar.
     *
     * @param sourceItem el item del inventario
     * @param destItem   el item de la hotbar
     * @param dragInfo   informacion sobre el origen y destino del intercambio
     */
    private void swapItemFromInventory(Item sourceItem, Item destItem, DragInfo dragInfo) {
        // Elimina el item de origen del inventario
        inventory.remove(dragInfo.sourceRow(), dragInfo.sourceCol());
        // Agrega el item de destino del hotbar en la posicion de origen del item del inventario
        inventory.put(destItem, dragInfo.sourceRow(), dragInfo.sourceCol());
        // Elimina el item de destino del hotbar
        container.getSlots().remove(new SlotPosition(dragInfo.destRow(), dragInfo.destCol()));
        // Agrega el item de origen del inventario en la posicion de destino del item del hotbar
        container.getSlots().put(new SlotPosition(dragInfo.destRow(), dragInfo.destCol()), sourceItem);
    }

    /**
     * Maneja el movimiento de items dentro de la hotbar.
     *
     * @param dragInfo informacion sobre el origen y destino del movimiento
     * @return true si la operacion fue exitosa, false en caso contrario
     */
    private boolean handleItemInHotbar(DragInfo dragInfo) {
        Item sourceItem = container.get(dragInfo.sourceRow(), dragInfo.sourceCol());
        Item destItem = container.get(dragInfo.destRow(), dragInfo.destCol());

        if (sourceItem == null) return false;

        SlotPosition sourcePos = new SlotPosition(dragInfo.sourceRow(), dragInfo.sourceCol());
        SlotPosition destPos = new SlotPosition(dragInfo.destRow(), dragInfo.destCol());

        if (destItem == null) moveItemInHotbar(sourcePos, destPos, sourceItem);
        else swapItemInHotbar(sourcePos, destPos, sourceItem, destItem);

        controller.onContainerChanged();
        return true;
    }

    /**
     * Mueve un item dentro de la hotbar a un slot vacio.
     *
     * @param sourcePos  posicion de origen del item
     * @param destPos    posicion de destino para el item
     * @param sourceItem el item a mover
     */
    private void moveItemInHotbar(SlotPosition sourcePos, SlotPosition destPos, Item sourceItem) {
        container.getSlots().remove(sourcePos);
        container.getSlots().put(destPos, sourceItem);
        // Marca la posicion de destino como ocupada
        int destIndex = destPos.row() * container.getCols() + destPos.col();
        container.getOccupiedSlots().set(destIndex);
        // Marca la posicion de origen como libre
        int sourceIndex = sourcePos.row() * container.getCols() + sourcePos.col();
        container.getOccupiedSlots().clear(sourceIndex);
    }

    /**
     * Intercambia dos items dentro de la hotbar.
     *
     * @param sourcePos  posicion del primer item
     * @param destPos    posicion del segundo item
     * @param sourceItem el item en la posicion de origen
     * @param destItem   el item en la posicion de destino
     */
    private void swapItemInHotbar(SlotPosition sourcePos, SlotPosition destPos, Item sourceItem, Item destItem) {
        container.getSlots().put(sourcePos, destItem);
        container.getSlots().put(destPos, sourceItem);
    }

}