package com.punkipunk.gfx;

import com.punkipunk.gfx.SpriteSheet.SpriteRegion;

/**
 * En los videojuegos, un <b>frame</b> se refiere a un fotograma individual de una animacion o imagen que se muestra en la
 * pantalla durante un periodo de tiempo especifico. Los videojuegos, al igual que las peliculas, funcionan mostrando una
 * secuencia rapida de imagenes estaticas para crear la ilusion de movimiento. Cada una de estas imagenes estaticas es un frame.
 */

public class Animation {

    private final int speed;
    private final SpriteRegion[] frames;
    private int index;
    private long lastTime, timer;

    public Animation(int speed, SpriteRegion[] frames) {
        this.speed = speed;
        this.frames = frames;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }

    /**
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
    public SpriteRegion getCurrentFrame() {
        return frames[index];
    }

    /**
     * Gets the first frame.
     *
     * @return the first frame.
     */
    public SpriteRegion getFirstFrame() {
        return frames[0];
    }

    /**
     * Gets the last frame.
     *
     * @return the last frame.
     */
    public SpriteRegion getLastFrame() {
        return frames[frames.length - 1];
    }

    /**
     * Get the frame index.
     * <p>
     * Necesario para sincronizar con entity.animationIndex.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the frame index.
     *
     * @param i frame index.
     */
    public void setFrame(int i) {
        index = i;
    }

}
