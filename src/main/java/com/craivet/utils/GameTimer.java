package com.craivet.utils;

import static com.craivet.utils.Global.*;

/**
 * Temporizador del GameLoop.
 */

public class GameTimer {

    public int framesInRender;
    public boolean showFPS;
    private int ticks = 0, framesInConsole = 0;
    private long lastUpdate, lastFrame;
    private final double nsPerUpdate, nsPerFrame;
    private double unprocessed;
    private long timer;

    public GameTimer() {
        lastUpdate = System.nanoTime();
        lastFrame = System.nanoTime();
        timer = System.currentTimeMillis();
        nsPerUpdate = 1E9 / UPDATES;
        nsPerFrame = 1E9 / FRAMES;
    }

    /**
     * Verifica si ha pasado el tiempo necesario para realizar una actualizacion del juego en funcion del timestep fijo
     * de actualizacion definido.
     *
     * @return true si se debe realizar una actualizacion del juego, o false.
     */
    public boolean shouldUpdate() {
        boolean shouldUpdate = false;
        long currentTime = System.nanoTime();
        unprocessed += (currentTime - lastUpdate) / nsPerUpdate;
        lastUpdate = currentTime;
        while (unprocessed >= 1) {
            ticks++;
            unprocessed--;
            shouldUpdate = true;
        }
        return shouldUpdate;
    }

    /**
     * Verifica si ha pasado el tiempo necesario para realizar una renderizacion del juego en funcion del timestep fijo
     * de renderizacion definido.
     *
     * @return true si se debe realizar una renderizacion del juego, o false.
     */
    public boolean shouldRender() {
        boolean shouldRender = false;
        long currentTime = System.nanoTime();
        // Si la opcion FPS_UNLIMITED esta activada o si alcanzo el tiempo entre cada frame
        if (FPS_UNLIMITED || currentTime - lastFrame >= nsPerFrame) {
            framesInConsole++;
            lastFrame = System.nanoTime();
            shouldRender = true;
        }
        return shouldRender;
    }

    /**
     * Temporiza la cantidad de ticks y frames cada un intervalo de tiempo.
     *
     * @param interval intervalo de tiempo en milisegundos.
     */
    public void timer(int interval) {
        if (System.currentTimeMillis() - timer >= interval) {
            System.out.println(ticks + " ticks, " + framesInConsole + " fps");
            timer = System.currentTimeMillis();
            ticks = 0;
            framesInRender = framesInConsole;
            framesInConsole = 0;
            showFPS = true;
        }
    }

}
