package com.craivet.world;

import com.craivet.Direction;
import com.craivet.world.entity.Entity;

import static com.craivet.utils.Global.tile;

/**
 * These coordinates are nothing more than the sum of pixels starting from the upper left corner of the map (0, 0). When
 * this sum is divided by the size of the tile, the position in rows and columns is obtained. This is done to facilitate
 * the handling of coordinates. Another thing to keep in mind is that the entity's hitbox is added to the x-y
 * coordinates to obtain the exact position of the collider rectangle and not the frame. It is important to clarify that
 * the hitbox is positioned, NOT the image.
 */

public class Position {

    public int x, y;

    /**
     * Establece la posicion de la entidad.
     */
    public void set(int col, int row) {
        x = col * tile;
        y = row * tile;
    }

    /**
     * Sets the position of the player.
     * <p>
     * TODO Prevent the entity from appearing on a solid entity, solid tile or outside the map boundaries.
     */
    public void set(World world, Entity entity, int map, int zone, int col, int row, Direction dir) {
        world.map = map;
        world.zone = zone;
        // Add half the width of the hitbox and subtract one pixel to center the horizontal position within the tile
        x = (col * tile) + entity.hitbox.width / 2 - 1;
        /* Subtracts the height of the hitbox so that the position fits in the specified row, since the player image
         * occupies two vertical tiles. Finally, a pixel is subtracted in case the position is above a solid tile to
         * prevent it from "locking". */
        y = (row * tile) - entity.hitbox.height - 1;
        switch (dir) {
            case DOWN -> entity.currentFrame = entity.down.getFirstFrame();
            case UP -> entity.currentFrame = entity.up.getFirstFrame();
            case LEFT -> entity.currentFrame = entity.left.getFirstFrame();
            case RIGHT -> entity.currentFrame = entity.right.getFirstFrame();
        }
    }

    /**
     * Updates the position of the entity.
     *
     * @param entity    entity.
     * @param direction direction of the entity.
     */
    public void update(Entity entity, Direction direction) {
        switch (direction) {
            case DOWN -> y += entity.stats.speed;
            case UP -> y -= entity.stats.speed;
            case LEFT -> x -= entity.stats.speed;
            case RIGHT -> x += entity.stats.speed;
        }
    }

}
