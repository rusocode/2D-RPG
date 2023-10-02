package com.craivet.world;

import com.craivet.Direction;
import com.craivet.world.entity.Entity;

import static com.craivet.utils.Global.tile;

/**
 * Estas coordenadas no son mas que la suma de pixeles a partir de la esquina superior izquierda del mapa (0, 0). Cuando
 * se divide esa suma por el tamaño del tile, se obtiene la posicion en filas y columnas. Esto se hace para facilitar el
 * manejo de las coordenadas. Otra cosa a tener en cuenta, es que a las coordenadas x-y se le suma la hitbox de la
 * entidad para obtener la posicion exacta del rectangulo colisionador y no del frame. Es importante aclarar que se
 * posiciona la hitbox, NO la imagen.
 *
 * <a href="https://github.com/AlmasB/FXGL/blob/dev/fxgl-scene/src/main/java/com/almasb/fxgl/ui/Position.java">...</a>
 */

public class Position {

    // TODO Agregar metodo spawn como inicial de posicion

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
     * TODO Evitar que la entidad aparezca sobre una entidad solida, tile solido o fuera de los limites del mapa.
     */
    public void set(World world, Entity entity, int map, int zone, int col, int row, Direction dir) {
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

    /**
     * Actualiza la posicion de la entidad.
     *
     * @param entity    entidad.
     * @param direction direccion de la entidad.
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
