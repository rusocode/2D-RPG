package com.punkipunk;

import com.punkipunk.world.entity.item.Item;

import java.util.*;

public class GridInventory {

    private final int rows;
    private final int cols;
    private final Map<GridPosition, Item> items; // Proporciona acceso rapido (O(1)) a un item dado por su posicion evitando duplicacion en posiciones (unico item en cada posicion)
    private final Set<GridPosition> freePositions; // El conjunto Set no permite duplicados, asi que agregar una posicion que ya esta libre no causaria problemas

    public GridInventory(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.items = new HashMap<>();
        this.freePositions = new HashSet<>();
        initializeFreePositions();
    }

    /**
     * Inicializa los espacios libres.
     */
    private void initializeFreePositions() {
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                freePositions.add(new GridPosition(row, col));
    }

    /**
     * Agrega el item a la coleccion de items en la primera posicion libre disponible.
     * <p>
     * El algoritmo comienza con un proceso sistematico de busqueda utilizando dos bucles anidados: el bucle exterior itera sobre
     * las filas y el interior sobre las columnas, lo que permite recorrer la cuadricula de manera ordenada desde la posicion
     * (0,0), avanzando de izquierda a derecha y de arriba hacia abajo. En cada iteracion, crea un objeto GridPosition con las
     * coordenadas actuales (row, col) y verifica si esa posicion esta disponible usando el metodo contains() del conjunto
     * freePositions. Cuando encuentra la primera posicion libre, la almacena en la variable pos y utiliza break para salir
     * inmediatamente de ambos bucles, optimizando asi el proceso de busqueda. Una vez encontrada la posicion libre, el algoritmo
     * procede a agregar el item al Map usando put() con la posicion encontrada como clave y el item como valor, y simultaneamente
     * elimina esa posicion del conjunto de posiciones libres usando remove().
     *
     * @param item item.
     */
    public void add(Item item) {
        GridPosition freePos = null; // Almacena la primera posicion libre encontrada
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                GridPosition currentPos = new GridPosition(row, col); // Crea una posicion con las coordenadas actuales
                // Verifica si la posicion actual esta libre
                if (freePositions.contains(currentPos)) {
                    freePos = currentPos; // Guarda la primera posicion libre encontrada
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
     * Agrega el item en la posicion especificada del inventario.
     * <p>
     * La diferencia principal con el metodo {@code add()} es que {@code addAt()} intenta agregar en una posicion especifica,
     * mientras que add() busca la primera posicion libre disponible.
     */
    public void addAt(Item item, int row, int col) {
        GridPosition pos = new GridPosition(row, col);
        items.put(pos, item);
        freePositions.remove(pos);
    }

    /**
     * Elimina el item en la posicion especificada del inventario.
     */
    public void remove(int row, int col) {
        GridPosition pos = new GridPosition(row, col);
        items.remove(pos);
        freePositions.add(pos);
    }

    /**
     * Obtiene el item en la posicion especificada del inventario.
     */
    public Item get(int row, int col) {
        return items.get(new GridPosition(row, col));
    }

    public boolean isFree(int row, int col) {
        return freePositions.contains(new GridPosition(row, col));
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

    public Map<GridPosition, Item> getItemPositions() {
        return new HashMap<>(items);
    }

}
