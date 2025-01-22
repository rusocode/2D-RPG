package com.punkipunk.gfx;

import javafx.scene.image.Image;

/**
 * <p>
 * En los videojuegos, un "frame" se refiere a un fotograma individual de una animacion o imagen que se muestra en la pantalla
 * durante un periodo de tiempo especifico. Los videojuegos, al igual que las peliculas, funcionan mostrando una secuencia rapida
 * de imagenes estaticas para crear la ilusion de movimiento. Cada una de estas imagenes estaticas es un frame.
 * <p>
 * En este caso tenemos los frames de movimiento (en cuatro direcciones) y los frames de ataque (en cuatro direcciones) para cada
 * entidad. Al tener pocos frames no seria necesario aumentar los fps ya que no influyen en la sensacion de animacion.
 */

public class Animation {

    private final int speed;
    private final Image[] frames;
    private int index;
    private long lastTime, timer;

    public Animation(int speed, Image[] frames) {
        this.speed = speed;
        this.frames = frames;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }

    /**
     * <p>
     * Actualiza el frame en funcion de la velocidad. Si el temporizador alcanza la velocidad especificada, cambia el frame
     * incrementando la velocidad. Si llego al ultimo indice, vuelve al primer frame.
     */
    public void tick() {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis(); // The player goes crazy without this line xD
        if (timer >= speed) {
            index++;
            timer = 0;
            if (index >= frames.length) index = 0;
        }
    }

    /**
     * Gets the current frame.
     *
     * @return the current frame.
     */
    public Image getCurrentFrame() {
        return frames[index];
    }

    /**
     * Gets the first frame.
     *
     * @return the first frame.
     */
    public Image getFirstFrame() {
        return frames[0];
    }

    /**
     * Gets the last frame.
     *
     * @return the last frame.
     */
    public Image getLastFrame() {
        return frames[frames.length - 1];
    }

    /**
     * Set the frame.
     *
     * @param i frame index.
     */
    public void setFrame(int i) {
        index = i;
    }

}
