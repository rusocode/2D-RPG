package com.punkipunk.world;

import com.punkipunk.Direction;
import com.punkipunk.entity.Entity;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * Estas coordenadas no son mas que la suma de pixeles comenzando desde la esquina superior izquierda del mapa (0, 0). Al dividir
 * esta suma por el tamaño del tile se obtiene la posicion en filas y columnas. Esto se hace para facilitar el manejo de
 * coordenadas. Otra cosa a tener en cuenta es que a las coordenadas x-y se suma el hitbox de la entidad para obtener la posicion
 * exacta del rectangulo del colisionador y no del frame. Es importante aclarar que se posiciona el hitbox, NO la imagen.
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
     * Establece la posicion del player.
     * <p>
     * TODO Evitar que la entidad aparezca sobre otra entidad, sobre un tile solido y fuera de los limites del mapa
     */
    public void set(World world, Entity entity, MapID mapId, int col, int row, Direction direction) {
        world.map.id = mapId;
        // Agregua la mitad del ancho del hitbox y resta un pixel para centrar la posicion horizontal dentro del tile
        x = (int) ((col * tile) + entity.hitbox.getWidth() / 2 - 1);
        /* Resta la altura del hitbox para que la posicion encaje en la coordenada Y especificada, ya que la imagen del player
         * ocupa dos tiles verticales. Por ultimo, resta un pixel en caso de que la posicion este sobre un tile solido para evitar
         * que se bloquee. */
        y = (int) ((row * tile) - entity.hitbox.getHeight() - 1);
        entity.direction = direction;
        switch (direction) {
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
