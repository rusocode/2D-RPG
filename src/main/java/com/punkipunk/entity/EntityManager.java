package com.punkipunk.entity;

import com.punkipunk.core.Game;
import com.punkipunk.entity.components.Particle;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.interactive.InteractiveFactory;
import com.punkipunk.entity.interactive.InteractiveType;
import com.punkipunk.entity.interactive.MetalPlate;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemFactory;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.mob.MobFactory;
import com.punkipunk.entity.mob.MobType;
import com.punkipunk.entity.player.Player;
import com.punkipunk.entity.spells.Spell;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

import static com.punkipunk.utils.Global.MAPS;

/**
 * Logica relacionada con la actualizacion y renderizado de entidades.
 * <p>
 * Con este nuevo sistema de renderizado de entidades no necesitas preocuparte por indices o espacios vacios. Las listas crecen y
 * se reducen dinamicamente. Por lo tanto son faciles de iterar y modificar.
 * <p>
 * Las entidades muertas se eliminan automaticamente, por lo tanto no hay necesidad de manejar nulls. Esto da un mejor rendimiento
 * al no iterar sobre espacios vacios.
 * <p>
 * El metodo stream() en Java es una caracteristica introducida en Java 8 que proporciona una forma de procesar colecciones de
 * datos de manera funcional y declarativa.
 * <p>
 * El metodo stream() convierte una coleccion (como List, Set o Array) en un Stream, que es una secuencia de elementos que soporta
 * operaciones agregadas y de transformacion. Estas operaciones pueden ser:
 * <p>
 * Operaciones intermedias (devuelven otro Stream):
 * <ul>
 * <li>{@code filter()}: Para filtrar elementos segun una condicion
 * <li>{@code map()}: Para transformar elementos
 * <li>{@code sorted()}: Para ordenar elementos
 * <li>{@code distinct()}: Para eliminar duplicados
 * </ul>
 * Operaciones terminales (producen un resultado final):
 * <ul>
 * <li{@code collect()}: Para acumular elementos en una nueva coleccion
 * <li>{@code forEach()}: Para realizar una accion con cada elemento
 * <li>{@code reduce()}: Para combinar elementos
 * <li>{@code count()}: Para contar elementos
 * </ul>
 * Por ejemplo:
 * <pre>{@code
 * plates = world.entities.getInteractives(world.map.num).stream()
 *                 .filter(interactive -> interactive instanceof MetalPlate && interactive.stats.name != null)
 *                 .toList();
 * }</pre>
 * <p>
 * Este ejemplo obtiene todas las entidades interactivas del mapa actual, las convierte en un Stream para filtrar solamente
 * aquellas que son placas metalicas y tienen un nombre asignado, y finalmente las convierte en una lista que se guarda en la
 * variable plates.
 * <p>
 * Las principales ventajas de usar streams son:
 * <ul>
 * <li>Permite escribir codigo mas declarativo y legible
 * <li>Facilita el procesamiento paralelo con parallelStream()
 * <li>Reduce la necesidad de bucles explicitos
 * <li>Proporciona operaciones de alto nivel para manipular colecciones
 * </ul>
 * <p>
 * El metodo {@code removeIf()} es un metodo que itera sobre la lista y elimina los elementos que cumplen con la condicion
 * especificada en el predicado. Es equivalente al siguiente codigo engorroso:
 * <pre>{@code
 * Iterator<Spell> iterator = spells.get(currentMap).iterator();
 *     while (iterator.hasNext()) {
 *         Spell spell = iterator.next();
 *         if (spell.flags.alive) spell.update();
 *         if (!spell.flags.alive) iterator.remove();
 * }
 * }</pre>
 */

public class EntityManager {

    public final Player player;
    public final List<Particle> particles = new ArrayList<>();
    /**
     * Mapas para almacenar entidades por tipo y numero de mapa, por lo tanto cada mapa del juego tendra su propia lista de
     * entidades (mas flexible y dinamico que arrays fijos).
     */
    private final Map<Integer, List<Mob>> mobs = new HashMap<>();
    private final Map<Integer, List<Item>> items = new HashMap<>();
    private final Map<Integer, List<Interactive>> interactives = new HashMap<>();
    private final Map<Integer, List<Spell>> spells = new HashMap<>();
    private final World world;
    private final MobFactory mobFactory;
    private final ItemFactory itemFactory;
    private final InteractiveFactory interactiveFactory;
    /**
     * Lista temporal para ordenar entidades durante el rendering, permitiendo ordenar las entidades por profundidad (coordenada
     * y). Se limpia y reconstruye en cada frame.
     */
    private final List<Entity> renderOrder = new ArrayList<>();

    public EntityManager(Game game, World world) {
        this.world = world;
        this.mobFactory = new MobFactory(game, world);
        this.itemFactory = new ItemFactory(game, world);
        this.interactiveFactory = new InteractiveFactory(game, world);
        this.player = new Player(game, world);
        for (int i = 0; i < MAPS; i++) {
            mobs.put(i, new ArrayList<>());
            items.put(i, new ArrayList<>());
            interactives.put(i, new ArrayList<>());
            spells.put(i, new ArrayList<>());
        }
    }

