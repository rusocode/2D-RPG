package com.punkipunk.gui.container;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.world.entity.item.Item;
import javafx.geometry.Bounds;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Clase abstracta que implementa la funcionalidad de interaccion con el mouse para contenedores en una interfaz grafica JavaFX.
 * <p>
 * Esta clase maneja la logica base para mostrar tooltips y manejar clicks en los slots del contenedor.
 * <p>
 * Ahora la funcionalidad de tooltips es reutilizable por otros contenedores. Otros contenedores pueden implementar sus propias
 * acciones especificas sobrescribiendo {@code onItemClicked()}. Por ejemplo, en un inventario al hacer doble click sobre un item,
 * este se consume o equipa, y en una boveda, este se deposita.
 */

public abstract class ContainerMouse {

    protected final Container container;
    protected final ContainerController controller;

    public ContainerMouse(Container container, ContainerController controller) {
        this.container = container;
        this.controller = controller;
    }

    /**
     * Metodo abstracto que debe ser implementado para manejar la logica cuando se hace click en un item del contenedor.
     *
     * @param event   el evento del mouse que contiene informacion sobre la interaccion
     * @param slot    el StackPane donde se hizo click
     * @param row     la fila del slot en el contenedor
     * @param col     la columna del slot en el contenedor
     * @param tooltip el tooltip asociado al slot
     */
    protected abstract void onItemClicked(MouseEvent event, StackPane slot, int row, int col, Tooltip tooltip);

    /**
     * Configura los eventos del mouse para un slot especifico.
     * <p>
     * Crea un tooltip y configura el evento de click.
     *
     * @param slot el StackPane al que se le agregaran los eventos
     * @param row  la fila del slot en el contenedor
     * @param col  la columna del slot en el contenedor
     */
    public void setEvents(StackPane slot, int row, int col) {
        Tooltip tooltip = createTooltip(slot, row, col);
        slot.setOnMouseClicked(event -> onItemClicked(event, slot, row, col, tooltip));
    }

    /**
     * Crea y configura un tooltip para un slot especifico.
     * <p>
     * Configura los eventos de mouse para mostrar y ocultar el tooltip.
     *
     * @param slot el StackPane para el cual se creara el tooltip
     * @param row  la fila del slot en el contenedor
     * @param col  la columna del slot en el contenedor
     * @return el tooltip configurado
     */
    private Tooltip createTooltip(StackPane slot, int row, int col) {
        Tooltip tooltip = new Tooltip();
        tooltip.getStyleClass().add("tooltip");
        slot.setOnMouseMoved(event -> updateTooltip(tooltip, slot, event, row, col));
        slot.setOnMouseExited(event -> tooltip.hide());
        return tooltip;
    }

    /**
     * Actualiza el contenido del tooltip basado en el item presente en el slot.
     * <p>
     * Si hay un item, muestra su nombre y actualiza la posicion del tooltip.
     *
     * @param tooltip el tooltip a actualizar
     * @param slot    el StackPane asociado al tooltip
     * @param event   el evento del mouse que triggered la actualizacion
     * @param row     la fila del slot en el contenedor
     * @param col     la columna del slot en el contenedor
     */
    private void updateTooltip(Tooltip tooltip, StackPane slot, MouseEvent event, int row, int col) {
        Item item = container.get(row, col);
        if (item != null) {
            tooltip.setText(item.stats.name);
            updateTooltipPosition(tooltip, slot, event);
        }
    }

    /**
     * Actualiza la posicion del tooltip relativa al cursor del mouse.
     * <p>
     * Aplica un offset para mejorar la visibilidad.
     *
     * @param tooltip el tooltip a posicionar
     * @param slot    el StackPane asociado al tooltip
     * @param event   el evento del mouse que contiene las coordenadas actuales
     */
    private void updateTooltipPosition(Tooltip tooltip, StackPane slot, MouseEvent event) {
        final int tooltipOffsetX = 5;
        final int tooltipOffsetY = 5;
        Bounds bounds = slot.localToScreen(slot.getBoundsInLocal());
        double x = bounds.getMinX() + event.getX() + bounds.getWidth() / 2 - tooltipOffsetX;
        double y = bounds.getMinY() + event.getY() - bounds.getHeight() / 2 - tooltipOffsetY;
        tooltip.show(slot, x, y);
    }

}
