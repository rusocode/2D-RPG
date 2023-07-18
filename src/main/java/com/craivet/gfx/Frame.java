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

    public BufferedImage[] movement = new BufferedImage[2]; // Array que contendra los frames de movimiento
    public BufferedImage[] weapon; // Array que contendra los frames de ataque

    public int movementNum = 1, attackNum = 1;

}
