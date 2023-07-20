package com.craivet.gfx;

import java.awt.image.BufferedImage;

/**
 * En los videojuegos, un "frame" se refiere a un cuadro individual de animacion o imagen que se muestra en la pantalla
 * durante un periodo de tiempo especifico. Los videojuegos, al igual que las peliculas, funcionan mostrando una
 * secuencia rapida de imagenes estaticas para crear la ilusion de movimiento. Cada una de estas imagenes estaticas es
 * un frame.
 * <p>
 * En este caso tenemos los frames de movimiento (en cuatro direcciones) y los de ataque (en cuatro direcciones) para
 * cada entidad. Al tener pocos frames seria innecesario aumentar los fps ya que no influyen en la sensacion de
 * animacion. Solo el aumento se aprovecharia para el movimiento de la camara.
 */

public class Frame {

    /* Lo que esta pasando es que el array solo se inicializa para los mobs, pero para los items, projectile y tile
     * interactivos nunca sucede ya que estos utilizan la variable imagen para representar la imagen. Por lo tanto el
     * render de Entity se llama para todas las entidades y al no inicializar el array de movement para los items, etc.
     * lanza NPE ya que estas utlimas no inicializaron ese array. */
    public BufferedImage[] movement; // Array que contendra los frames de movimiento
    public BufferedImage[] weapon; // Array que contendra los frames de ataque

    public int movementNum = 1, attackNum = 1;

}
