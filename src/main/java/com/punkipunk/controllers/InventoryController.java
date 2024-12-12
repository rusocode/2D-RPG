package com.punkipunk.controllers;

import com.punkipunk.gui.container.ContainerDragDrop;
import com.punkipunk.gui.container.ContainerMouse;
import com.punkipunk.gui.container.inventory.InventoryDragDrop;
import com.punkipunk.gui.container.inventory.InventoryMouse;
import com.punkipunk.world.entity.Player;

public class InventoryController extends ContainerController {

    private Player player;

    public void initialize(Player player) {
        this.player = player;
        initialize(player.inventory);
    }

    @Override
    protected ContainerDragDrop createDragDrop() {
        return new InventoryDragDrop(player.inventory, this);
    }

    @Override
    protected ContainerMouse createMouse() {
        return new InventoryMouse(player.inventory, this, player);
    }

}

