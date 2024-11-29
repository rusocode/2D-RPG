package com.punkipunk.inventory;

import com.punkipunk.world.entity.item.Item;

import java.util.*;

/**
 * Maneja la logica fundamental del inventario.
 */

public abstract class Inventory {

    private final int rows;
    private final int cols;
    private final Map<InventoryPosition, Item> items; // Para acceso O(1) a items (unico item en cada posicion)
    private final Set<InventoryPosition> freePositions; // Para tracking de posiciones libres sin duplicados (asi que agregar una posicion que ya esta libre no causaria problemas)

    public Inventory(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.items = new HashMap<>();
        this.freePositions = new HashSet<>();
        initializeFreePositions();
    }

    /**
     * Inicializa las posiciones libres.
     */
    private void initializeFreePositions() {
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                freePositions.add(new InventoryPosition(row, col));
    }

    /**
     * Agrega el item a la coleccion de items en la primera posicion libre disponible.
     * <p>
     * El algoritmo comienza con un proceso sistematico de busqueda utilizando dos bucles anidados: el bucle exterior itera sobre
     * las filas y el interior sobre las columnas, lo que permite recorrer el inventario de manera ordenada desde la posicion
     * (0,0), avanzando de izquierda a derecha y de arriba hacia abajo. En cada iteracion, crea un objeto InventoryPosition con
     * las coordenadas actuales (row, col) y verifica si esa posicion esta libre. Cuando encuentra la primera posicion libre, la
     * almacena en la variable freePos y utiliza break para salir inmediatamente de ambos bucles, optimizando asi el proceso de
     * busqueda. Una vez encontrada la posicion libre, el algoritmo procede a agregar el item al Map usando put() con la posicion
     * encontrada como clave y el item como valor, y simultaneamente elimina esa posicion del conjunto de posiciones libres usando
     * remove().
     */
    public void add(Item item) {
        InventoryPosition freePos = null;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                InventoryPosition currentPos = new InventoryPosition(row, col); // Crea la posicion actual
                if (isPositionFree(currentPos)) {
                    freePos = currentPos; // Almacena la primera posicion libre encontrada
                    break; // Sale del bucle interno
                }
            }
            if (freePos != null) break; // Si ya encontro una posicion libre, sale del bucle externo
        }
        // Como las validaciones se hacen en canPickup(), aqui freePos nunca sera null
        items.put(freePos, item); // Agrega el item a la posicion libre
        freePositions.remove(freePos); // Remueve la posicion libre de las posiciones libres
    }

    /**
     * Elimina el item en la posicion especificada del inventario.
     */
    public void remove(int row, int col) {
        InventoryPosition pos = new InventoryPosition(row, col);
        items.remove(pos);
        freePositions.add(pos); // Agrega la posicion especificada a la lista de posiciones libres
    }

    /**
     * Obtiene el item en la posicion especificada del inventario.
     */
    public Item get(int row, int col) {
        return items.get(new InventoryPosition(row, col));
    }

    /**
     * Comprueba si la posicion actual esta libre.
     *
     * @param position posicion actual.
     * @return true si la posicion actual esta libre, false en caso contrario.
     */
    public boolean isPositionFree(InventoryPosition position) {
        return freePositions.contains(position);
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
        initializeFreePositions();
    }

    public Collection<Item> getAllItems() {
        return items.values();
    }

    public Map<InventoryPosition, Item> getItemPositions() {
        return new HashMap<>(items);
    }

}
