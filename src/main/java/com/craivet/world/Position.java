package com.craivet.world;

import com.craivet.Direction;
import com.craivet.world.entity.Entity;

import static com.craivet.utils.Global.UPDATES;
import static com.craivet.utils.Global.tile;

/**
 * Estas coordenadas no son mas que la suma de pixeles a partir de la esquina superior izquierda del mapa (0, 0). Cuando
 * se divide esa suma por el tamaño del tile, se obtiene la posicion en filas y columnas (row y col). Esto se hace para
 * facilitar el manejo de las coordenadas. Otra cosa a tener en cuenta, es que a las coordenadas x-y se le suma la
 * hitbox para obtener la posicion exacta del rectangulo colisionador y no del frame.
 */

public class Position {

    public int x, y;

    /**
     * Establece la posicion. Es importante aclarar que se posiciona la hitbox, NO la imagen.
     * <p>
     * TODO Evitar que el player aparezca sobre una entidad solida o fuera de los limites del mapa
     */
    public void setPos(World world, Entity entity, int map, int zone, int col, int row, Direction dir) {
        world.map = map;
        world.zone = zone;
        // Suma la mitad del ancho de la hitbox y resta un pixel para centrar la posicion horizontal dentro del tile
        x = (col * tile) + entity.hitbox.width / 2 - 1;
        /* Resta el alto de la hitbox para que la posicion se ajuste en la fila especificada, ya que la imagen del
         * player ocupa dos tiles verticales. Por ultimo se resta un pixel en caso de que la posicion este por encima
         * de un tile solido para evitar que se "trabe". */
        y = (row * tile) - entity.hitbox.height - 1;
        switch (dir) {
            case DOWN -> entity.currentFrame = entity.down.getFirstFrame();
            case UP -> entity.currentFrame = entity.up.getFirstFrame();
            case LEFT -> entity.currentFrame = entity.left.getFirstFrame();
            case RIGHT -> entity.currentFrame = entity.right.getFirstFrame();
        }
    }

}
