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
 * <p>
 * Clase que implementa la funcionalidad especifica de arrastrar y soltar para el inventario. Extiende de ContainerDragDrop para
 * manejar las operaciones de movimiento y intercambio de items dentro del inventario.
 * <p>
 * TODO Agregar sonido de intercambio de items
 */

public class InventoryDragDrop extends ContainerDragDrop {

    public InventoryDragDrop(Container container, ContainerController controller) {
        super(container, controller);
    }

    /**
     * Implementa la logica cuando un item es soltado en un slot del inventario.
     * <p>
     * Verifica si el dragboard contiene la informacion necesaria y procede a mover o intercambiar los items segun corresponda.
     *
     * @param event el evento de arrastre que contiene la informacion de la operacion
     * @param slot  el StackPane donde se solto el item
     * @return true si la operacion fue exitosa, false en caso contrario
     */
    @Override
    protected boolean onItemDropped(DragEvent event, StackPane slot) {
        Dragboard dragboard = event.getDragboard();
        if (!dragboard.hasString()) return false;
        return moveOrSwapItems(dragboard, slot);
    }

    /**
     * Procesa el movimiento o intercambio de items en el inventario.
     * <p>
     * Extrae las coordenadas de origen y destino del dragboard y realiza la operacion correspondiente segun si el slot de destino
     * esta ocupado o no.
     *
     * @param dragboard el Dragboard que contiene las coordenadas del item origen
     * @param slot      el StackPane de destino donde se soltara el item
     * @return true si la operacion se completo exitosamente, false en caso contrario
     */
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

    /**
     * Mueve un item de una posicion a otra en el inventario.
     * <p>
     * Actualiza las estructuras de datos del contenedor y los bits de ocupacion para reflejar la nueva posicion del item.
     *
     * @param sourcePos  la posicion de origen del item
     * @param destPos    la posicion de destino para el item
     * @param sourceItem el item que se esta moviendo
     */
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

    /**
     * Intercambia dos items entre sus posiciones en el inventario.
     * <p>
     * Actualiza las posiciones de ambos items en el contenedor.
     *
     * @param sourcePos  la posicion del primer item
     * @param destPos    la posicion del segundo item
     * @param destItem   el item en la posicion de destino
     * @param sourceItem el item en la posicion de origen
     */
    private void swap(SlotPosition sourcePos, SlotPosition destPos, Item destItem, Item sourceItem) {
        container.getSlots().put(sourcePos, destItem);
        container.getSlots().put(destPos, sourceItem);
    }

}
