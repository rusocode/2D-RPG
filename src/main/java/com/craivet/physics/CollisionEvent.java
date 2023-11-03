package com.craivet.physics;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.mob.Mob;

import java.awt.*;

import static com.craivet.utils.Global.*;

/**
 * Represents an event in the World. The events can be teleportation, talking to an npc, etc.
 * <p>
 * TODO Or could it just be called Event?
 */

public class CollisionEvent {

    private final Game game;
    private final World world;

    private final Rectangle[][][] event;
    private boolean canTouchEvent;
    /* The event does not happen again if the player is not 1 tile away. This mechanic prevents the event from repeating
     * itself in the same place repeatedly. */
    public int previousEventX, previousEventY;
    public int map, col, row;

    public CollisionEvent(Game game, World world) {
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
            if (checkCollision(NASHE, 27, 16, Direction.RIGHT)) hurt(entity);
            if (checkCollision(NASHE, 23, 12, Direction.UP)) heal(entity);
            if (checkCollision(NASHE, 10, 39, Direction.UP))
                teleport(INDOOR, NASHE_INDOOR_01, 12, 13); // De Nashe a Nashe Indoor 1
            if (checkCollision(NASHE_INDOOR_01, 12, 13, Direction.DOWN))
                teleport(OUTSIDE, NASHE, 10, 39); // De Nashe Indoor 1 a Nashe
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
            game.world.createMobs();
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
        game.state = TRANSITION_STATE;
        world.nextZone = zone;
        this.map = map;
        this.col = col;
        this.row = row;
        canTouchEvent = false;
    }

}
