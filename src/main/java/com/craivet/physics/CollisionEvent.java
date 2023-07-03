package com.craivet.physics;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;

import java.awt.*;

import static com.craivet.utils.Global.*;

/**
 * Representa un evento en el World. Los eventos pueden ser una teletransportacion, hablar con un npc, etc.
 */

public class CollisionEvent {

    private final Game game;
    private final World world;
    private final Entity entity;

    private final Rectangle[][][] event;
    private boolean canTouchEvent;
    /* El evento no sucede de nuevo si el player no se encuentra a 1 tile de distancia. Esta mecanica evita que el
     * evento se repita en el mismo lugar repetidamente. */
    public int previousEventX, previousEventY;
    public int map, col, row;

    public CollisionEvent(Game game, World world) {
        this.game = game;
        this.world = world;
        entity = new Entity(game, world);
        event = new Rectangle[MAX_MAP][MAX_MAP_ROW][MAX_MAP_COL];
    }

    /**
     * Crea un evento para cada tile. Tecnicamente hablando, crea un rectangulo (Rectangle) pequeño en el centro de
     * cada tile.
     */
    public void createEvents() {
        for (int map = 0; map < MAX_MAP; map++) {
            for (int row = 0; row < MAX_MAP_ROW; row++) {
                for (int col = 0; col < MAX_MAP_COL; col++) {
                    event[map][row][col] = new Rectangle();
                    event[map][row][col].x = 23;
                    event[map][row][col].y = 23;
                    event[map][row][col].width = 2;
                    event[map][row][col].height = 2;
                }
            }
        }
        initDialogues();
    }

    /**
     * Verifica el evento.
     */
    public void checkEvent() {

        // Verifica si el player esta a mas de 1 tile de distancia del ultimo evento utilizando el evento previo como informacion
        int xDis = Math.abs(world.player.x - previousEventX);
        int yDis = Math.abs(world.player.y - previousEventY);
        int dis = Math.max(xDis, yDis);
        if (dis > tile_size) canTouchEvent = true;

        if (canTouchEvent) {
            if (checkCollision(NIX, 27, 16, RIGHT)) hurt();
            if (checkCollision(NIX, 23, 12, UP)) heal();
            if (checkCollision(NIX_INDOOR_01, 12, 9, ANY)) speak(world.npcs[1][0]);
            if (checkCollision(NIX, 10, 39, ANY)) teleport(INDOOR, NIX_INDOOR_01, 12, 13); // De Nix a Nix Indoor 1
            if (checkCollision(NIX_INDOOR_01, 12, 13, ANY))
                teleport(OUTSIDE, NIX, 10, 39); // De Nix Indoor 1 a Nix
            if (checkCollision(NIX, 12, 9, ANY)) teleport(DUNGEON, DUNGEON_01, 9, 41); // De Nix a Dungeon 1
            if (checkCollision(DUNGEON_01, 9, 41, ANY)) teleport(OUTSIDE, NIX, 12, 9); // De Dungeon 1 a Nix
            if (checkCollision(DUNGEON_01, 8, 7, ANY))
                teleport(DUNGEON, DUNGEON_02, 26, 41); // De Dungeon 1 a Dungeon 2
            if (checkCollision(DUNGEON_02, 26, 41, ANY))
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
    private boolean checkCollision(int map, int col, int row, int direction) {
        boolean isColliding = false;

        // Si el player esta en el mismo mapa que el evento
        if (map == world.map) {
            world.player.hitbox.x += world.player.x;
            world.player.hitbox.y += world.player.y;
            event[map][row][col].x += col * tile_size;
            event[map][row][col].y += row * tile_size;

            // Si el player colisiona con el evento y si la direccion coincide con la del evento
            if (world.player.hitbox.intersects(event[map][row][col]) && (world.player.direction == direction || direction == ANY)) {
                isColliding = true;
                world.player.attackCanceled = true; // Cancela el ataque en caso de interactuar con un evento usando enter (tecla que se utiliza para atacar)
                // En base a esta informacion verifica la distancia entre el player y el ultimo evento
                previousEventX = world.player.x;
                previousEventY = world.player.y;
            }

            // Resetea la posicion del hitbox del player y la posicion del evento
            world.player.hitbox.x = world.player.hitboxDefaultX;
            world.player.hitbox.y = world.player.hitboxDefaultY;
            event[map][row][col].x = 23;
            event[map][row][col].y = 23;
        }

        return isColliding;

    }

    /**
     * Daña al player.
     */
    private void hurt() {
        entity.startDialogue(DIALOGUE_STATE, entity, 0);
        world.player.HP--;
        canTouchEvent = false;
    }

    /**
     * Sana al player.
     */
    private void heal() {
        if (game.keyboard.enter) {
            entity.startDialogue(DIALOGUE_STATE, entity, 1);
            world.player.HP = world.player.maxHP;
            game.world.createMOBs();
        }
    }

    /**
     * Teletransporta al player.
     *
     * @param area area al que se teletransporta el player.
     * @param map  mapa al que se teletransporta el player.
     * @param col  columna al que se teletransporta el player.
     * @param row  fila al que se teletransporta el player.
     */
    private void teleport(int area, int map, int col, int row) {
        game.state = TRANSITION_STATE;
        world.nextArea = area;
        this.map = map;
        this.col = col;
        this.row = row;
        canTouchEvent = false;
    }

    /**
     * Habla con la entidad.
     *
     * @param entity entidad con la que habla el player.
     */
    private void speak(Entity entity) {
        if (game.keyboard.enter) entity.speak();
    }

    private void initDialogues() {
        entity.dialogues[0][0] = "You fall into a pit!";
        entity.dialogues[1][0] = "You drink the water.\nYour life has been recovered.";
    }

}
