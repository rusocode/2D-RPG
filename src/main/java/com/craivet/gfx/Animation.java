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

public class Animation {

    public int index;
    int speed;
    private long lastTime, timer;
    private final BufferedImage[] frames;

    public Animation(int speed, BufferedImage[] frames) {
        this.frames = frames;
    }

    /**
     * Actualiza el frame dependiendo de la velocidad. Si el temporizador alcanzo la velocidad especificada, entonces
     * cambia de frame incrementando el indice. Si llego al ultimo indice, vuelve al primer frame.
     */
    public void tick() {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis(); // El player se vuelve loco sin esta linea xD
        if (timer > 90) {
            index++;
            timer = 0;
            if (index >= frames.length) index = 0;
        }
    }

    /**
     * Obtiene el frame actual.
     *
     * @return el frame actual.
     */
    public BufferedImage getCurrentFrame() {
        return frames[index];
    }

    /**
     * Obtiene el primer frame.
     *
     * @return el primer frame.
     */
    public BufferedImage getFirstFrame() {
        return frames[0];
    }

}
