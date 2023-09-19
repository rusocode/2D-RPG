package com.craivet.world;

/**
 * Estas coordenadas no son mas que la suma de pixeles a partir de la esquina superior izquierda del mapa (0, 0). Cuando
 * se divide esa suma por el tamaño del tile, se obtiene la posicion en filas y columnas (row y col). Esto se hace para
 * facilitar el manejo de las coordenadas. Otra cosa a tener en cuenta, es que a las coordenadas x-y se le suma la
 * hitbox para obtener la posicion exacta del rectangulo colisionador y no del frame.
 */

public class Position {

    public int x, y;

}
