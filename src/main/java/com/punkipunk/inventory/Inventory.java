package com.punkipunk.inventory;

import com.punkipunk.Game;
import com.punkipunk.world.entity.item.Item;

import java.util.*;

/**
 * Logica fundamental del inventario.
 */

public abstract class Inventory {

    private final Game game;
    private final Map<SlotPosition, Item> items; // Para acceso O(1) a items (unico item en cada posicion)
    private final BitSet occupiedPositions; // Tracking de posiciones ocupadas
    private final int rows;
    private final int cols;

    public Inventory(Game game, int rows, int cols) {
        this.game = game;
        this.rows = rows;
        this.cols = cols;
        this.items = new HashMap<>();
        this.occupiedPositions = new BitSet(rows * cols);
    }

    /**
     * Agrega el item en la primera posicion libre del inventario.
     */
    public void add(Item item) {

        // Devuelve el indice del primer bit que se establece como false y que aparece en el indice inicial especificado o despues de el
        int freeIndex = occupiedPositions.nextClearBit(0);

        // Verifica si el inventario esta lleno
        if (freeIndex >= rows * cols) {
            System.out.println("Slot index " + freeIndex + " exceeded!");
            return;
        }

        // Convierte el indice plano a coordenadas de cuadricula
        int row = freeIndex / cols;
        int col = freeIndex % cols;

        SlotPosition freePos = new SlotPosition(row, col);

        items.put(freePos, item);
        occupiedPositions.set(freeIndex);
    }

    /**
     * Elimina el item en la posicion especificada del inventario.
     */
    public void remove(int row, int col) {
        SlotPosition pos = new SlotPosition(row, col);
        items.remove(pos);
        // Marca la posicion del item eliminado como libre en el BitSet
        int index = row * cols + col;
        occupiedPositions.clear(index);
    }

    /**
     * Obtiene el item en la posicion especificada del inventario.
     */
    public Item get(int row, int col) {
        return items.get(new SlotPosition(row, col));
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    /**
     * Verifica si se puede agregar el item al inventario.
     * <p>
     * Verifica si el inventario no esta lleno o si hay un item apilable igual al item actual en el inventario.
     *
     * @param currentItem item actual.
     * @return true si se puede agregar el item al inventario, false en caso contrario.
     */
    public boolean canAddItem(Item currentItem) {
        return size() < getRows() * getCols() || isStackableSameItemInInventory(currentItem);
    }

    /**
     * Verifica si hay un item apilable igual al item actual en el inventario.
     *
     * @param currentItem item actual.
     * @return true si hay un item apilable igual al item actual en el inventario, false en caso contrario.
     */
    private boolean isStackableSameItemInInventory(Item currentItem) {
        return getAllItems().stream().anyMatch(item -> item.stackable && item.stats.name.equals(currentItem.stats.name));
    }

    // TODO Se podria convinar con el metodo add()?
    public void updateOrAddItem(Item currentItem) {
        // Filtra el primer item que es apilable y igual al item actual
        Optional<Item> existingItem = getAllItems().stream().filter(item -> item.stackable && item.stats.name.equals(currentItem.stats.name)).findFirst();
        // Si esta presente, suma la cantidad del item actual al item existente
        if (existingItem.isPresent()) {
            existingItem.get().amount += currentItem.amount;
            notifyInventoryChanged();
            return;
        }

        // Agrega un nuevo item
        Item newItem = game.system.itemGenerator.generate(currentItem.stats.name);
        add(newItem);

        // Si el nuevo item es stackable, entonces ademas de crearlo, ajusta la cantidad
        if (currentItem.stackable) newItem.amount = currentItem.amount;

        notifyInventoryChanged();

    }

    /**
     * Obtiene solo los items.
     */
    public Collection<Item> getAllItems() {
        return items.values();
    }

    /**
     * Obtiene los items con sus posiciones especificas.
     */
    public Map<SlotPosition, Item> getItems() {
        return items;
    }

    public BitSet getOccupiedPositions() {
        return occupiedPositions;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Metodo abstracto para notificar cambios en el inventario. Cada subclase implementara este metodo segun sus necesidades.
     */
    protected abstract void notifyInventoryChanged();

}
