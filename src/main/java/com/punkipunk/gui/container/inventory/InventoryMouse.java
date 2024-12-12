package com.punkipunk.gui.container.inventory;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.ContainerMouse;
import com.punkipunk.gui.container.SlotPosition;
import com.punkipunk.gui.container.equipment.Equipment;
import com.punkipunk.gui.container.equipment.EquipmentStrategy;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;
import com.punkipunk.world.entity.item.ItemType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Implementa la logica del mouse para los items del inventario.
 * <p>
 * TODO Implementar la division de items para items apilables
 */

public class InventoryMouse extends ContainerMouse {

    private final Player player;
    private final Inventory inventory;

    public InventoryMouse(Container container, ContainerController controller, Player player) {
        super(container, controller);
        this.player = player;
        this.inventory = (Inventory) container; // Cast seguro ya que sabemos que es un Inventory
    }

    @Override
    protected void onItemClicked(MouseEvent event, StackPane slot, int row, int col, Tooltip tooltip) {
        if (event.getClickCount() == 2) {
            Item itemClicked = container.get(row, col);
            if (itemClicked != null) {
                switch (itemClicked.itemType) {
                    case CONSUMABLE -> consume(itemClicked, row, col, tooltip);
                    case AXE, SWORD, PICKAXE, SHIELD, LIGHT -> equip(itemClicked, slot);
                }
                controller.updateSlot(row, col); // TODO Deberia llamar a onContainerChanged()
            }
        }
    }

    private void consume(Item itemClicked, int row, int col, Tooltip tooltip) {
        inventory.consume(itemClicked, row, col);
        if (itemClicked.amount < 1) tooltip.hide();
    }

    private void equip(Item itemClicked, StackPane slot) {
        inventory.equip(itemClicked);
        updateSlotBackground(slot, itemClicked);
    }

    private void updateSlotBackground(StackPane slot, Item itemClicked) {
        clearSimilarSlotBackgrounds(itemClicked);
        updateSlotStyleClass(slot, isItemEquipped(itemClicked));
    }

    /**
     * Establece el estilo de la clase equipped al slot
     */
    private void updateSlotStyleClass(StackPane slot, boolean isEquipped) {
        if (isEquipped) slot.getStyleClass().add("equipped");
        else slot.getStyleClass().remove("equipped");
    }

    /**
     * Limpia los fondos de los items del mismo tipo
     */
    private void clearSimilarSlotBackgrounds(Item itemClicked) {
        for (int row = 0; row < container.getRows(); row++) {
            for (int col = 0; col < container.getCols(); col++) {
                Item itemIndexed = container.get(row, col);
                if (itemIndexed != null && isSameItemType(itemIndexed, itemClicked)) {
                    StackPane slot = controller.getSlot(new SlotPosition(row, col));
                    if (slot != null) slot.getStyleClass().remove("equipped");
                }
            }
        }
    }

    private boolean isItemEquipped(Item itemClicked) {
        Equipment equipment = inventory.getEquipment();
        EquipmentStrategy strategy = equipment.getStrategyForItemType(itemClicked.itemType);
        if (strategy == null) return false;
        return strategy.isEquipped(player, itemClicked);
    }

    private boolean isSameItemType(Item itemIndexed, Item itemClicked) {
        return (ItemType.WEAPON_TYPES.contains(itemClicked.itemType) &&
                ItemType.WEAPON_TYPES.contains(itemIndexed.itemType)) ||
                (itemClicked.itemType == ItemType.SHIELD &&
                        itemIndexed.itemType == ItemType.SHIELD) ||
                (itemClicked.itemType == ItemType.LIGHT &&
                        itemIndexed.itemType == ItemType.LIGHT);
    }

}
