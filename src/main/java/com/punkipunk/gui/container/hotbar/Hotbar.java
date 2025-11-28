package com.punkipunk.gui.container.hotbar;

import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.player.Player;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.inventory.Inventory;
import com.punkipunk.world.World;

public class Hotbar extends Container {

    private final Inventory inventory;

    public Hotbar(IGame game, World world, Player player, Inventory inventory) {
        super(game, world, player, 1, 9); // TODO Usar constantes
        this.inventory = inventory;
    }

    @Override
    public void add(Item currentItem) {
        /* Si en el inventario hay un item stackable, entonces suma la cantidad del item actual al item stackable del
         * inventario. Esto se hace para evitar agregar el item stackable como un nuevo item a la hotbar. */
        if (inventory.isStackableSameItemInContainer(currentItem)) {
            inventory.add(currentItem);
            return;
        }
        if (canAddItem(currentItem)) super.add(currentItem); // Primero intenta agregar al hotbar
        else inventory.add(currentItem); // Si el hotbar está lleno, agrega al inventario
    }

}