    public Item createItem(ItemType type, int mapNum, int... pos) {
        Item item = itemFactory.createEntity(type, pos);
        items.get(mapNum).add(item);
        return item;
    }

    public void createItemWithAmount(ItemType type, int mapNum, int amount, int... pos) {
        Item item = itemFactory.createEntityWithAmount(type, amount, pos);
        items.get(mapNum).add(item);
    }

    public Mob createMob(MobType type, int mapNum, int... pos) {
        Mob mob = mobFactory.createEntity(type, pos);
        mobs.get(mapNum).add(mob);
        return mob;
    }

    public void createInteractive(InteractiveType type, int mapNum, int... pos) {
        Interactive interactive = interactiveFactory.createEntity(type, pos);
        interactives.get(mapNum).add(interactive);
    }

    public void update() {
        int currentMap = world.map.num;

        player.update();

        mobs.get(currentMap).removeIf(mob -> {
            /* Cuando el mob muere, primero establece el estado dead en true para evitar que se mueva. Luego, genera la animacion
             * de muerte, y cuando termine, establece alive en false para que no genere movimiento y elimine el mob. */
            if (mob.flags.alive && !mob.flags.dead) mob.update();
            if (!mob.flags.alive) {
                mob.checkDrop(); // TODO Por que se comprueba desde aca?
                return true; // Remueve el mob
            }
            return false;
        });

        spells.get(currentMap).removeIf(spell -> {
            if (spell.flags.alive) spell.update();
            return !spell.flags.alive; // Retorna true (para remover) si el spell esta muerto
        });

        particles.removeIf(particle -> {
            if (particle.flags.alive) particle.update();
            return !particle.flags.alive;
        });

        interactives.get(currentMap).forEach(Interactive::update);
    }

    public void render(GraphicsContext g2) {
        int currentMap = world.map.num;
        renderOrder.clear();

        // Agrega las entidades interactivas menos la placa de metal
        interactives.get(currentMap).forEach(interactive -> {
            if (!(interactive instanceof MetalPlate)) renderOrder.add(interactive);
        });

        // Agrega el player si se puede dibujar
        /* if (player.drawing) */
        renderOrder.add(player);

        // Agrega solo los items solidos a la lista
        items.get(currentMap).forEach(item -> {
            if (item.solid) renderOrder.add(item);
        });

        renderOrder.addAll(mobs.get(currentMap));

        renderOrder.addAll(spells.get(currentMap));

        renderOrder.addAll(particles);

        /* Ordena la lista de entidades en funcion de la coordenada [y]. Es decir, si el player esta por encima del mob, entonces
         * el player se dibuja por debajo de este. Pero si el player esta por debajo del mob, entonces se dibuja por encima de
         * este. Lo mismo se aplica para las demas entidades, menos para los items no solidos. Esto se debe a que todas las
         * entidades estan en la misma lista y se ordenan de forma ascendente por la posicion de la coordenada [y] de cada entidad. */
        renderOrder.sort(Comparator.comparingInt(e -> (int) (e.position.y + e.hitbox.getY())));

        // Renderiza la placa de metal por debajo de todas las entidades
        interactives.get(currentMap).forEach(interactive -> {
            if (interactive instanceof MetalPlate) interactive.render(g2);
        });

        // Renderiza los items no solidos por debajo de las demas entidades
        items.get(currentMap).forEach(item -> {
            if (!item.solid) item.render(g2);
        });

        // Renderizar todas las entidades
        renderOrder.forEach(entity -> entity.render(g2));

    }

    public void removeItem(int mapNum, Item item) {
        items.get(mapNum).remove(item);
    }

    public void removeMob(int mapNum, Mob mob) {
        mobs.get(mapNum).remove(mob);
    }

    public void removeInteractive(int mapNum, Interactive interactive) {
        interactives.get(mapNum).remove(interactive);
    }

    public void replaceInteractive(int mapNum, Interactive oldInteractive, Interactive newInteractive) {
        List<Interactive> interactive = interactives.get(mapNum);
        int index = interactive.indexOf(oldInteractive);
        if (index != -1) interactive.set(index, newInteractive);
    }

    public void removeTempEntities() {
        for (int map = 0; map < MAPS; map++)
            items.get(map).removeIf(item -> item.temp);
    }

    public List<Mob> getMobs(int mapNum) {
        return mobs.get(mapNum);
    }

    public List<Item> getItems(int mapNum) {
        return items.get(mapNum);
    }

    public List<Spell> getSpells(int mapNum) {
        return spells.get(mapNum);
    }

    public List<Interactive> getInteractives(int mapNum) {
        return interactives.get(mapNum);
    }

    public void clearItems(int mapNum) {
        items.get(mapNum).clear();
    }

}
