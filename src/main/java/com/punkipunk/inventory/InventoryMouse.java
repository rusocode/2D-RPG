package com.punkipunk.inventory;

import com.punkipunk.controllers.InventoryController;
import com.punkipunk.inventory.equipment.EquipmentStrategy;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;
import com.punkipunk.world.entity.item.ItemType;
import javafx.geometry.Bounds;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class InventoryMouse {

    private final Player player;
    private final InventoryController inventoryController;

    public InventoryMouse(Player player, InventoryController inventoryController) {
        this.player = player;
        this.inventoryController = inventoryController;
    }

    public void setEvents(StackPane slot, int row, int col) {
        Tooltip tooltip = createTooltip(slot, row, col);
        slot.setOnMouseClicked(event -> handleSlotClick(event, slot, row, col, tooltip));
    }

    private Tooltip createTooltip(StackPane slot, int row, int col) {
        Tooltip tooltip = new Tooltip();
        tooltip.getStyleClass().add("tooltip");
        slot.setOnMouseMoved(event -> updateTooltip(tooltip, slot, event, row, col));
        slot.setOnMouseExited(event -> tooltip.hide());
        return tooltip;
    }

    private void updateTooltip(Tooltip tooltip, StackPane slot, MouseEvent event, int row, int col) {
        Item item = player.inventory.get(row, col);
        if (item != null) {
            tooltip.setText(item.stats.name);
            updateTooltipPosition(tooltip, slot, event);
        }
    }

    private void updateTooltipPosition(Tooltip tooltip, StackPane slot, MouseEvent event) {
        final int tooltipOffsetX = 5;
        final int tooltipOffsetY = 5;
        // Obtiene las coordenadas del slot en la pantalla
        Bounds bounds = slot.localToScreen(slot.getBoundsInLocal());
        // Calcula la posicion del tooltip basada en los limites del slot y la posicion del mouse
        double x = bounds.getMinX() + event.getX() + bounds.getWidth() / 2 - tooltipOffsetX;
        double y = bounds.getMinY() + event.getY() - bounds.getHeight() / 2 - tooltipOffsetY;
        tooltip.show(slot, x, y);
    }

    private void handleSlotClick(MouseEvent event, StackPane slot, int row, int col, Tooltip tooltip) {
        if (event.getClickCount() == 2) {
            Item itemClicked = player.inventory.get(row, col);
            if (itemClicked != null) handleItemAction(itemClicked, row, col, slot, tooltip);
        }
    }

    private void handleItemAction(Item itemClicked, int row, int col, StackPane slot, Tooltip tooltip) {
        switch (itemClicked.itemType) {
            case CONSUMABLE -> handleConsumableItem(itemClicked, row, col, tooltip);
            case AXE, SWORD, PICKAXE, SHIELD, LIGHT -> handleEquipableItem(itemClicked, slot);
        }
        inventoryController.updateSlot(row, col);
    }

    private void handleConsumableItem(Item item, int row, int col, Tooltip tooltip) {
        player.inventory.consume(item, row, col);
        if (item.amount < 1) tooltip.hide();
    }

    private void handleEquipableItem(Item itemClicked, StackPane slot) {
        player.inventory.equip(itemClicked);
        updateSlotBackground(slot, itemClicked);
    }

    private void updateSlotBackground(StackPane slot, Item itemClicked) {
        clearSimilarSlotBackgrounds(itemClicked);
        updateSlotStyleClass(slot, isItemEquipped(itemClicked));
    }

    /**
     * <p>
     * Establece el estilo de la clase equipped al slot si el item clickeado esta equipado o remueve el estilo de la clase
     * equipped si no esta equipado.
     */
    private void updateSlotStyleClass(StackPane slot, boolean isEquipped) {
        if (isEquipped) slot.getStyleClass().add("equipped");
        else slot.getStyleClass().remove("equipped");
    }

    /**
     * Limpia los fondos de los items del mismo tipo.
     *
     * @param itemClicked item clickeado.
     */
    private void clearSimilarSlotBackgrounds(Item itemClicked) {
        for (int row = 0; row < player.inventory.getRows(); row++) {
            for (int col = 0; col < player.inventory.getCols(); col++) {
                Item itemIndexed = player.inventory.get(row, col);
                if (itemIndexed != null && isSameItemType(itemIndexed, itemClicked)) {
                    StackPane slot = inventoryController.getSlot(new SlotPosition(row, col));
                    if (slot != null) slot.getStyleClass().remove("equipped");
                }
            }
        }
    }

    /**
     * Verifica si el item clickeado esta equipado.
     *
     * @param itemClicked item clickeado del inventario.
     * @return true si el item clickeado esta equipado, false en caso contrario.
     */
    private boolean isItemEquipped(Item itemClicked) {
        // Obtiene la estrategia de equipamiento del item clickeado
        EquipmentStrategy strategy = player.inventory.getEquipment().getStrategyForItemType(itemClicked.itemType);
        if (strategy == null) return false;
        return strategy.isEquipped(player, itemClicked);
    }

    /**
     * Verifica si el item clickeado es igual al item indexado.
     *
     * @param itemIndexed item indexado del invetario.
     * @param itemClicked item clickeado del inventario.
     * @return true si el item clickeado es igual al item indexado, false en caso contrario.
     */
    private boolean isSameItemType(Item itemIndexed, Item itemClicked) {
        return (ItemType.WEAPON_TYPES.contains(itemClicked.itemType) && ItemType.WEAPON_TYPES.contains(itemIndexed.itemType)) ||
                (itemClicked.itemType == ItemType.SHIELD && itemIndexed.itemType == ItemType.SHIELD) ||
                (itemClicked.itemType == ItemType.LIGHT && itemIndexed.itemType == ItemType.LIGHT);
    }

}
