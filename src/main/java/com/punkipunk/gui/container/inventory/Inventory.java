package com.punkipunk.gui.container.inventory;

import com.punkipunk.Game;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.equipment.Equipment;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.*;

/**
 * Funciones especificas del inventario como equipar o consumir un item.
 */

public class Inventory extends Container {

    private final Game game;
    private final World world;
    private final Player player;
    private final Equipment equipment;

    public Inventory(Game game, World world, Player player, int rows, int cols) {
        super(game, world, rows, cols);
        this.game = game;
        this.world = world;
        this.player = player;
        this.equipment = new Equipment(player);
        initializeDefaultItems();
    }

    public void initializeDefaultItems() {
        add(new IronSword(game, world));
        add(new WoodShield(game, world));
        add(new PotionBlue(game, world, 1));
        add(new Key(game, world, 1));
        add(new IronAxe(game, world));
        add(new IronPickaxe(game, world));
        add(new IronAxe(game, world));
    }

    public void equip(Item item) {
        equipment.equip(item);
    }

    public void consume(Item item, int row, int col) {
        if (item.use(player)) {
            item.amount--;
            if (item.amount <= 0) remove(row, col);
        }
    }

    public Equipment getEquipment() {
        return equipment;
    }

}
