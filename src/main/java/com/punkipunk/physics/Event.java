package com.punkipunk.physics;

import com.punkipunk.Direction;
import com.punkipunk.Game;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.io.Progress;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Entity;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

/**
 * Represents an event in the World. The events can be teleportation, heal, etc.
 */

public class Event {

    private final Game game;
    private final World world;

    private final Rectangle[][][] event;
    // x and y position of the event
    private final int x = 5;
    private final int y = 7;
    /* The event does not happen again if the player is not 1 tile away. This mechanic prevents the event from repeating
     * itself in the same place repeatedly. */
    public int previousEventX, previousEventY;
    public int mapNum, col, row;
    // Avoid generate the event in the same place multiple times
    private boolean canCollideEvent;

    public Event(Game game, World world) {
        this.game = game;
        this.world = world;
        event = new Rectangle[MAPS][MAX_MAP_ROW][MAX_MAP_COL];
    }

    /**
     * Create an event for each tile. Technically speaking, it creates a small Rectangle in the center of each tile.
     */
    public void create() {
        int eventWidth = 22;
        int eventHeight = 18;
        for (int map = 0; map < MAPS; map++)
            for (int row = 0; row < MAX_MAP_ROW; row++)
                for (int col = 0; col < MAX_MAP_COL; col++)
                    event[map][row][col] = new Rectangle(x, y, eventWidth, eventHeight);
    }

    /**
     * Check the event.
     *
     * @param entity entity that generates the event.
     */
    public void check(Entity entity) {

        // Check if the player is more than 1 tile away from the last event using the previous event as information
        int xDis = Math.abs(world.entities.player.pos.x - previousEventX);
        int yDis = Math.abs(world.entities.player.pos.y - previousEventY);
        int dis = Math.max(xDis, yDis);
        if (dis > tile) canCollideEvent = true;

        if (canCollideEvent) {
            if (isColliding(ABANDONED_ISLAND, 27, 16, Direction.RIGHT)) hurt(entity);
            if (isColliding(ABANDONED_ISLAND, 23, 12, Direction.UP)) heal(entity);
            if (isColliding(DUNGEON_BREG_SUB, 25, 27, Direction.ANY)) bossScene();
            if (isColliding(ABANDONED_ISLAND, 10, 39, Direction.UP)) teleport(MARKET, ABANDONED_ISLAND_MARKET, 12, 13);
            if (isColliding(ABANDONED_ISLAND_MARKET, 12, 13, Direction.DOWN)) teleport(OVERWORLD, ABANDONED_ISLAND, 10, 39);
            if (isColliding(ABANDONED_ISLAND, 12, 9, Direction.ANY)) teleport(DUNGEON, DUNGEON_BREG, 9, 41);
            if (isColliding(DUNGEON_BREG, 9, 41, Direction.ANY)) teleport(OVERWORLD, ABANDONED_ISLAND, 12, 9);
            if (isColliding(DUNGEON_BREG, 8, 7, Direction.ANY)) teleport(BOSS, DUNGEON_BREG_SUB, 26, 41);
            if (isColliding(DUNGEON_BREG_SUB, 26, 41, Direction.ANY)) teleport(DUNGEON, DUNGEON_BREG, 8, 7);
        }

    }

    /**
     * Check the collision of the player with the event.
     * <p>
     * TODO Only check the collision with the player and not with other entities
     *
     * @param map       map of the event.
     * @param col       event column.
     * @param row       event row.
     * @param direction direction of the event.
     * @return returns true if the player collides with the event or false.
     */
    private boolean isColliding(int map, int col, int row, Direction direction) {
        boolean colliding = false;

        // If the player is on the same map as the event
        if (map == world.map.num) {
            world.entities.player.hitbox.setX(world.entities.player.hitbox.getX() + world.entities.player.pos.x);
            world.entities.player.hitbox.setY(world.entities.player.hitbox.getY() + world.entities.player.pos.y);
            event[map][row][col].setX(event[map][row][col].getX() + col * tile);
            event[map][row][col].setY(event[map][row][col].getY() + row * tile);

            // If the player collides with the event and if the direction matches that of the event
            if (world.entities.player.hitbox.intersects(event[map][row][col].getBoundsInParent()) && (world.entities.player.direction == direction || direction == Direction.ANY)) {
                colliding = true;
                world.entities.player.attackCanceled = true; // Cancels the attack if you interact with an event using enter (key used to attack)
                // Based on this information, verify the distance between the player and the last event
                previousEventX = world.entities.player.pos.x;
                previousEventY = world.entities.player.pos.y;
            }

            // Resets the player hitbox position and event position
            world.entities.player.hitbox.setX(world.entities.player.hitboxDefaultX);
            world.entities.player.hitbox.setY(world.entities.player.hitboxDefaultY);
            event[map][row][col].setX(x);
            event[map][row][col].setY(y);
        }

        return colliding;

    }

    /**
     * Hurt the entity.
     *
     * @param entity entity to hurt.
     */
    private void hurt(Entity entity) {
        entity.dialogue.dialogues[0][0] = "You fall into a pit!";
        entity.dialogue.startDialogue(State.DIALOGUE, entity, 0);
        entity.stats.hp--;
        canCollideEvent = false;
    }

    /**
     * Heals the entity.
     *
     * @param entity entity to heal.
     */
    private void heal(Entity entity) {
        if (game.systems.keyboard.isKeyPressed(Key.ENTER)) {
            entity.dialogue.dialogues[1][0] = "You drink the water.\nYour life has been recovered.";
            entity.dialogue.startDialogue(State.DIALOGUE, entity, 1);
            entity.stats.hp = entity.stats.maxHp;
        }
    }

    /**
     * Generate the boss scene.
     */
    private void bossScene() {
        if (!world.entities.player.bossBattleOn && !Progress.bossDefeated) {
            State.setState(State.CUTSCENE);
            world.cutscene.n = world.cutscene.boss;
            world.entities.player.bossBattleOn = true;
        }
    }

    /**
     * Teleports the player.
     *
     * @param zone   zone to which the player teleports.
     * @param mapNum map to which the player teleports.
     * @param col    column to which the player teleports.
     * @param row    row to which the player teleports.
     */
    private void teleport(int zone, int mapNum, int col, int row) {
        State.setState(State.TELEPORT);
        world.map.nextZone = zone;
        this.mapNum = mapNum;
        this.col = col;
        this.row = row;
        canCollideEvent = false;
    }

}
