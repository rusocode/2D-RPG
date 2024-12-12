package com.punkipunk.gui.container;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.world.entity.item.Item;
import javafx.geometry.Bounds;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * <p>
 * Gestiona los eventos del mouse.
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

    protected abstract void onItemClicked(MouseEvent event, StackPane slot, int row, int col, Tooltip tooltip);

    public void setEvents(StackPane slot, int row, int col) {
        Tooltip tooltip = createTooltip(slot, row, col);
        slot.setOnMouseClicked(event -> onItemClicked(event, slot, row, col, tooltip));
    }

    private Tooltip createTooltip(StackPane slot, int row, int col) {
        Tooltip tooltip = new Tooltip();
        tooltip.getStyleClass().add("tooltip");
        slot.setOnMouseMoved(event -> updateTooltip(tooltip, slot, event, row, col));
        slot.setOnMouseExited(event -> tooltip.hide());
        return tooltip;
    }

    private void updateTooltip(Tooltip tooltip, StackPane slot, MouseEvent event, int row, int col) {
        Item item = container.get(row, col);
        if (item != null) {
            tooltip.setText(item.stats.name);
            updateTooltipPosition(tooltip, slot, event);
        }
    }

    private void updateTooltipPosition(Tooltip tooltip, StackPane slot, MouseEvent event) {
        final int tooltipOffsetX = 5;
        final int tooltipOffsetY = 5;
        Bounds bounds = slot.localToScreen(slot.getBoundsInLocal());
        double x = bounds.getMinX() + event.getX() + bounds.getWidth() / 2 - tooltipOffsetX;
        double y = bounds.getMinY() + event.getY() - bounds.getHeight() / 2 - tooltipOffsetY;
        tooltip.show(slot, x, y);
    }

}
