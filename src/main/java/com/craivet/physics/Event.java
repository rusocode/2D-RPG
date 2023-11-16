package com.craivet.physics;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.io.Progress;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;

import java.awt.*;

import static com.craivet.utils.Global.*;

/**
 * Represents an event in the World. The events can be teleportation, talking to an npc, etc.
 */

public class Event {

    private final Game game;
    private final World world;

    private final Rectangle[][][] event;
    private boolean canTouchEvent;
    /* Variable para saber cuando el player esta dentro de la boss area para evitar que se produsca el mismo evento cada
     * ves que pasa por ese evento. Solo se vuelve a desactivar cuando muere en la boss area. */
    public boolean inBoss;
    /* The event does not happen again if the player is not 1 tile away. This mechanic prevents the event from repeating
     * itself in the same place repeatedly. */
    public int previousEventX, previousEventY;
    public int map, col, row;

    public Event(Game game, World world) {
        this.game = game;
        this.world = world;
        event = new Rectangle[MAPS][MAX_MAP_ROW][MAX_MAP_COL];
    }

    /**
     * Create an event for each tile. Technically speaking, it creates a small Rectangle in the center of each tile.
     */
    public void createEvents() {
        for (int map = 0; map < MAPS; map++)
            for (int row = 0; row < MAX_MAP_ROW; row++)
                for (int col = 0; col < MAX_MAP_COL; col++)
                    event[map][row][col] = new Rectangle(5, 7, 22, 18);
    }

    /**
     * Check the event.
     */
    public void checkEvent(Entity entity) {

        // Check if the player is more than 1 tile away from the last event using the previous event as information
        int xDis = Math.abs(world.player.pos.x - previousEventX);
        int yDis = Math.abs(world.player.pos.y - previousEventY);
        int dis = Math.max(xDis, yDis);
        if (dis > tile) canTouchEvent = true;

        if (canTouchEvent) {
            if (checkCollision(ABANDONED_ISLAND, 27, 16, Direction.RIGHT)) hurt(entity);
            if (checkCollision(ABANDONED_ISLAND, 23, 12, Direction.UP)) heal(entity);
            if (checkCollision(DUNGEON_BREG_SUB, 25, 27, Direction.ANY)) skeleton();
            if (checkCollision(ABANDONED_ISLAND, 10, 39, Direction.UP))
                teleport(MARKET, ABANDONED_ISLAND_MARKET, 12, 13); // From Abandoned Island to Abandoned Island Market
            if (checkCollision(ABANDONED_ISLAND_MARKET, 12, 13, Direction.DOWN))
                teleport(OVERWORLD, ABANDONED_ISLAND, 10, 39); // From Abandoned Island Market to Abandoned Island
            if (checkCollision(ABANDONED_ISLAND, 12, 9, Direction.ANY))
                teleport(DUNGEON, DUNGEON_BREG, 9, 41); // From Abandoned Island to Dungeon Breg
            if (checkCollision(DUNGEON_BREG, 9, 41, Direction.ANY))
                teleport(OVERWORLD, ABANDONED_ISLAND, 12, 9); // From Dungeon Breg to Abandoned Island
            if (checkCollision(DUNGEON_BREG, 8, 7, Direction.ANY))
                teleport(BOSS, DUNGEON_BREG_SUB, 26, 41); // From Dungeon Breg to Dungeon Breg Sub
            if (checkCollision(DUNGEON_BREG_SUB, 26, 41, Direction.ANY))
                teleport(DUNGEON, DUNGEON_BREG, 8, 7); // From Dungeon Breg Sub to Dungeon Breg
        }

    }

    /**
     * Check the collision with the event.
     *
     * @param map       map of the event.
     * @param col       event column.
     * @param row       event row.
     * @param direction direction of the event.
     * @return returns true if I collide with the event or false.
     */
    private boolean checkCollision(int map, int col, int row, Direction direction) {
        boolean isColliding = false;

        // If the player is on the same map as the event
        if (map == world.map) {
            world.player.hitbox.x += world.player.pos.x;
            world.player.hitbox.y += world.player.pos.y;
            event[map][row][col].x += col * tile;
            event[map][row][col].y += row * tile;

            // If the player collides with the event and if the direction matches that of the event
            if (world.player.hitbox.intersects(event[map][row][col]) && (world.player.direction == direction || direction == Direction.ANY)) {
                isColliding = true;
                world.player.attackCanceled = true; // Cancels the attack if you interact with an event using enter (key used to attack)
                // Based on this information, verify the distance between the player and the last event
                previousEventX = world.player.pos.x;
                previousEventY = world.player.pos.y;
            }

            // Resets the player hitbox position and event position
            world.player.hitbox.x = world.player.hitboxDefaultX;
            world.player.hitbox.y = world.player.hitboxDefaultY;
            event[map][row][col].x = 5;
            event[map][row][col].y = 7;
        }

        return isColliding;

    }

    /**
     * Hurt the player.
     */
    private void hurt(Entity entity) {
        entity.dialogue.dialogues[0][0] = "You fall into a pit!";
        entity.dialogue.startDialogue(DIALOGUE_STATE, entity, 0);
        world.player.stats.hp--;
        canTouchEvent = false;
    }

    /**
     * Heals the player.
     */
    private void heal(Entity entity) {
        if (game.keyboard.enter) {
            entity.dialogue.dialogues[1][0] = "You drink the water.\nYour life has been recovered.";
            entity.dialogue.startDialogue(DIALOGUE_STATE, entity, 1);
            world.player.stats.hp = world.player.stats.maxHp;
        }
    }

    private void skeleton() {
        if (!world.bossBattleOn && !Progress.skeletonDefeated && !inBoss) {
            game.state = CUTSCENE_STATE;
            world.cutscene.sceneNum = world.cutscene.skeleton;
            inBoss = true;
        }
    }

    /**
     * Teleports the player.
     *
     * @param zone zone to which the player teleports.
     * @param map  map to which the player teleports.
     * @param col  column to which the player teleports.
     * @param row  row to which the player teleports.
     */
    private void teleport(int zone, int map, int col, int row) {
        game.state = TELEPORT_STATE;
        world.nextZone = zone;
        this.map = map;
        this.col = col;
        this.row = row;
        canTouchEvent = false;
    }

}
