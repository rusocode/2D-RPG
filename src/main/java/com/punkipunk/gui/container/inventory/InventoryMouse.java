package com.punkipunk.gui.container.inventory;

import com.punkipunk.controllers.ContainerController;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.player.Player;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.ContainerMouse;
import com.punkipunk.gui.container.SlotPosition;
import com.punkipunk.gui.container.equipment.Equipment;
import com.punkipunk.gui.container.equipment.EquipmentStrategy;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * <p>
 * Clase que implementa la funcionalidad especifica de interaccion con el mouse para el inventario. Extiende de ContainerMouse
 * para manejar las operaciones de consumo y equipamiento de items mediante doble click.
 * <p>
 * TODO Implementar la division de items para items apilables
 */

public class InventoryMouse extends ContainerMouse {

    /** Referencia al jugador que posee el inventario */
    private final Player player;
    /** Referencia al jugador que posee el inventario */
    private final Inventory inventory;

    /**
     * Constructor que inicializa el sistema de interaccion con el mouse para el inventario.
     *
     * @param container  el contenedor que almacena los items del inventario
     * @param controller el controlador que maneja la logica del inventario
     * @param player     el jugador propietario del inventario
     */
    public InventoryMouse(Container container, ContainerController controller, Player player) {
        super(container, controller);
        this.player = player;
        this.inventory = (Inventory) container; // Cast seguro ya que sabemos que es un Inventory
    }

    /**
     * Implementa la logica cuando se hace click en un item del inventario.
     * <p>
     * Maneja el doble click para consumir o equipar items segun su tipo.
     *
     * @param event   el evento del mouse que contiene la informacion de la interaccion
     * @param slot    el StackPane donde se hizo click
     * @param row     la fila del slot en el contenedor
     * @param col     la columna del slot en el contenedor
     * @param tooltip el tooltip asociado al slot
     */
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

    /**
     * Consume un item del inventario y actualiza el tooltip si es necesario.
     *
     * @param itemClicked el item a consumir
     * @param row         la fila del item en el inventario
     * @param col         la columna del item en el inventario
     * @param tooltip     el tooltip a ocultar si el item se consume por completo
     */
    private void consume(Item itemClicked, int row, int col, Tooltip tooltip) {
        container.consume(itemClicked, row, col);
        if (itemClicked.amount < 1) tooltip.hide();
    }

    /**
     * Equipa un item al jugador y actualiza el fondo visual del slot.
     *
     * @param itemClicked el item a equipar
     * @param slot        el StackPane que contiene el item
     */
    private void equip(Item itemClicked, StackPane slot) {
        inventory.equip(itemClicked);
        updateSlotBackground(slot, itemClicked);
    }

    /**
     * Actualiza el fondo visual de un slot cuando un item es equipado, limpiando primero los fondos de items similares.
     *
     * @param slot        el StackPane a actualizar
     * @param itemClicked el item que fue equipado
     */
    private void updateSlotBackground(StackPane slot, Item itemClicked) {
        clearSimilarSlotBackgrounds(itemClicked);
        updateSlotStyleClass(slot, isItemEquipped(itemClicked));
    }

    /**
     * Actualiza la clase de estilo de un slot segun si el item esta equipado o no.
     *
     * @param slot       el StackPane a actualizar
     * @param isEquipped true si el item esta equipado, false en caso contrario
     */
    private void updateSlotStyleClass(StackPane slot, boolean isEquipped) {
        if (isEquipped) slot.getStyleClass().add("equipped");
        else slot.getStyleClass().remove("equipped");
    }

    /**
     * Limpia los fondos visuales de todos los slots que contienen items del mismo tipo que el item clickeado.
     *
     * @param itemClicked el item de referencia para buscar items similares
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

    /**
     * Verifica si un item esta actualmente equipado por el jugador.
     *
     * @param itemClicked el item a verificar
     * @return true si el item esta equipado, false en caso contrario
     */
    private boolean isItemEquipped(Item itemClicked) {
        Equipment equipment = inventory.getEquipment();
        EquipmentStrategy strategy = equipment.getStrategyForItemType(itemClicked.itemType);
        if (strategy == null) return false;
        return strategy.isEquipped(player, itemClicked);
    }

    /**
     * Determina si dos items son del mismo tipo de equipamiento (armas, escudo o luz).
     *
     * @param itemIndexed el item a comparar
     * @param itemClicked el item de referencia
     * @return true si los items son del mismo tipo, false en caso contrario
     */
    private boolean isSameItemType(Item itemIndexed, Item itemClicked) {
        return (ItemType.WEAPON.contains(itemClicked.itemType) &&
                ItemType.WEAPON.contains(itemIndexed.itemType)) ||
                (itemClicked.itemType == ItemType.SHIELD &&
                        itemIndexed.itemType == ItemType.SHIELD) ||
                (itemClicked.itemType == ItemType.LIGHT &&
                        itemIndexed.itemType == ItemType.LIGHT);
    }

}
