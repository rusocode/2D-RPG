package com.punkipunk.world;

import com.punkipunk.Direction;
import com.punkipunk.entity.Entity;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * Estas coordenadas no son mas que la suma de pixeles comenzando desde la esquina superior izquierda del mapa (0, 0). Al dividir
 * esta suma por el tamaño del tile se obtiene la posición en filas y columnas. Esto se hace para facilitar el manejo de
 * coordenadas. Otra cosa a tener en cuenta es que a las coordenadas x-y se suma el hitbox de la entidad para obtener la posición
 * exacta del rectangulo del colisionador y no del frame. Es importante aclarar que se posiciona el hitbox, NO la imagen.
 */

public class Position {

    public int x, y;

    /**
     * Set up the position of the entity.
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
        world.map.num = map;
        world.map.zone = zone;
        // Add half the width of the hitbox and subtract one pixel to center the horizontal position within the tile
        x = (int) ((col * tile) + entity.hitbox.getWidth() / 2 - 1);
        /* Subtracts the height of the hitbox so that the position fits in the specified row, since the player image
         * occupies two vertical tiles. Finally, a pixel is subtracted in case the position is above a solid tile to
         * prevent it from "locking". */
        y = (int) ((row * tile) - entity.hitbox.getHeight() - 1);
        entity.direction = dir;
        switch (dir) {
            case DOWN -> entity.currentFrame = entity.down.getFirstFrame();
            case UP -> entity.currentFrame = entity.up.getFirstFrame();
            case LEFT -> entity.currentFrame = entity.left.getFirstFrame();
            case RIGHT -> entity.currentFrame = entity.right.getFirstFrame();
        }
    }

    /**
     * Actualiza la posicion de la entidad dependiendo de la direccion.
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
