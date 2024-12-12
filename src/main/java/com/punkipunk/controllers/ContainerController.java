package com.punkipunk.controllers;

import com.punkipunk.gui.container.*;
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
 * Define la estructura base del controlador del contenedor.
 */

public abstract class ContainerController implements ContainerListener {

    /* Es la coleccion que maneja los elementos visuales de la UI. Almacena las referencias a los componentes graficos que
     * representan cada slot. */
    protected final Map<SlotPosition, StackPane> containerSlots = new HashMap<>();
    protected Container container;
    protected ContainerDragDrop dragDrop;
    protected ContainerMouse mouse;

    @FXML
    protected GridPane grid;

    protected abstract ContainerDragDrop createDragDrop();

    protected abstract ContainerMouse createMouse();

    @Override
    public void onContainerChanged() {
        updateGrid(); // Actualiza la UI cuando cambia el contenedor, ahora cualquier tipo de contenedor puede notificar cambios de manera consistente
    }

    protected void initialize(Container container) {
        this.container = container;
        this.dragDrop = createDragDrop();
        this.mouse = createMouse();
        container.addListener(this);
        initializeGrid();
    }

    private void initializeGrid() {
        for (int row = 0; row < container.getRows(); row++) {
            for (int col = 0; col < container.getCols(); col++) {
                StackPane slot = createSlot(row, col);
                containerSlots.put(new SlotPosition(row, col), slot);
                setSlotEvents(slot, row, col);
                grid.add(slot, col, row);
            }
        }
    }

    /**
     * Crea un slot individual.
     */
    private StackPane createSlot(int row, int col) {
        if (row < 0 || row >= container.getRows() || col < 0 || col >= container.getCols())
            throw new IllegalArgumentException("Invalid slot position");

        StackPane slot = new StackPane();
        slot.getStyleClass().add("slot");

        ImageView image = new ImageView();
        image.getStyleClass().add("image");

        Label amount = new Label();
        amount.getStyleClass().add("amount");

        slot.getChildren().addAll(image, amount);

        Item item = container.get(row, col);
        if (item != null) {
            image.setImage(item.sheet.frame);
            amount.setText(item.amount > 1 ? String.valueOf(item.amount) : "");
        }

        return slot;
    }

    /**
     * Configura los eventos del slot.
     */
    private void setSlotEvents(StackPane slot, int row, int col) {
        mouse.setEvents(slot, row, col);
        dragDrop.setEvents(slot, row, col);
    }

    /**
     * Actualiza toda la grilla.
     */
    private void updateGrid() {
        for (int row = 0; row < container.getRows(); row++)
            for (int col = 0; col < container.getCols(); col++)
                updateSlot(row, col);
    }

    /**
     * Actualiza un slot especifico.
     */
    public void updateSlot(int row, int col) {
        Optional.ofNullable(containerSlots.get(new SlotPosition(row, col)))
                .ifPresent(slot -> Optional.ofNullable(container.get(row, col))
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
     */
    public StackPane getSlot(SlotPosition pos) {
        return containerSlots.get(pos);
    }

}
