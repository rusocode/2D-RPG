package com.punkipunk.gui.container;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.entity.item.Item;
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

/**
 * <p>
 * Clase abstracta que implementa la funcionalidad de arrastrar y soltar (drag and drop) para contenedores en una interfaz grafica
 * JavaFX.
 * <p>
 * Esta clase maneja la logica base para permitir el arrastre de elementos entre slots dentro de un contenedor.
 * <p>
 * TODO Agregar sonido de intercambio de items
 */

public abstract class ContainerDragDrop {

    /** El contenedor que almacena los items */
    protected final Container container;
    /** El controlador que maneja la logica del contenedor */
    protected final ContainerController controller;

    public ContainerDragDrop(Container container, ContainerController controller) {
        this.container = container;
        this.controller = controller;
    }

    /**
     * Metodo abstracto que debe ser implementado para manejar la logica cuando un item es soltado en un slot.
     *
     * @param event el evento de arrastre que contiene informacion sobre la operacion
     * @param slot  el StackPane donde se solto el item
     * @return true si la operacion de soltar fue exitosa, false en caso contrario
     */
    protected abstract boolean onItemDropped(DragEvent event, StackPane slot);

    /**
     * Configura todos los eventos de drag and drop para un slot especifico.
     *
     * @param slot el StackPane al que se le agregaran los eventos
     * @param row  la fila del slot en el contenedor
     * @param col  la columna del slot en el contenedor
     */
    public void setEvents(StackPane slot, int row, int col) {
        setDragDetected(slot, row, col);
        setDragOver(slot);
        setDragDropped(slot);
        setDragEnterExit(slot);
    }

    /**
     * Verifica si el evento de drag no contiene la informacion necesaria.
     *
     * @param event el evento de drag a validar
     * @return true si el evento no contiene la informacion necesaria, false en caso contrario
     */
    protected boolean isInvalidDrag(DragEvent event) {
        return !event.getDragboard().hasString();
    }

    /**
     * Extrae la informacion del drag and drop del evento y el slot de destino.
     * <p>
     * Convierte las coordenadas del string del dragboard a un objeto DragInfo.
     *
     * @param event el evento de drag que contiene las coordenadas
     * @param slot  el slot donde se solto el item
     * @return un objeto DragInfo con las coordenadas de origen y destino
     */
    protected DragInfo extractDragInfo(DragEvent event, StackPane slot) {
        String[] coords = event.getDragboard().getString().split(",");
        return new DragInfo(
                Integer.parseInt(coords[0]),
                Integer.parseInt(coords[1]),
                GridPane.getRowIndex(slot),
                GridPane.getColumnIndex(slot)
        );
    }

    /**
     * Configura el evento que se dispara cuando se inicia el arrastre de un item.
     * <p>
     * Crea una vista previa del item arrastrado y almacena la informacion de su posicion.
     *
     * @param slot el StackPane desde donde se inicia el arrastre
     * @param row  la fila del slot
     * @param col  la columna del slot
     */
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

    /**
     * Prepara el Dragboard para la operacion de arrastre.
     * <p>
     * Configura la vista previa del item que se esta arrastrando.
     *
     * @param slot el StackPane que contiene el item a arrastrar
     * @return el Dragboard configurado para la operacion
     */
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

    /**
     * Agrega el contenido necesario al Dragboard, en este caso las coordenadas del item que se esta arrastrando.
     *
     * @param dragboard el Dragboard donde se almacenara la informacion
     * @param row       la fila del item
     * @param col       la columna del item
     */
    private void addDragboardContent(Dragboard dragboard, int row, int col) {
        ClipboardContent content = new ClipboardContent();
        content.putString(row + "," + col);
        dragboard.setContent(content);
    }

    /**
     * Configura el evento que determina si un item puede ser soltado en un slot.
     * <p>
     * Acepta la operacion si el origen es diferente al destino y hay informacion valida.
     *
     * @param slot el StackPane que podria recibir el item
     */
    private void setDragOver(StackPane slot) {
        slot.setOnDragOver(event -> {
            if (event.getGestureSource() != slot && event.getDragboard().hasString())
                event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
    }

    /**
     * Configura el evento que se dispara cuando un item es soltado en un slot.
     * <p>
     * Delega la logica especifica al metodo abstracto onItemDropped.
     *
     * @param slot el StackPane donde se solto el item
     */
    private void setDragDropped(StackPane slot) {
        slot.setOnDragDropped(event -> {
            boolean success = onItemDropped(event, slot);
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * <p>
     * Configura los eventos visuales que se activan cuando un item esta siendo arrastrado sobre un slot. Agrega y remueve la
     * clase CSS 'drag' para feedback visual.
     *
     * @param slot el StackPane que recibira los eventos visuales
     */
    private void setDragEnterExit(StackPane slot) {
        slot.setOnDragEntered(event -> {
            if (event.getGestureSource() != slot) slot.getStyleClass().add("drag");
        });
        slot.setOnDragExited(event -> slot.getStyleClass().remove("drag"));
    }

    /**
     * Record que encapsula la informacion de origen y destino de una operacion de drag and drop.
     */
    protected record DragInfo(int sourceRow, int sourceCol, int destRow, int destCol) {
        @Override
        public String toString() {
            return String.format("Slot [%d,%d] to [%d,%d]", sourceRow, sourceCol, destRow, destCol);
        }
    }

}
