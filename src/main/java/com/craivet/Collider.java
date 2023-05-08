package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.Player;

import static com.craivet.utils.Global.*;

/**
 * Deteccion de colisiones.
 */

public class Collider {

    private final World world;

    public Collider(World world) {
        this.world = world;
    }

    /**
     * Comprueba si la entidad colisiona con un tile solido.
     *
     * @param entity la entidad.
     */
    public void checkTile(Entity entity) {

        int entityBottomWorldY = entity.y + entity.hitbox.y + entity.hitbox.height;
        int entityTopWorldY = entity.y + entity.hitbox.y;
        int entityLeftWorldX = entity.x + entity.hitbox.x;
        int entityRightWorldX = entity.x + entity.hitbox.x + entity.hitbox.width;

        int entityBottomRow = entityBottomWorldY / tile_size;
        int entityTopRow = entityTopWorldY / tile_size;
        int entityLeftCol = entityLeftWorldX / tile_size;
        int entityRightCol = entityRightWorldX / tile_size;

        // En caso de que la entidad colisione en el medio de dos tiles solidos
        int tile1, tile2;

        int direction = entity.direction;
        /* Obtiene la direccion del atacante si la entidad es retrocedida para evitar que la verificacion de la colision
         * se vuelva inexacta. */
        if (entity.knockback) direction = entity.knockbackDirection;

        switch (direction) {
            case DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.speed) / tile_size;
                tile1 = world.tileIndex[world.map][entityBottomRow][entityLeftCol];
                tile2 = world.tileIndex[world.map][entityBottomRow][entityRightCol];
                if (world.tileData[tile1].solid || world.tileData[tile2].solid) entity.collision = true;
            }
            case UP -> {
                entityTopRow = (entityTopWorldY - entity.speed) / tile_size;
                tile1 = world.tileIndex[world.map][entityTopRow][entityLeftCol];
                tile2 = world.tileIndex[world.map][entityTopRow][entityRightCol];
                if (world.tileData[tile1].solid || world.tileData[tile2].solid) entity.collision = true;
            }
            case LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / tile_size;
                tile1 = world.tileIndex[world.map][entityTopRow][entityLeftCol];
                tile2 = world.tileIndex[world.map][entityBottomRow][entityLeftCol];
                if (world.tileData[tile1].solid || world.tileData[tile2].solid) entity.collision = true;
            }
            case RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.speed) / tile_size;
                tile1 = world.tileIndex[world.map][entityTopRow][entityRightCol];
                tile2 = world.tileIndex[world.map][entityBottomRow][entityRightCol];
                if (world.tileData[tile1].solid || world.tileData[tile2].solid) entity.collision = true;
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
                entity.hitbox.x += entity.x;
                entity.hitbox.y += entity.y;
                world.items[world.map][i].hitbox.x += world.items[world.map][i].x;
                world.items[world.map][i].hitbox.y += world.items[world.map][i].y;

                int direction = entity.direction;
                if (entity.knockback) direction = entity.knockbackDirection;

                switch (direction) {
                    case DOWN -> entity.hitbox.y += entity.speed;
                    case UP -> entity.hitbox.y -= entity.speed;
                    case LEFT -> entity.hitbox.x -= entity.speed;
                    case RIGHT -> entity.hitbox.x += entity.speed;
                }

                if (entity.hitbox.intersects(world.items[world.map][i].hitbox)) {
                    if (world.items[world.map][i].solid) entity.collision = true;
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
        int direction = entity.direction;
        // Obtiene la direccion del atacante si la entidad es retrocedida
        if (entity.knockback) direction = entity.knockbackDirection;
        int speed = entity.speed;

        for (int i = 0; i < otherEntity[1].length; i++) {
            if (otherEntity[world.map][i] != null) {
                // Obtiene la posicion del area del cuerpo de la entidad y de la otra entidad
                entity.hitbox.x += entity.x;
                entity.hitbox.y += entity.y;
                otherEntity[world.map][i].hitbox.x += otherEntity[world.map][i].x;
                otherEntity[world.map][i].hitbox.y += otherEntity[world.map][i].y;
                switch (direction) {
                    case DOWN -> entity.hitbox.y += speed;
                    case UP -> entity.hitbox.y -= speed;
                    case LEFT -> entity.hitbox.x -= speed;
                    case RIGHT -> entity.hitbox.x += speed;
                }

                if (entity.hitbox.intersects(otherEntity[world.map][i].hitbox)) {
                    if (otherEntity[world.map][i] != entity) { // Evita la colision en si misma
                        entity.collision = true;
                        entity.collisionOnEntity = true;
                        index = i;
                        // break; // TODO No tendria que romper el bucle una vez que hay colision?
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
        entity.hitbox.x += entity.x;
        entity.hitbox.y += entity.y;
        world.player.hitbox.x += world.player.x;
        world.player.hitbox.y += world.player.y;
        switch (entity.direction) {
            case DOWN -> entity.hitbox.y += entity.speed;
            case UP -> entity.hitbox.y -= entity.speed;
            case LEFT -> entity.hitbox.x -= entity.speed;
            case RIGHT -> entity.hitbox.x += entity.speed;
        }

        if (entity.hitbox.intersects(world.player.hitbox)) {
            entity.collision = true;
            contact = true;
        }

        entity.hitbox.x = entity.hitboxDefaultX;
        entity.hitbox.y = entity.hitboxDefaultY;
        world.player.hitbox.x = world.player.hitboxDefaultX;
        world.player.hitbox.y = world.player.hitboxDefaultY;

        return contact;

    }

}
