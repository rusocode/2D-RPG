package com.punkipunk.inventory;

import com.punkipunk.Game;
import com.punkipunk.inventory.equipment.Equipment;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Representa las funciones del inventario del player como equipar un item y consumir un item.
 * <p>
 * TODO Los streams pueden agregar una ligera sobrecarga, por lo que se debe realizar una evaluacion comparativa si el rendimiento
 * es fundamental. Para inventarios muy grandes, considere estructuras de datos mas especializadas
 * TODO Para obtener un mejor rendimiento con muchos listeners, considere usar un {@code CopyOnWriteArrayList} si los oyentes
 * pueden cambiar con frecuencia durante la iteracion.
 */

public class PlayerInventory extends Inventory {

    private final Game game;
    private final World world;
    private final Player player;
    private final Equipment equipment;
    private final List<InventoryListener> listeners = new ArrayList<>(); // Lista de observadores para notificar cambios

    public PlayerInventory(Game game, World world, Player player, int rows, int cols) {
        super(game, rows, cols);
        this.game = game;
        this.world = world;
        this.player = player;
        this.equipment = new Equipment(player);
    }

    @Override
    protected void notifyInventoryChanged() {
        listeners.forEach(InventoryListener::onInventoryChanged);
    }

    public void initializeDefaultItems() {
        add(new IronSword(game, world));
        add(new WoodShield(game, world));
        add(new PotionBlue(game, world, 1));
        add(new Key(game, world, 1));
        add(new IronAxe(game, world));
        add(new IronPickaxe(game, world));
    }

    public void equip(Item item) {
        equipment.equip(item);
    }

    public void consume(Item item, int row, int col) {
        if (item.use(player)) {
            item.amount--;
            if (item.amount <= 0) remove(row, col);
            notifyInventoryChanged();
        }
    }

    public void addListener(InventoryListener listener) {
        listeners.add(listener);
    }

    public Equipment getEquipment() {
        return equipment;
    }

}
