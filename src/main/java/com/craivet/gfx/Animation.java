package com.craivet.gfx;

import java.awt.image.BufferedImage;

/**
 * In video games, a "frame" refers to an individual frame of animation or image that is displayed on the screen for a
 * specific period of time. Video games, like movies, work by displaying a rapid sequence of static images to create the
 * illusion of movement. Each of these static images is a frame.
 * <p>
 * In this case we have the movement frames (in four directions) and the attack frames (in four directions) for each
 * entity. Having few frames it would be unnecessary to increase the fps since they do not influence the sensation of
 * animation. Only the magnification would be used for camera movement.
 */

public class Animation {

    private final int speed;
    private int index;
    private long lastTime, timer;
    private final BufferedImage[] frames;

    public Animation(int speed, BufferedImage[] frames) {
        this.speed = speed;
        this.frames = frames;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }

    /**
     * Updates the frame depending on the speed. If the timer reaches the specified speed, then it changes the frame by
     * incrementing the rate. If I reach the last index, it returns to the first frame.
     */
    public void tick() {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis(); // The player goes crazy without this line xD
        if (timer > speed) {
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
    public BufferedImage getCurrentFrame() {
        return frames[index];
    }

    /**
     * Gets the first frame.
     *
     * @return the first frame.
     */
    public BufferedImage getFirstFrame() {
        return frames[0];
    }

    /**
     * Gets the last frame.
     *
     * @return the last frame.
     */
    public BufferedImage getLastFrame() {
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
