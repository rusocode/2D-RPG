package com.craivet.gfx;

import com.craivet.util.Utils;

import java.awt.image.BufferedImage;

import static com.craivet.util.Global.tile_size;

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

    public BufferedImage[] movement;
    public BufferedImage[] weapon;

    //
    public int movementNum = 1, attackNum = 1;

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
