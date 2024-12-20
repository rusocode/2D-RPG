package com.punkipunk.controllers;

import com.punkipunk.gui.container.ContainerDragDrop;
import com.punkipunk.gui.container.ContainerMouse;
import com.punkipunk.gui.container.hotbar.HotbarDragDrop;
import com.punkipunk.gui.container.hotbar.HotbarMouse;
import com.punkipunk.world.entity.Player;

public class HotbarController extends ContainerController {

    private Player player;
    private InventoryController inventoryController;

    public void initialize(Player player, InventoryController inventoryController) {
        this.player = player;
        this.inventoryController = inventoryController;
        initialize(player.hotbar);
    }

    @Override
    protected ContainerDragDrop createDragDrop() {
        return new HotbarDragDrop(player.hotbar, this, player.inventory, inventoryController);
    }

    @Override
    protected ContainerMouse createMouse() {
        return new HotbarMouse(player.hotbar, this, player);
    }

}
