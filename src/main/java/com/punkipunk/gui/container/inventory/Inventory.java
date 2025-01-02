package com.punkipunk.gui.container.inventory;

import com.punkipunk.core.Game;
import com.punkipunk.entity.item.*;
import com.punkipunk.entity.player.Player;
import com.punkipunk.gui.container.Container;
import com.punkipunk.gui.container.equipment.Equipment;
import com.punkipunk.world.World;

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
    private final Equipment equipment;

    public Inventory(Game game, World world, Player player) {
        super(game, world, player, 3, 9);
        this.game = game;
        this.world = world;
        this.equipment = new Equipment(player);
        initializeDefaultItems();
    }

    public void initializeDefaultItems() {
        add(new StoneSword(game, world));
        add(new WoodShield(game, world));
        add(new PotionBlue(game, world, 1));
        add(new Key(game, world));
        add(new StoneAxe(game, world));
        add(new StonePickaxe(game, world));
        add(new StoneAxe(game, world));
    }

    public void equip(Item item) {
        equipment.equip(item);
    }

    public Equipment getEquipment() {
        return equipment;
    }

}
