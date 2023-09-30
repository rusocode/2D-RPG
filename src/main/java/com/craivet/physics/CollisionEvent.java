package com.craivet.physics;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.mob.Mob;

import java.awt.*;

import static com.craivet.utils.Global.*;

/**
 * Representa un evento en el World. Los eventos pueden ser teletransportacion, hablar con un npc, etc.
 * <p>
 * TODO O podria llamarse solo Event?
 */

public class CollisionEvent {

    private final Game game;
    private final World world;

    private final Rectangle[][][] event;
    private boolean canTouchEvent;
    /* El evento no sucede de nuevo si el player no se encuentra a 1 tile de distancia. Esta mecanica evita que el
     * evento se repita en el mismo lugar repetidamente. */
    public int previousEventX, previousEventY;
    public int map, col, row;

    public CollisionEvent(Game game, World world) {
        this.game = game;
        this.world = world;
        event = new Rectangle[MAPS][MAX_MAP_ROW][MAX_MAP_COL];
    }

    /**
     * Crea un evento para cada tile. Tecnicamente hablando, crea un rectangulo (Rectangle) pequeño en el centro de
     * cada tile.
     */
    public void createEvents() {
        for (int map = 0; map < MAPS; map++) {
            for (int row = 0; row < MAX_MAP_ROW; row++) {
                for (int col = 0; col < MAX_MAP_COL; col++)
                    event[map][row][col] = new Rectangle(5, 7, 22, 18);
            }
        }
    }

    /**
     * Verifica el evento.
     */
    public void checkEvent(Entity entity) {

        // Verifica si el player esta a mas de 1 tile de distancia del ultimo evento utilizando el evento previo como informacion
        int xDis = Math.abs(world.player.pos.x - previousEventX);
        int yDis = Math.abs(world.player.pos.y - previousEventY);
        int dis = Math.max(xDis, yDis);
        if (dis > tile) canTouchEvent = true;

        if (canTouchEvent) {
            if (checkCollision(NASHE, 27, 16, Direction.RIGHT)) hurt(entity);
            if (checkCollision(NASHE, 23, 12, Direction.UP)) heal(entity);
            if (checkCollision(NASHE_INDOOR_01, 12, 9, Direction.UP)) dialogue(world.mobs[1][0]);
            if (checkCollision(NASHE, 10, 39, Direction.UP))
                teleport(INDOOR, NASHE_INDOOR_01, 12, 13); // De Nashe a Nashe Indoor 1
            if (checkCollision(NASHE_INDOOR_01, 12, 13, Direction.DOWN))
                teleport(OUTSIDE, NASHE, 10, 39); // De Nix Indoor 1 a Nix
            if (checkCollision(NASHE, 12, 9, Direction.ANY))
                teleport(DUNGEON, DUNGEON_01, 9, 41); // De Nashe a Dungeon 1
            if (checkCollision(DUNGEON_01, 9, 41, Direction.ANY))
                teleport(OUTSIDE, NASHE, 12, 9); // De Dungeon 1 a Nashe
            if (checkCollision(DUNGEON_01, 8, 7, Direction.ANY))
                teleport(DUNGEON, DUNGEON_02, 26, 41); // De Dungeon 1 a Dungeon 2
            if (checkCollision(DUNGEON_02, 26, 41, Direction.ANY))
                teleport(DUNGEON, DUNGEON_01, 8, 7); // De Dungeon 2 a Dungeon 1
        }

    }

    /**
     * Verifica la colision con el evento.
     *
     * @param map       mapa del evento.
     * @param col       columna del evento.
     * @param row       fila del evento.
     * @param direction direccion del evento.
     * @return devuelve true si colisiono con el evento o false.
     */
    private boolean checkCollision(int map, int col, int row, Direction direction) {
        boolean isColliding = false;

        // Si el player esta en el mismo mapa que el evento
        if (map == world.map) {
            world.player.hitbox.x += world.player.pos.x;
            world.player.hitbox.y += world.player.pos.y;
            event[map][row][col].x += col * tile;
            event[map][row][col].y += row * tile;

            // Si el player colisiona con el evento y si la direccion coincide con la del evento
            if (world.player.hitbox.intersects(event[map][row][col]) && (world.player.direction == direction || direction == Direction.ANY)) {
                isColliding = true;
                world.player.attackCanceled = true; // Cancela el ataque en caso de interactuar con un evento usando enter (tecla que se utiliza para atacar)
                // En base a esta informacion verifica la distancia entre el player y el ultimo evento
                previousEventX = world.player.pos.x;
                previousEventY = world.player.pos.y;
            }

            // Resetea la posicion del hitbox del player y la posicion del evento
            world.player.hitbox.x = world.player.hitboxDefaultX;
            world.player.hitbox.y = world.player.hitboxDefaultY;
            event[map][row][col].x = 5;
            event[map][row][col].y = 7;
        }

        return isColliding;

    }

    /**
     * Daña al player.
     */
    private void hurt(Entity entity) {
        entity.dialogue.dialogues[0][0] = "You fall into a pit!";
        entity.startDialogue(DIALOGUE_STATE, entity, 0);
        world.player.stats.hp--;
        canTouchEvent = false;
    }

    /**
     * Sana al player.
     */
    private void heal(Entity entity) {
        if (game.keyboard.enter) {
            entity.dialogue.dialogues[1][0] = "You drink the water.\nYour life has been recovered.";
            entity.startDialogue(DIALOGUE_STATE, entity, 1);
            world.player.stats.hp = world.player.stats.maxHp;
            game.world.createMobs();
        }
    }

    /**
     * Teletransporta al player.
     *
     * @param zone zona a la que se teletransporta el player.
     * @param map  mapa al que se teletransporta el player.
     * @param col  columna al que se teletransporta el player.
     * @param row  fila al que se teletransporta el player.
     */
    private void teleport(int zone, int map, int col, int row) {
        game.state = TRANSITION_STATE;
        world.nextZone = zone;
        this.map = map;
        this.col = col;
        this.row = row;
        canTouchEvent = false;
    }

    /**
     * Dialoga con el Mob.
     *
     * @param mob mob con la que habla el player.
     */
    private void dialogue(Mob mob) {
        if (game.keyboard.enter) mob.dialogue();
    }

}
