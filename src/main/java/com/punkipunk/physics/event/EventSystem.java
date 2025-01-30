package com.punkipunk.physics.event;

import com.punkipunk.Direction;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.player.Player;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.event.EventData;
import com.punkipunk.json.model.event.EventsConfig;
import com.punkipunk.json.model.event.TeleportTarget;
import com.punkipunk.world.MapID;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * Maneja la logica de deteccion y disparo de eventos.
 * <p>
 * Los eventos son areas especificas en el mapa que disparan acciones cuando el player interactua con ellas, como
 * teletransportacion, curacion, daño o escenas especiales.
 * <p>
 * El sistema maneja dos tipos principales de eventos:
 * <ul>
 *   <li>Eventos con verificacion de distancia - Requieren que el player se aleje antes de poder reactivarse</li>
 *   <li>Eventos sin verificacion de distancia - Se pueden activar repetidamente (como teletransportacion)</li>
 * </ul>
 */

public class EventSystem {

    private final World world;
    private final EventActions actions;
    /** Lista de eventos */
    private final List<Event> events = new ArrayList<>();
    private final JsonLoader jsonLoader = JsonLoader.getInstance();
    /** Indica si se puede disparar un evento */
    boolean canTriggerEvent;
    /** Posicion del ultimo evento activado */
    private Position previousEvent = new Position(0, 0);

    public EventSystem(Game game, World world) {
        this.world = world;
        this.actions = new EventActions(game, world);
        loadEvents();
    }

    private void loadEvents() {
        try {
            // Deserializa la configuracion completa
            EventsConfig config = jsonLoader.deserialize("events", EventsConfig.class); // TODO Cambiar nombre de mapas a "Abandoned Islan" por ejemplo
            // Procesa cada evento en la configuración
            config.events().forEach((key, eventData) -> createEvent(eventData));
        } catch (Exception e) {
            System.err.println("Error loading events: " + e.getMessage());
        }
    }

    private void createEvent(EventData config) {
        // Convierte strings a enums
        int mapId = getMapId(config.map()).ordinal();
        int x = config.x(), y = config.y();
        Direction direction = Direction.valueOf(config.direction());
        // Crea el evento segun su tipo
        Event event = switch (config.type()) {
            case "HURT" -> new Event(mapId, x, y, direction, actions::hurt);
            case "HEAL" -> new Event(mapId, x, y, direction, actions::heal);
            case "BOSS_SCENE" -> new Event(mapId, x, y, direction, e -> actions.bossScene());
            case "TELEPORT" -> createTeleportEvent(config);
            default -> throw new IllegalArgumentException("Unknown event type: " + config.type());
        };
        events.add(event);
    }

    private Event createTeleportEvent(EventData config) {
        TeleportTarget target = config.target();

        return new Event(
                getMapId(config.map()).ordinal(),
                config.x(),
                config.y(),
                Direction.valueOf(config.direction()),
                e -> actions.teleport(getMapId(target.map()), target.x(), target.y())
        );
    }

    private MapID getMapId(String mapName) {
        for (MapID map : MapID.values())
            if (map.name().equals(mapName)) return map;
        return MapID.ABANDONED_ISLAND;
    }

    /**
     * Verifica y dispara los eventos para una entidad.
     * <p>
     * El proceso ocurre en el siguiente orden:
     * <ol>
     * <li>Verifica si la entidad esta lo suficientemente lejos del ultimo evento activado
     * <li>Filtra los eventos que pueden ser disparados
     * <li>Busca el primer evento que colisiona con el jugador
     * <li>Si encuentra un evento valido, ejecuta su accion asociada
     * </ol>
     *
     * @param entity entidad para la que se verifican y disparan los eventos
     */
    public void checkAndTriggerEvents(Entity entity) {
        isEntityFarEnoughFromPreviousEvent(entity);
        events.stream()
                .filter(event -> canTriggerEvent)
                .filter(this::isPlayerCollidingEvent)
                .findFirst()
                .ifPresent(event -> event.action.execute(entity));
    }

    /**
     * Verifica si la entidad esta lo suficientemente lejos del evento anterior para poder activarlo de nuevo.
     */
    private void isEntityFarEnoughFromPreviousEvent(Entity entity) {
        Position currentPos = new Position(entity.position.x, entity.position.y);
        int xDistance = Math.abs(currentPos.x - previousEvent.x);
        int yDistance = Math.abs(currentPos.y - previousEvent.y);
        int maxDistance = Math.max(xDistance, yDistance);
        if (maxDistance > tile) canTriggerEvent = true;
    }

    /**
     * Verifica si el player esta colisionando con un evento.
     * <p>
     * Un evento puede ser activado cuando el player:
     * <ul>
     * <li>Esta en el mismo mapa que el evento
     * <li>Su hitbox colisiona con el area del evento
     * <li>Esta mirando en la direccion requerida (o el evento acepta cualquier direccion)
     * </ul>
     *
     * @param event evento
     * @return true si el player puede activar el evento
     */
    private boolean isPlayerCollidingEvent(Event event) {
        return event.map == world.map.id.ordinal() &&
                Optional.of(world.entitySystem.player)
                        .filter(player -> {

                            boolean colliding = event.area.isCollidingWithPlayer(player);
                            boolean correctDirection = event.direction() == Direction.ANY || event.direction() == player.direction;

                            if (colliding) {
                                player.attackCanceled = true;
                                previousEvent = new Position(player.position.x, player.position.y);
                            }

                            return colliding && correctDirection;
                        })
                        .isPresent();
    }

    /**
     * Define una accion de evento.
     */
    @FunctionalInterface
    private interface Action {
        void execute(Entity entity);
    }

    private record Position(int x, int y) {
    }

    private record Event(int map, int x, int y, Direction direction, Action action, Area area) {
        // Constructor que crea automaticamente el area del evento
        Event(int map, int x, int y, Direction direction, Action action) {
            this(map, x, y, direction, action, Area.fromTile(x, y));
        }
    }

    private record Area(int x, int y, int width, int height) {

        // Tamaño del area del evento
        static final int X_OFFSET = 5, Y_OFFSET = 7, WIDTH = 22, HEIGHT = 18;

        // Metodo factory para crear un area desde coordenadas de tile
        static Area fromTile(int x, int y) {
            return new Area(x * tile + X_OFFSET, y * tile + Y_OFFSET, WIDTH, HEIGHT);
        }

        // Metodo para verificar colision con el jugador
        boolean isCollidingWithPlayer(Player player) {
            Rectangle playerHitbox = new Rectangle(
                    player.position.x + player.hitbox.getX(),
                    player.position.y + player.hitbox.getY(),
                    player.hitbox.getWidth(),
                    player.hitbox.getHeight()
            );

            Rectangle eventArea = new Rectangle(x, y, width, height);
            return playerHitbox.intersects(eventArea.getBoundsInParent());
        }

    }

}