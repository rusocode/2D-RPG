package com.punkipunk.physics;

import com.punkipunk.Direction;
import com.punkipunk.world.World;
import com.punkipunk.entity.base.Entity;
import com.punkipunk.entity.player.Player;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * The collision between two entities is generated when one of the limits of the hitbox passes 1 pixel of the hitbox of the other
 * entity. But in the case of the attackbox, a collision is only generated when the limits of both touch.
 */

public class CollisionChecker {

    private final World world;

    public CollisionChecker(World world) {
        this.world = world;
    }

    /**
     * Checks if the entity collides with a solid tile.
     *
     * @param entity the entity.
     */
    public void checkTile(Entity entity) {

        int entityBottomWorldY = (int) (entity.pos.y + entity.hitbox.getY() + entity.hitbox.getHeight());
        int entityTopWorldY = (int) (entity.pos.y + entity.hitbox.getY());
        int entityLeftWorldX = (int) (entity.pos.x + entity.hitbox.getX());
        int entityRightWorldX = (int) (entity.pos.x + entity.hitbox.getX() + entity.hitbox.getWidth());

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
                // TODO
                entity.hitbox.setX(entity.hitbox.getX() + entity.pos.x);
                entity.hitbox.setY(entity.hitbox.getY() + entity.pos.y);
                world.entities.items[world.map.num][i].hitbox.setX(world.entities.items[world.map.num][i].hitbox.getX() + world.entities.items[world.map.num][i].pos.x);
                world.entities.items[world.map.num][i].hitbox.setY(world.entities.items[world.map.num][i].hitbox.getY() + world.entities.items[world.map.num][i].pos.y);

                Direction direction = entity.direction;
                if (entity.flags.knockback) direction = entity.direction.knockbackDirection;

                switch (direction) {
                    case DOWN -> entity.hitbox.setY(entity.hitbox.getY() + entity.stats.speed);
                    case UP -> entity.hitbox.setY(entity.hitbox.getY() - entity.stats.speed);
                    case LEFT -> entity.hitbox.setX(entity.hitbox.getX() - entity.stats.speed);
                    case RIGHT -> entity.hitbox.setX(entity.hitbox.getX() + entity.stats.speed);
                }

                if (entity.hitbox.intersects(world.entities.items[world.map.num][i].hitbox.getBoundsInParent())) {
                    if (world.entities.items[world.map.num][i].solid) entity.flags.colliding = true;
                    if (entity instanceof Player) index = i;
                }

                entity.hitbox.setX(entity.hitboxDefaultX);
                entity.hitbox.setY(entity.hitboxDefaultY);

                world.entities.items[world.map.num][i].hitbox.setX(world.entities.items[world.map.num][i].hitboxDefaultX);
                world.entities.items[world.map.num][i].hitbox.setY(world.entities.items[world.map.num][i].hitboxDefaultY);
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
                entity.hitbox.setX(entity.hitbox.getX() + entity.pos.x);
                entity.hitbox.setY(entity.hitbox.getY() + entity.pos.y);
                otherEntity[world.map.num][i].hitbox.setX(otherEntity[world.map.num][i].hitbox.getX() + otherEntity[world.map.num][i].pos.x);
                otherEntity[world.map.num][i].hitbox.setY(otherEntity[world.map.num][i].hitbox.getY() + otherEntity[world.map.num][i].pos.y);

                switch (direction) {
                    case DOWN -> entity.hitbox.setY(entity.hitbox.getY() + speed);
                    case UP -> entity.hitbox.setY(entity.hitbox.getY() - speed);
                    case LEFT -> entity.hitbox.setX(entity.hitbox.getX() - speed);
                    case RIGHT -> entity.hitbox.setX(entity.hitbox.getX() + speed);
                }

                if (entity.hitbox.intersects(otherEntity[world.map.num][i].hitbox.getBoundsInParent())) {
                    if (otherEntity[world.map.num][i] != entity) { // Avoid the collision itself
                        entity.flags.colliding = true;
                        entity.flags.collidingOnMob = true;
                        index = i;
                    }
                }

                entity.hitbox.setX(entity.hitboxDefaultX);
                entity.hitbox.setY(entity.hitboxDefaultY);

                otherEntity[world.map.num][i].hitbox.setX(otherEntity[world.map.num][i].hitboxDefaultX);
                otherEntity[world.map.num][i].hitbox.setY(otherEntity[world.map.num][i].hitboxDefaultY);

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

        entity.hitbox.setX(entity.hitbox.getX() + entity.pos.x);
        entity.hitbox.setY(entity.hitbox.getY() + entity.pos.y);
        world.entities.player.hitbox.setX(world.entities.player.hitbox.getX() + world.entities.player.pos.x);
        world.entities.player.hitbox.setY(world.entities.player.hitbox.getY() + world.entities.player.pos.y);

        switch (entity.direction) {
            case DOWN -> entity.hitbox.setY(entity.hitbox.getY() + entity.stats.speed);
            case UP -> entity.hitbox.setY(entity.hitbox.getY() - entity.stats.speed);
            case LEFT -> entity.hitbox.setX(entity.hitbox.getX() - entity.stats.speed);
            case RIGHT -> entity.hitbox.setX(entity.hitbox.getX() + entity.stats.speed);
        }

        if (entity.hitbox.intersects(world.entities.player.hitbox.getBoundsInParent())) {
            entity.flags.colliding = true;
            contact = true;
        }

        entity.hitbox.setX(entity.hitboxDefaultX);
        entity.hitbox.setY(entity.hitboxDefaultY);
        world.entities.player.hitbox.setX(world.entities.player.hitboxDefaultX);
        world.entities.player.hitbox.setY(world.entities.player.hitboxDefaultY);

        return contact;

    }

}
