package com.craivet.physics;

import com.craivet.Direction;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Player;

import static com.craivet.utils.Global.*;

/**
 * The collision between two entities is generated when one of the limits of the hitbox passes 1 pixel of the hitbox of
 * the other entity. But in the case of the attackbox, a collision is only generated when the limits of both touch.
 */

public class Collision {

    private final World world;

    public Collision(World world) {
        this.world = world;
    }

    /**
     * Checks if the entity collides with a solid tile.
     *
     * @param entity the entity.
     */
    public void checkTile(Entity entity) {

        int entityBottomWorldY = entity.pos.y + entity.hitbox.y + entity.hitbox.height;
        int entityTopWorldY = entity.pos.y + entity.hitbox.y;
        int entityLeftWorldX = entity.pos.x + entity.hitbox.x;
        int entityRightWorldX = entity.pos.x + entity.hitbox.x + entity.hitbox.width;

        int entityBottomRow = entityBottomWorldY / tile;
        int entityTopRow = entityTopWorldY / tile;
        int entityLeftCol = entityLeftWorldX / tile;
        int entityRightCol = entityRightWorldX / tile;

        // In case the entity collides in the middle of two solid tools
        int tile1, tile2;

        Direction direction = entity.direction;

        // Gets the direction of the attacker if the entity is knockback to prevent the collision check from becoming inaccurate
        if (entity.flags.knockback) direction = entity.direction.knockbackDirection;

        switch (direction) {
            case DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.stats.speed) / tile;
                tile1 = world.map.tileIndex[world.map.num][entityBottomRow][entityLeftCol];
                tile2 = world.map.tileIndex[world.map.num][entityBottomRow][entityRightCol];
                if (world.map.tileData[tile1].solid || world.map.tileData[tile2].solid) entity.flags.colliding = true;
            }
            case UP -> {
                entityTopRow = (entityTopWorldY - entity.stats.speed) / tile;
                tile1 = world.map.tileIndex[world.map.num][entityTopRow][entityLeftCol];
                tile2 = world.map.tileIndex[world.map.num][entityTopRow][entityRightCol];
                if (world.map.tileData[tile1].solid || world.map.tileData[tile2].solid) entity.flags.colliding = true;
            }
            case LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.stats.speed) / tile;
                tile1 = world.map.tileIndex[world.map.num][entityTopRow][entityLeftCol];
                tile2 = world.map.tileIndex[world.map.num][entityBottomRow][entityLeftCol];
                if (world.map.tileData[tile1].solid || world.map.tileData[tile2].solid) entity.flags.colliding = true;
            }
            case RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.stats.speed) / tile;
                tile1 = world.map.tileIndex[world.map.num][entityTopRow][entityRightCol];
                tile2 = world.map.tileIndex[world.map.num][entityBottomRow][entityRightCol];
                if (world.map.tileData[tile1].solid || world.map.tileData[tile2].solid) entity.flags.colliding = true;
            }
        }

    }

    /**
     * Checks if the entity collides with an item.
     *
     * @param entity the entity.
     * @return the index of the item in case the player collides with it.
     */
    public int checkItem(Entity entity) {
        int index = -1;
        for (int i = 0; i < world.entities.items[1].length; i++) {
            if (world.entities.items[world.map.num][i] != null) {
                // Gets the hitbox position of the entity and the item
                entity.hitbox.x += entity.pos.x;
                entity.hitbox.y += entity.pos.y;
                world.entities.items[world.map.num][i].hitbox.x += world.entities.items[world.map.num][i].pos.x;
                world.entities.items[world.map.num][i].hitbox.y += world.entities.items[world.map.num][i].pos.y;

                Direction direction = entity.direction;
                if (entity.flags.knockback) direction = entity.direction.knockbackDirection;

                switch (direction) {
                    case DOWN -> entity.hitbox.y += entity.stats.speed;
                    case UP -> entity.hitbox.y -= entity.stats.speed;
                    case LEFT -> entity.hitbox.x -= entity.stats.speed;
                    case RIGHT -> entity.hitbox.x += entity.stats.speed;
                }

                if (entity.hitbox.intersects(world.entities.items[world.map.num][i].hitbox)) {
                    if (world.entities.items[world.map.num][i].solid) entity.flags.colliding = true;
                    if (entity instanceof Player) index = i;
                }

                entity.hitbox.x = entity.hitboxDefaultX;
                entity.hitbox.y = entity.hitboxDefaultY;
                world.entities.items[world.map.num][i].hitbox.x = world.entities.items[world.map.num][i].hitboxDefaultX;
                world.entities.items[world.map.num][i].hitbox.y = world.entities.items[world.map.num][i].hitboxDefaultY;
            }
        }
        return index;
    }

    /**
     * Check the collision between entities.
     *
     * @param entity      entity.
     * @param otherEntity another entity.
     * @return the index of the other entity in case the entity collides with it.
     */
    public int checkEntity(Entity entity, Entity[][] otherEntity) {
        int index = -1;

        // Temporary direction of the entity
        Direction direction = entity.direction;
        // Gets the direction of the attacker if the entity is knockback
        if (entity.flags.knockback) direction = entity.direction.knockbackDirection;
        int speed = entity.stats.speed;

        for (int i = 0; i < otherEntity[1].length; i++) {
            if (otherEntity[world.map.num][i] != null) {
                // Gets the position of the hitbox of the entity and the other entity
                entity.hitbox.x += entity.pos.x;
                entity.hitbox.y += entity.pos.y;
                otherEntity[world.map.num][i].hitbox.x += otherEntity[world.map.num][i].pos.x;
                otherEntity[world.map.num][i].hitbox.y += otherEntity[world.map.num][i].pos.y;

                switch (direction) {
                    case DOWN -> entity.hitbox.y += speed;
                    case UP -> entity.hitbox.y -= speed;
                    case LEFT -> entity.hitbox.x -= speed;
                    case RIGHT -> entity.hitbox.x += speed;
                }

                if (entity.hitbox.intersects(otherEntity[world.map.num][i].hitbox)) {
                    if (otherEntity[world.map.num][i] != entity) { // Avoid the collision itself
                        entity.flags.colliding = true;
                        entity.flags.collidingOnMob = true;
                        index = i;
                    }
                }

                entity.hitbox.x = entity.hitboxDefaultX;
                entity.hitbox.y = entity.hitboxDefaultY;
                otherEntity[world.map.num][i].hitbox.x = otherEntity[world.map.num][i].hitboxDefaultX;
                otherEntity[world.map.num][i].hitbox.y = otherEntity[world.map.num][i].hitboxDefaultY;
            }
        }
        return index;
    }

    /**
     * Checks if the entity collides with the player.
     *
     * <p>TODO Can it work with the checkEntity() method?
     *
     * @param entity the entity.
     * @return true if the entity collides with the player.
     */
    public boolean checkPlayer(Entity entity) {
        boolean contact = false;
        entity.hitbox.x += entity.pos.x;
        entity.hitbox.y += entity.pos.y;
        world.entities.player.hitbox.x += world.entities.player.pos.x;
        world.entities.player.hitbox.y += world.entities.player.pos.y;

        switch (entity.direction) {
            case DOWN -> entity.hitbox.y += entity.stats.speed;
            case UP -> entity.hitbox.y -= entity.stats.speed;
            case LEFT -> entity.hitbox.x -= entity.stats.speed;
            case RIGHT -> entity.hitbox.x += entity.stats.speed;
        }

        if (entity.hitbox.intersects(world.entities.player.hitbox)) {
            entity.flags.colliding = true;
            contact = true;
        }

        entity.hitbox.x = entity.hitboxDefaultX;
        entity.hitbox.y = entity.hitboxDefaultY;
        world.entities.player.hitbox.x = world.entities.player.hitboxDefaultX;
        world.entities.player.hitbox.y = world.entities.player.hitboxDefaultY;

        return contact;

    }

}
