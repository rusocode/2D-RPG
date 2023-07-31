package com.craivet.gfx;

import com.craivet.util.Utils;

import java.awt.image.BufferedImage;

import static com.craivet.util.Global.tile_size;

public class Frame {

    public BufferedImage[] movement;
    public BufferedImage[] weapon;
    public BufferedImage[] down, up, left, right;

    public int movementNum = 1, attackNum = 1;

    // TODO Tendria que moverse a otro lado para desacoplar la carga de frames independiente de cada entidad de la animacion que es esta clase

    /**
     * Carga los frames de movimiento en el array.
     *
     * @param ss SpriteSheet con los frames de movimiento.
     * @param w  ancho del frame.
     * @param h  alto del frame.
     * @param s  valor de escala.
     */
    public void loadMovementFrames(SpriteSheet ss, int w, int h, int s) {
        BufferedImage[] frames = SpriteSheet.getMovementFrames(ss, w, h);
        movement = new BufferedImage[frames.length];
        for (int i = 0; i < frames.length; i++)
            movement[i] = Utils.scaleImage(frames[i], s, s);
    }

    public void loadMovementFrames2(SpriteSheet ss, int w, int h) {
        BufferedImage[] frames = SpriteSheet.getMovementFrames(ss, w, h);
        movement = new BufferedImage[frames.length];
        System.arraycopy(frames, 0, movement, 0, frames.length);
    }

    public void loadMovementFrames3(SpriteSheet ss, int w, int h, int sw, int sh) {
        BufferedImage[] down = SpriteSheet.getMovementFramesDown(ss, w, h);
        BufferedImage[] up = SpriteSheet.getMovementFramesUp(ss, w, h);
        BufferedImage[] left = SpriteSheet.getMovementFramesLeft(ss, w, h);
        BufferedImage[] right = SpriteSheet.getMovementFramesRight(ss, w, h);
        this.down = new BufferedImage[down.length];
        this.up = new BufferedImage[up.length];
        this.left = new BufferedImage[left.length];
        this.right = new BufferedImage[right.length];
        for (int i = 0; i < 6; i++) {
            this.down[i] = Utils.scaleImage(down[i], sw, sh);
            this.up[i] = Utils.scaleImage(up[i], sw, sh);
            if (i < 5) {
                this.left[i] = Utils.scaleImage(left[i], sw, sh);
                this.right[i] = Utils.scaleImage(right[i], sw, sh);
            }
        }
    }

    /**
     * Carga los frames de armas en el array.
     *
     * @param ss SpriteSheet con los frames de armas.
     * @param w  ancho del frame.
     * @param h  alto del frame.
     */
    public void loadWeaponFrames(SpriteSheet ss, int w, int h) {
        BufferedImage[] frames = SpriteSheet.getWeaponFrames(ss, w, h);
        weapon = new BufferedImage[frames.length];
        weapon[0] = Utils.scaleImage(frames[0], tile_size, tile_size * 2);
        weapon[1] = Utils.scaleImage(frames[1], tile_size, tile_size * 2);
        weapon[2] = Utils.scaleImage(frames[2], tile_size, tile_size * 2);
        weapon[3] = Utils.scaleImage(frames[3], tile_size, tile_size * 2);
        weapon[4] = Utils.scaleImage(frames[4], tile_size * 2, tile_size);
        weapon[5] = Utils.scaleImage(frames[5], tile_size * 2, tile_size);
        weapon[6] = Utils.scaleImage(frames[6], tile_size * 2, tile_size);
        weapon[7] = Utils.scaleImage(frames[7], tile_size * 2, tile_size);
    }
}
