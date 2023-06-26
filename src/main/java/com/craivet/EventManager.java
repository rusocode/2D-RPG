package com.craivet;

import com.craivet.entity.Entity;

import static com.craivet.utils.Global.*;

public class EventManager {

    private final Game game;
    private final World world;
    Entity eventMaster;

    private final EventRect[][][] eventRect;

    /* El evento no sucede de nuevo si el player no se encuentra a 1 tile de distancia. Esta mecanica evita que el
     * evento se repita en el mismo lugar. */
    public int previousEventX, previousEventY;
    private boolean canTouchEvent = true;
    public int map, col, row;

    public EventManager(Game game, World world) {
        this.game = game;
        this.world = world;
        eventMaster = new Entity(game, world);
        eventRect = new EventRect[MAX_MAP][MAX_MAP_ROW][MAX_MAP_COL];

        // Crea un evento con area solida para cada tile
        for (int map = 0; map < MAX_MAP; map++) {
            for (int row = 0; row < MAX_MAP_ROW; row++) {
                for (int col = 0; col < MAX_MAP_COL; col++) {
                    eventRect[map][row][col] = new EventRect();
                    eventRect[map][row][col].x = 23;
                    eventRect[map][row][col].y = 23;
                    eventRect[map][row][col].width = 2;
                    eventRect[map][row][col].height = 2;
                    eventRect[map][row][col].eventRectDefaultX = eventRect[map][row][col].x;
                    eventRect[map][row][col].eventRectDefaultY = eventRect[map][row][col].y;
                }
            }
        }

        initDialogue();

    }

    /**
     * Verifica los eventos.
     */
    public void checkEvent() {

        // Verifica si el player esta a mas de 1 tile de distancia del ultimo evento
        int xDistance = Math.abs(world.player.x - previousEventX);
        int yDistance = Math.abs(world.player.y - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > tile_size) canTouchEvent = true;

        if (canTouchEvent) {
            // El else if se utiliza para evitar que el siguiente if se llame inmediatamente en el caso de la teleport
            if (hit(NIX, 27, 16, RIGHT)) damagePit();
            else if (hit(NIX, 23, 12, UP)) healingPool();
            else if (hit(NIX_INDOOR_01, 12, 9, ANY)) speak(world.npcs[1][0]);
            else if (hit(NIX, 10, 39, ANY)) teleport(INDOOR, NIX_INDOOR_01, 12, 13); // De Nix a Nix Indoor 1
            else if (hit(NIX_INDOOR_01, 12, 13, ANY)) teleport(OUTSIDE, NIX, 10, 39); // De Nix Indoor 1 a Nix
            else if (hit(NIX, 12, 9, ANY)) teleport(DUNGEON, DUNGEON_01, 9, 41); // De Nix a Dungeon 1
            else if (hit(DUNGEON_01, 9, 41, ANY)) teleport(OUTSIDE, NIX, 12, 9); // De Dungeon 1 a Nix
            else if (hit(DUNGEON_01, 8, 7, ANY)) teleport(DUNGEON, DUNGEON_02, 26, 41); // De Dungeon 1 a Dungeon 2
            else if (hit(DUNGEON_02, 26, 41, ANY)) teleport(DUNGEON, DUNGEON_01, 8, 7); // De Dungeon 2 a Dungeon 1
        }

    }

    /**
     * Verifica si el area solida del player colisiona con el evento de la posicion especificada.
     */
    private boolean hit(int map, int col, int row, int reqDirection) {
        boolean hit = false;

        if (map == world.map) {
            world.player.hitbox.x += world.player.x;
            world.player.hitbox.y += world.player.y;
            eventRect[map][row][col].x += col * tile_size;
            eventRect[map][row][col].y += row * tile_size;

            // Si el player colisiona con el evento
            if (world.player.hitbox.intersects(eventRect[map][row][col])) {
                if (world.player.direction == reqDirection || reqDirection == ANY) {
                    hit = true;
                    // En base a esta informacion, podemos verificar la distancia entre el player y el ultimo evento
                    previousEventX = world.player.x;
                    previousEventY = world.player.y;
                }
            }

            world.player.hitbox.x = world.player.hitboxDefaultX;
            world.player.hitbox.y = world.player.hitboxDefaultY;
            eventRect[map][row][col].x = eventRect[map][row][col].eventRectDefaultX;
            eventRect[map][row][col].y = eventRect[map][row][col].eventRectDefaultY;
        }

        return hit;

    }

    /**
     * Resta vida al player si callo en la foza.
     */
    private void damagePit() {
        game.state = PLAY_STATE;
        eventMaster.startDialogue(DIALOGUE_STATE, eventMaster, 0);
        world.player.HP--;
        canTouchEvent = false;
    }

    /**
     * Regenera vida al player si toma agua.
     */
    private void healingPool() {
        if (game.key.enter) {
            game.state = PLAY_STATE;
            world.player.attackCanceled = true; // No puede atacar si regenera vida
            eventMaster.startDialogue(DIALOGUE_STATE, eventMaster, 1);
            world.player.HP = world.player.maxHP;
            game.getWorld().createMOBs();
        }
    }

    private void teleport(int area, int map, int col, int row) {
        game.state = TRANSITION_STATE;
        world.nextArea = area;
        this.map = map;
        this.col = col;
        this.row = row;
        canTouchEvent = false;
    }

    private void speak(Entity entity) {
        if (game.key.enter) {
            world.player.attackCanceled = true;
            entity.speak();
        }
    }

    private void initDialogue() {
        eventMaster.dialogues[0][0] = "You fall into a pit!";
        eventMaster.dialogues[1][0] = "You drink the water.\nYour life has been recovered.";
    }

}
