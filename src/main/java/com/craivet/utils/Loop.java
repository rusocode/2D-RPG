package com.craivet.utils;

import static com.craivet.utils.Global.*;

/**
 * GameLoop timer.
 * <p>
 * <a href="https://gameprogrammingpatterns.com/update-method.html">Update Method</a>
 */
public class Loop {

    public int framesInRender;
    public boolean showFPS;
    private int ticks, framesInConsole;
    private long lastUpdate, lastFrame;
    private final double nsPerUpdate, nsPerFrame;
    private double unprocessed;
    private long timer;

    public Loop() {
        lastUpdate = System.nanoTime();
        lastFrame = System.nanoTime();
        timer = System.currentTimeMillis();
        nsPerUpdate = 1E9 / UPDATES;
        nsPerFrame = 1E9 / FRAMES;
    }

    /**
     * Checks if the time necessary to update the game has passed based on the fixed update timestep.
     *
     * @return true if a game update should be updated or false.
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
     * Checks if the time necessary to render the game has passed based on the fixed rendering timestep.
     *
     * @return true if the game should be rendered or false.
     */
    public boolean shouldRender() {
        boolean shouldRender = false;
        long currentTime = System.nanoTime();
        // If the FPS_UNLIMITED option is activated or if the time between each frame is reached
        if (FPS_UNLIMITED || currentTime - lastFrame >= nsPerFrame) {
            framesInConsole++;
            lastFrame = System.nanoTime();
            shouldRender = true;
        }
        return shouldRender;
    }

    /**
     * Time the number of ticks and frames each time interval.
     *
     * @param interval time interval in milliseconds.
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
