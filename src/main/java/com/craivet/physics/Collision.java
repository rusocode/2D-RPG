package com.craivet.physics;

import com.craivet.Direction;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Player;

import static com.craivet.utils.Global.*;

/**
 * La colision entre dos entidades se genera cuando uno de los limites de la hitbox se pasa 1 pixel de la hitbox de la
 * otra entidad. Pero en el caso del attackbox, solo se genera colision cuando los limites de ambos se tocan.
 * <p>
 * TODO Se podria cambiar los nombres de los metodos
 */

public class Collision {

    private final World world;

    public Collision(World world) {
        this.world = world;
    }

    /**
     * Comprueba si la entidad colisiona con un tile solido.
     *
     * @param entity la entidad.
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

        // En caso de que la entidad colisione en el medio de dos tiles solidos
        int tile1, tile2;

        Direction direction = entity.stats.direction;
        /* Obtiene la direccion del atacante si la entidad es retrocedida para evitar que la verificacion de la colision
         * se vuelva inexacta. */
        if (entity.flags.knockback) direction = entity.knockbackDirection;

        switch (direction) {
            case DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.stats.speed) / tile;
                tile1 = world.tileIndex[world.map][entityBottomRow][entityLeftCol];
                tile2 = world.tileIndex[world.map][entityBottomRow][entityRightCol];
                if (world.tileData[tile1].solid || world.tileData[tile2].solid) entity.flags.colliding = true;
            }
            case UP -> {
                entityTopRow = (entityTopWorldY - entity.stats.speed) / tile;
                tile1 = world.tileIndex[world.map][entityTopRow][entityLeftCol];
                tile2 = world.tileIndex[world.map][entityTopRow][entityRightCol];
                if (world.tileData[tile1].solid || world.tileData[tile2].solid) entity.flags.colliding = true;
            }
            case LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.stats.speed) / tile;
                tile1 = world.tileIndex[world.map][entityTopRow][entityLeftCol];
                tile2 = world.tileIndex[world.map][entityBottomRow][entityLeftCol];
                if (world.tileData[tile1].solid || world.tileData[tile2].solid) entity.flags.colliding = true;
            }
            case RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.stats.speed) / tile;
                tile1 = world.tileIndex[world.map][entityTopRow][entityRightCol];
                tile2 = world.tileIndex[world.map][entityBottomRow][entityRightCol];
                if (world.tileData[tile1].solid || world.tileData[tile2].solid) entity.flags.colliding = true;
            }
        }

    }

    /**
     * Comprueba si la entidad colisiona con un item.
     *
     * @param entity la entidad.
     * @return el indice del item en caso de que el player colisione con este.
     */
    public int checkItem(Entity entity) {
        int index = -1;
        for (int i = 0; i < world.items[1].length; i++) {
            if (world.items[world.map][i] != null) {
                // Obtiene la posicion del hitbox de la entidad y del item
                entity.hitbox.x += entity.pos.x;
                entity.hitbox.y += entity.pos.y;
                world.items[world.map][i].hitbox.x += world.items[world.map][i].pos.x;
                world.items[world.map][i].hitbox.y += world.items[world.map][i].pos.y;

                Direction direction = entity.stats.direction;
                if (entity.flags.knockback) direction = entity.knockbackDirection;

                switch (direction) {
                    case DOWN -> entity.hitbox.y += entity.stats.speed;
                    case UP -> entity.hitbox.y -= entity.stats.speed;
                    case LEFT -> entity.hitbox.x -= entity.stats.speed;
                    case RIGHT -> entity.hitbox.x += entity.stats.speed;
                }

                if (entity.hitbox.intersects(world.items[world.map][i].hitbox)) {
                    if (world.items[world.map][i].stats.solid) entity.flags.colliding = true;
                    if (entity instanceof Player) index = i;
                }

                entity.hitbox.x = entity.hitboxDefaultX;
                entity.hitbox.y = entity.hitboxDefaultY;
                world.items[world.map][i].hitbox.x = world.items[world.map][i].hitboxDefaultX;
                world.items[world.map][i].hitbox.y = world.items[world.map][i].hitboxDefaultY;
            }
        }
        return index;
    }

    /**
     * Comprueba la colision entre entidades.
     *
     * @param entity      entidad.
     * @param otherEntity otra entidad.
     * @return el indice de la otra entidad en caso de que la entidad colisione con esta.
     */
    public int checkEntity(Entity entity, Entity[][] otherEntity) {
        int index = -1;

        // Direccion temporal de la entidad
        Direction direction = entity.stats.direction;
        // Obtiene la direccion del atacante si la entidad es retrocedida
        if (entity.flags.knockback) direction = entity.knockbackDirection;
        int speed = entity.stats.speed;

        for (int i = 0; i < otherEntity[1].length; i++) {
            if (otherEntity[world.map][i] != null) {
                // Obtiene la posicion del area del cuerpo de la entidad y de la otra entidad
                entity.hitbox.x += entity.pos.x;
                entity.hitbox.y += entity.pos.y;
                otherEntity[world.map][i].hitbox.x += otherEntity[world.map][i].pos.x;
                otherEntity[world.map][i].hitbox.y += otherEntity[world.map][i].pos.y;

                switch (direction) {
                    case DOWN -> entity.hitbox.y += speed;
                    case UP -> entity.hitbox.y -= speed;
                    case LEFT -> entity.hitbox.x -= speed;
                    case RIGHT -> entity.hitbox.x += speed;
                }

                if (entity.hitbox.intersects(otherEntity[world.map][i].hitbox)) {
                    if (otherEntity[world.map][i] != entity) { // Evita la colision en si misma
                        entity.flags.colliding = true;
                        entity.flags.collidingOnMob = true;
                        index = i;
                    }
                }

                entity.hitbox.x = entity.hitboxDefaultX;
                entity.hitbox.y = entity.hitboxDefaultY;
                otherEntity[world.map][i].hitbox.x = otherEntity[world.map][i].hitboxDefaultX;
                otherEntity[world.map][i].hitbox.y = otherEntity[world.map][i].hitboxDefaultY;
            }
        }
        return index;
    }

    /**
     * Comprueba si la entidad colisiona con el player.
     *
     * <p>TODO Se puede fucionar con el metodo checkEntity()?
     *
     * @param entity la entidad.
     * @return true si la entidad colisiona con el player.
     */
    public boolean checkPlayer(Entity entity) {
        boolean contact = false;
        entity.hitbox.x += entity.pos.x;
        entity.hitbox.y += entity.pos.y;
        world.player.hitbox.x += world.player.pos.x;
        world.player.hitbox.y += world.player.pos.y;

        switch (entity.stats.direction) {
            case DOWN -> entity.hitbox.y += entity.stats.speed;
            case UP -> entity.hitbox.y -= entity.stats.speed;
            case LEFT -> entity.hitbox.x -= entity.stats.speed;
            case RIGHT -> entity.hitbox.x += entity.stats.speed;
        }

        if (entity.hitbox.intersects(world.player.hitbox)) {
            entity.flags.colliding = true;
            contact = true;
        }

        entity.hitbox.x = entity.hitboxDefaultX;
        entity.hitbox.y = entity.hitboxDefaultY;
        world.player.hitbox.x = world.player.hitboxDefaultX;
        world.player.hitbox.y = world.player.hitboxDefaultY;

        return contact;

    }

}
