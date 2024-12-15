package com.punkipunk.gui.container.inventory;

import com.punkipunk.Game;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.equipment.Equipment;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.*;

/**
 * <p>
 * Clase que representa el inventario del jugador.
 * <p>
 * Extiende de Container y maneja los items que el jugador puede recolectar, equipar y consumir durante el juego. Tambien gestiona
 * el equipamiento del jugador.
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

    /**
     * Consume un item del inventario si es posible.
     * <p>
     * Si el item se puede usar, reduce su cantidad en 1.
     * <p>
     * Si la cantidad llega a 0, elimina el item del inventario.
     *
     * @param item el item a consumir
     * @param row  fila donde se encuentra el item en el inventario
     * @param col  columna donde se encuentra el item en el inventario
     */
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
