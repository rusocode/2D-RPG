package com.punkipunk.entity;

import com.punkipunk.core.Game;
import com.punkipunk.entity.components.Particle;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.interactive.InteractiveFactory;
import com.punkipunk.entity.interactive.InteractiveID;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemFactory;
import com.punkipunk.entity.item.ItemID;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.mob.MobFactory;
import com.punkipunk.entity.mob.MobID;
import com.punkipunk.entity.player.Player;
import com.punkipunk.entity.spells.Spell;
import com.punkipunk.world.MapID;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

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
 * plates = world.entities.getInteractives(world.map.id).stream()
 *                 .filter(interactive -> interactive.getType() == InteractiveType.METAL_PLATE)
 *                 .toList();
 * }</pre>
 * <p>
 * Este ejemplo obtiene todas las entidades interactivas del mapa actual, las convierte en un Stream para filtrar solamente
 * aquellas que son placas metalicas, y finalmente las convierte en una lista que se guarda en la variable plates.
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

public class EntitySystem {

    public final Player player;
    public final List<Particle> particles = new ArrayList<>();
    /**
     * Colecciones de tipo Map para almacenar entidades por tipo y numero de mapa, por lo tanto cada mapa del juego tendra su
     * propia lista de entidades (mas flexible y dinamico que arrays fijos).
     */
    private final Map<MapID, List<Mob>> mobs = new EnumMap<>(MapID.class);
    private final Map<MapID, List<Item>> items = new EnumMap<>(MapID.class);
    private final Map<MapID, List<Interactive>> interactives = new EnumMap<>(MapID.class);
    private final Map<MapID, List<Spell>> spells = new EnumMap<>(MapID.class);
    private final World world;
    private final MobFactory mobFactory;
    private final ItemFactory itemFactory;
    private final InteractiveFactory interactiveFactory;
    /**
     * Lista temporal para ordenar entidades durante el rendering, permitiendo ordenar las entidades por profundidad (coordenada
     * y). Se limpia y reconstruye en cada frame.
     */
    private final List<Entity> renderOrder = new ArrayList<>();

    public EntityFactory entityFactory;

    public EntitySystem(Game game, World world) {
        this.world = world;
        this.mobFactory = new MobFactory(game, world);
        this.itemFactory = new ItemFactory(game, world);
        this.interactiveFactory = new InteractiveFactory(game, world);
        this.player = new Player(game, world);
        for (MapID mapId : MapID.values()) {
            mobs.put(mapId, new ArrayList<>());
            items.put(mapId, new ArrayList<>());
            interactives.put(mapId, new ArrayList<>());
            spells.put(mapId, new ArrayList<>());
        }

        // Debe crearse despues de crear MobFactory, ItemFactory, etc. para evitar un NPE
        this.entityFactory = new EntityFactory(game, world, this);

    }

    public void update() {
        MapID mapId = world.map.id;

        player.update();

        mobs.get(mapId).removeIf(mob -> {
            /* Cuando el mob muere, primero establece el estado dead en true para evitar que se mueva. Luego, genera la animacion
             * de muerte, y cuando termine, establece alive en false para que no genere movimiento y elimine el mob. */
            if (mob.flags.alive && !mob.flags.dead) mob.update();
            if (!mob.flags.alive) {
                mob.checkDrop(); // TODO Por que se comprueba desde aca?
                return true; // Remueve el mob
            }
            return false;
        });

        spells.get(mapId).removeIf(spell -> {
            if (spell.flags.alive) spell.update();
            return !spell.flags.alive; // Retorna true (para remover) si el spell esta muerto
        });

        particles.removeIf(particle -> {
            if (particle.flags.alive) particle.update();
            return !particle.flags.alive;
        });

        interactives.get(mapId).forEach(Interactive::update);
    }

    public void render(GraphicsContext context) {
        MapID mapId = world.map.id;
        renderOrder.clear();

        // Agrega las entidades interactivas menos la placa de metal
        interactives.get(mapId).forEach(interactive -> {
            if (!(interactive.getID() == InteractiveID.METAL_PLATE)) renderOrder.add(interactive);
        });

        // Agrega el player si se puede dibujar
        /* if (player.drawing) */
        renderOrder.add(player);

        // Agrega solo los items solidos a la lista
        items.get(mapId).forEach(item -> {
            if (item.solid) renderOrder.add(item);
        });

        renderOrder.addAll(mobs.get(mapId));

        renderOrder.addAll(spells.get(mapId));

        renderOrder.addAll(particles);

        /* Ordena la lista de entidades en funcion de la coordenada [y]. Es decir, si el player esta por encima del mob, entonces
         * el player se dibuja por debajo de este. Pero si el player esta por debajo del mob, entonces se dibuja por encima de
         * este. Lo mismo se aplica para las demas entidades, menos para los items no solidos. Esto se debe a que todas las
         * entidades estan en la misma lista y se ordenan de forma ascendente por la posicion de la coordenada [y] de cada entidad. */
        renderOrder.sort(Comparator.comparingInt(e -> (int) (e.position.y + e.hitbox.getY())));

        // Renderiza la placa de metal por debajo de todas las entidades
        interactives.get(mapId).forEach(interactive -> {
            if (interactive.getID() == InteractiveID.METAL_PLATE) interactive.render(context);
        });

        // Renderiza los items no solidos por debajo de las demas entidades
        items.get(mapId).forEach(item -> {
            if (!item.solid) item.render(context);
        });

        // Renderizar todas las entidades
        renderOrder.forEach(entity -> entity.render(context));

    }

    public Item createItem(ItemID itemId, MapID mapId, int... pos) {
        Item item = itemFactory.create(itemId, pos);
        items.get(mapId).add(item);
        return item;
    }

    public void createItemWithAmount(ItemID itemId, MapID mapId, int amount, int... pos) {
        Item item = itemFactory.createWithAmount(itemId, amount, pos);
        items.get(mapId).add(item);
    }

    public Mob createMob(MobID mobId, MapID mapId, int... pos) {
        Mob mob = mobFactory.create(mobId, pos);
        mobs.get(mapId).add(mob);
        return mob;
    }

    public void createInteractive(InteractiveID interactiveId, MapID mapId, int... pos) {
        interactives.get(mapId).add(interactiveFactory.create(interactiveId, pos));
    }

    public void replaceInteractive(MapID mapId, Interactive oldInteractive, Interactive newInteractive) {
        List<Interactive> interactive = interactives.get(mapId);
        int index = interactive.indexOf(oldInteractive);
        if (index != -1) interactive.set(index, newInteractive);
    }

    public void removeTempEntities() {
        for (MapID mapId : MapID.values())
            items.get(mapId).removeIf(item -> item.temp);
    }

    public void removeItem(MapID mapId, Item item) {
        items.get(mapId).remove(item);
    }

    public void removeMob(MapID mapId, Mob mob) {
        mobs.get(mapId).remove(mob);
    }

    public void removeInteractive(MapID mapId, Interactive interactive) {
        interactives.get(mapId).remove(interactive);
    }

    public void clearItems(MapID mapId) {
        items.get(mapId).clear();
    }

    public List<Mob> getMobs(MapID mapId) {
        return mobs.get(mapId);
    }

    public List<Item> getItems(MapID mapId) {
        return items.get(mapId);
    }

    public List<Spell> getSpells(MapID mapId) {
        return spells.get(mapId);
    }

    public List<Interactive> getInteractives(MapID mapId) {
        return interactives.get(mapId);
    }

}
