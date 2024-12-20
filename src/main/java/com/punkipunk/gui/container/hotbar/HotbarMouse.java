package com.punkipunk.gui.container.hotbar;

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

public class HotbarMouse extends ContainerMouse {

    private final Player player;

    public HotbarMouse(Container container, ContainerController controller, Player player) {
        super(container, controller);
        this.player = player;
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
                controller.updateSlot(row, col);
            }
        }
    }

    private void consume(Item itemClicked, int row, int col, Tooltip tooltip) {
        container.consume(itemClicked, row, col);
        if (itemClicked.amount < 1) tooltip.hide();
    }

    private void equip(Item itemClicked, StackPane slot) {
        player.inventory.equip(itemClicked);
        updateSlotBackground(slot, itemClicked);
        controller.onContainerChanged();
    }

    private void updateSlotBackground(StackPane slot, Item itemClicked) {
        clearSimilarSlotBackgrounds(itemClicked);
        updateSlotStyleClass(slot, isItemEquipped(itemClicked));
    }

    private void updateSlotStyleClass(StackPane slot, boolean isEquipped) {
        if (isEquipped) slot.getStyleClass().add("equipped");
        else slot.getStyleClass().remove("equipped");
    }

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
        Equipment equipment = player.inventory.getEquipment(); // hotbar.getEquipment();
        EquipmentStrategy strategy = equipment.getStrategyForItemType(itemClicked.itemType);
        if (strategy == null) return false;
        return strategy.isEquipped(player, itemClicked);
    }

    private boolean isSameItemType(Item itemIndexed, Item itemClicked) {
        return (ItemType.WEAPON.contains(itemClicked.itemType) &&
                ItemType.WEAPON.contains(itemIndexed.itemType)) ||
                (itemClicked.itemType == ItemType.SHIELD &&
                        itemIndexed.itemType == ItemType.SHIELD) ||
                (itemClicked.itemType == ItemType.LIGHT &&
                        itemIndexed.itemType == ItemType.LIGHT);
    }

}
