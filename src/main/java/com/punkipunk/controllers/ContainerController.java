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
 * <p>
 * Clase abstracta que implementa la interfaz ContainerListener y proporciona la funcionalidad base para controlar contenedores en
 * la interfaz grafica. Maneja la creacion y actualizacion de slots, asi como sus eventos asociados.
 */

public abstract class ContainerController implements ContainerListener {

    /** Mapa que almacena las referencias a los componentes graficos (slots) indexados por su posicion en el contenedor */
    protected final Map<SlotPosition, StackPane> containerSlots = new HashMap<>();
    /** Referencia al contenedor que este controlador maneja */
    protected Container container;
    /** Manejador de eventos de arrastrar y soltar */
    protected ContainerDragDrop dragDrop;
    /** Manejador de eventos del mouse */
    protected ContainerMouse mouse;

    /** Panel de grilla que contiene los slots del contenedor */
    @FXML
    protected GridPane grid;

    /**
     * Metodo abstracto para crear el manejador de eventos de arrastrar y soltar.
     *
     * @return una implementacion de ContainerDragDrop especifica para el contenedor
     */
    protected abstract ContainerDragDrop createDragDrop();

    /**
     * Metodo abstracto para crear el manejador de eventos del mouse.
     *
     * @return una implementacion de ContainerMouse especifica para el contenedor
     */
    protected abstract ContainerMouse createMouse();

    /**
     * Implementacion del metodo de ContainerListener que se llama cuando el contenedor sufre cambios.
     */
    @Override
    public void onContainerChanged() {
        updateGrid();
    }

    /**
     * Inicializa el controlador con un contenedor especifico.
     * <p>
     * Crea los manejadores de eventos y configura la grilla inicial.
     *
     * @param container el contenedor a controlar
     */
    protected void initialize(Container container) {
        this.container = container;
        this.dragDrop = createDragDrop();
        this.mouse = createMouse();
        container.addListener(this);
        initializeGrid();
    }

    /**
     * Inicializa la grilla creando todos los slots necesarios y configurando sus eventos.
     */
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
     * Crea un slot individual con sus componentes graficos basicos.
     *
     * @param row fila del slot en el contenedor
     * @param col columna del slot en el contenedor
     * @return un nuevo StackPane configurado como slot
     * @throws IllegalArgumentException si la posicion es invalida
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
     * Configura los eventos de mouse y drag & drop para un slot.
     *
     * @param slot el StackPane a configurar
     * @param row  fila del slot
     * @param col  columna del slot
     */
    private void setSlotEvents(StackPane slot, int row, int col) {
        mouse.setEvents(slot, row, col);
        dragDrop.setEvents(slot, row, col);
    }

    /**
     * Actualiza el estado visual de todos los slots en la grilla.
     */
    private void updateGrid() {
        for (int row = 0; row < container.getRows(); row++)
            for (int col = 0; col < container.getCols(); col++)
                updateSlot(row, col);
    }

    /**
     * Actualiza el estado visual de un slot especifico.
     *
     * @param row fila del slot a actualizar
     * @param col columna del slot a actualizar
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

    /**
     * Actualiza un slot con la informacion de un item.
     *
     * @param slot el StackPane a actualizar
     * @param item el item cuya informacion se mostrara
     */
    private void updateSlotWithItem(StackPane slot, Item item) {
        ImageView imageView = (ImageView) slot.getChildren().get(0);
        Label amount = (Label) slot.getChildren().get(1);
        imageView.setImage(item.sheet.frame);
        amount.setText(item.amount > 1 ? String.valueOf(item.amount) : "");
    }

    /**
     * Limpia la informacion visual de un slot.
     *
     * @param slot el StackPane a limpiar
     */
    private void clearSlot(StackPane slot) {
        ImageView imageView = (ImageView) slot.getChildren().get(0);
        Label amount = (Label) slot.getChildren().get(1);
        imageView.setImage(null);
        amount.setText("");
    }

    /**
     * Obtiene el componente grafico de un slot en una posicion especifica.
     *
     * @param pos la posicion del slot a obtener
     * @return el StackPane en la posicion especificada
     */
    public StackPane getSlot(SlotPosition pos) {
        return containerSlots.get(pos);
    }

    public GridPane getGrid() {
        return grid;
    }

}
