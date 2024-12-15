package com.punkipunk.gui.container;

import com.punkipunk.Game;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.item.*;

import java.util.*;

/**
 * <p>
 * Clase base abstracta que representa un contenedor de items en el juego. Gestiona una cuadricula de slots donde se pueden
 * almacenar items, controlando la ocupacion de espacios y el apilamiento de items similares.
 * <p>
 * TODO Para obtener un mejor rendimiento con muchos listeners, considere usar un {@code CopyOnWriteArrayList} si los oyentes
 * pueden cambiar con frecuencia durante la iteracion.
 * TODO Los streams pueden agregar una ligera sobrecarga, por lo que se debe realizar una evaluacion comparativa si el rendimiento
 * es fundamental. Para contenedores muy grandes, considere estructuras de datos mas especializadas
 */

public abstract class Container {

    private final Game game;
    private final World world;
    private final int rows;
    private final int cols;
    /** Registro de que slots estan ocupados en el contenedor */
    private final BitSet occupiedSlots;
    /** Coleccion para los datos del modelo (los items y sus posiciones) con acceso O(1) a slots (unico slot en cada posicion) */
    private final Map<SlotPosition, Item> slots = new HashMap<>();
    /** Lista de listeners que seran notificados cuando el contenedor cambie */
    private final List<ContainerListener> listeners = new ArrayList<>();

    public Container(Game game, World world, int rows, int cols) {
        this.game = game;
        this.world = world;
        this.rows = rows;
        this.cols = cols;
        this.occupiedSlots = new BitSet(rows * cols);
    }

    /**
     * Notifica a todos los listeners que el contenedor ha cambiado.
     */
    protected void notifyContainerChanged() {
        listeners.forEach(ContainerListener::onContainerChanged);
    }

    /**
     * Agrega un nuevo listener para cambios en el contenedor.
     *
     * @param listener el listener a agregar
     */
    public void addListener(ContainerListener listener) {
        listeners.add(listener);
    }

    /**
     * Agrega un item al contenedor.
     * <p>
     * Si existe un item apilable similar, incrementa su cantidad. Si no, busca el primer slot libre y coloca el item alli.
     *
     * @param currentItem el item a agregar
     */
    public void add(Item currentItem) {
        // Filtra el primer item del contenedor que es stackable y igual al item actual
        Optional<Item> existingItem = getSlotsValues().stream().filter(item -> item.stackable && item.stats.name.equals(currentItem.stats.name)).findFirst();

        // Si el item esta presente, suma la cantidad del item actual al item existente
        if (existingItem.isPresent()) existingItem.get().amount += currentItem.amount;
        else { // Agrega el item actual en la primera posicion libre del contenedor

            // Devuelve el indice del primer bit que se establece como false y que aparece en el indice inicial especificado o despues de el
            int freeIndex = occupiedSlots.nextClearBit(0);

            // Verifica si el contenedor esta lleno
            if (freeIndex >= rows * cols) {
                System.out.println("Slot index " + freeIndex + " exceeded!");
                return;
            }

            // Convierte el indice plano a coordenadas de cuadricula
            int row = freeIndex / cols;
            int col = freeIndex % cols;

            SlotPosition freePos = new SlotPosition(row, col);

            // Genera el nuevo item y ajusta la cantidad si es stackable
            Item newItem = generate(currentItem.stats.name);
            newItem.amount = currentItem.stackable ? currentItem.amount : 1;

            slots.put(freePos, newItem);
            occupiedSlots.set(freeIndex);
        }

        notifyContainerChanged();

    }

    /**
     * Elimina el item en la posicion especificada.
     *
     * @param row fila del item a eliminar
     * @param col columna del item a eliminar
     */
    public void remove(int row, int col) {
        SlotPosition pos = new SlotPosition(row, col);
        slots.remove(pos);
        // Marca la posicion del item eliminado como libre en el BitSet
        int index = row * cols + col;
        occupiedSlots.clear(index);

        notifyContainerChanged();
    }

    /**
     * Obtiene el item en la posicion especificada.
     *
     * @param row fila del item
     * @param col columna del item
     * @return el item en la posicion especificada o null si no hay ninguno
     */
    public Item get(int row, int col) {
        return slots.get(new SlotPosition(row, col));
    }

    /**
     * Devuelve el numero de items en el contenedor.
     *
     * @return cantidad de items almacenados
     */
    public int size() {
        return slots.size();
    }

    /**
     * Elimina todos los items del contenedor.
     */
    public void clear() {
        slots.clear();
    }

    /**
     * Verifica si se puede agregar el item al contenedor.
     * <p>
     * Verifica si el contenedor no esta lleno o si hay un item apilable igual al item actual en el contenedor.
     *
     * @param currentItem item actual.
     * @return true si se puede agregar el item al contenedor, false en caso contrario.
     */
    public boolean canAddItem(Item currentItem) {
        return size() < rows * cols || isStackableSameItemInContainer(currentItem);
    }

    /**
     * Verifica si hay un item apilable igual al item actual en el contenedor.
     *
     * @param currentItem item actual.
     * @return true si hay un item apilable igual al item actual en el contenedor, false en caso contrario.
     */
    private boolean isStackableSameItemInContainer(Item currentItem) {
        return getSlotsValues().stream().anyMatch(item -> item.stackable && item.stats.name.equals(currentItem.stats.name));
    }

    /**
     * Genera un nuevo item segun el nombre especificado.
     *
     * @param name nombre del item a generar
     * @return el nuevo item generado
     * @throws IllegalStateException si el nombre del item no es valido
     */
    public Item generate(String name) {
        return switch (name) {
            case IronAxe.NAME -> new IronAxe(game, world);
            case Boots.NAME -> new Boots(game, world);
            case Chest.NAME -> new Chest(game, world);
            case Chicken.NAME -> new Chicken(game, world);
            case Gold.NAME -> new Gold(game, world, 0);
            case Door.NAME -> new Door(game, world);
            case IronDoor.NAME -> new IronDoor(game, world);
            case Key.NAME -> new Key(game, world, 0);
            case Lantern.NAME -> new Lantern(game, world);
            case IronPickaxe.NAME -> new IronPickaxe(game, world);
            case PotionBlue.NAME -> new PotionBlue(game, world, 0);
            /* In the case of trading with the Trader and buying 1 at a time, it would be inefficient since 1 object is
             * created per potion, therefore it would be good for the player to be able to determine the quantity to buy.
             * It is important to pass 0 when it is generated to only create the object and not specify a quantity. */
            case PotionRed.NAME -> new PotionRed(game, world, 0);
            case IronShield.NAME -> new IronShield(game, world);
            case WoodShield.NAME -> new WoodShield(game, world);
            case Stone.NAME -> new Stone(game, world, 0);
            case IronSword.NAME -> new IronSword(game, world);
            case Tent.NAME -> new Tent(game, world);
            case Wood.NAME -> new Wood(game, world, 0);
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    /**
     * Obtiene todos los items almacenados en el contenedor.
     *
     * @return coleccion con todos los items
     */
    public Collection<Item> getSlotsValues() {
        return slots.values();
    }

    /**
     * Obtiene el mapa completo de posiciones e items.
     *
     * @return mapa con las posiciones y sus items correspondientes
     */
    public Map<SlotPosition, Item> getSlots() {
        return slots;
    }

    /**
     * Obtiene el registro de slots ocupados.
     *
     * @return BitSet con el registro de ocupacion
     */
    public BitSet getOccupiedSlots() {
        return occupiedSlots;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

}
