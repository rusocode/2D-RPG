package com.craivet.gfx;

import com.craivet.util.Utils;

import java.awt.image.BufferedImage;

import static com.craivet.util.Global.*;

public class SpriteSheet {

    private BufferedImage image;
    public BufferedImage[] movement; // Entidades con dos frames para cada direccion
    public BufferedImage[] weapon;
    public int movementNum = 1, attackNum = 1;
    public BufferedImage[] down, up, left, right; // Player con mas de un frame para cada direccion

    public SpriteSheet() {
    }

    public SpriteSheet(BufferedImage image) {
        this.image = image;
    }

    public static BufferedImage[] getIconsSubimages(SpriteSheet ss, int w, int h) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        BufferedImage[] subimages = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                subimages[i++] = ss.crop(x * w, y * h, w, h);
        return subimages;
    }

    /**
     * Obtiene los frames de movimiento.
     *
     * <p>TODO Incluir funcion para anchos y altos de subimagenes diferentes (por ejemplo, si el parametro es true uso switch)
     * <p>TODO Arreglar la cantidad de iteraciones que hace el for y
     *
     * @param ss SpriteSheet con los frames de movimiento.
     * @param w  ancho del frame.
     * @param h  alto del frame.
     * @return una matriz con los frames del SpriteSheet.
     */
    public BufferedImage[] loadMovementFrames(SpriteSheet ss, int w, int h, int scale) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        movement = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                movement[i++] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), scale, scale);
        return movement;
    }

    /**
     * Obtiene los frames de armas.
     *
     * <p>TODO Arreglar la cantidad de iteraciones que hace el for y
     *
     * @param ss SpriteSheet con los frames de armas.
     * @param w  ancho del frame.
     * @param h  alto del frame.
     * @return una matriz con los frames del SpriteSheet.
     */
    public BufferedImage[] loadWeaponFrames(SpriteSheet ss, int w, int h) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        weapon = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                if (y == 0) weapon[i++] = Utils.scaleImage(ss.crop(x * w, 0, 16, 32), tile_size, tile_size * 2);
                if (y == 1) weapon[i++] = Utils.scaleImage(ss.crop(x * w, 32, 16, 32), tile_size, tile_size * 2);
                if (y == 2) {
                    if (x == 0) weapon[i++] = Utils.scaleImage(ss.crop(0, 64, 32, 16), tile_size * 2, tile_size);
                    if (x == 1) weapon[i++] = Utils.scaleImage(ss.crop(0, 80, 32, 16), tile_size * 2, tile_size);
                }
                if (y == 3) {
                    if (x == 0) weapon[i++] = Utils.scaleImage(ss.crop(0, 96, 32, 16), tile_size * 2, tile_size);
                    if (x == 1) weapon[i++] = Utils.scaleImage(ss.crop(0, 112, 32, 16), tile_size * 2, tile_size);
                }
            }
            if (y == 3) break; // Evita iterar hasta los espacios sobrantes de la matriz
        }
        return weapon;
    }

    public void loadMovementFramesOfPlayer(SpriteSheet ss, int scale) {

        final int col = 6;
        final int row = 4;
        int w = ss.getWidth() / col;
        int h = ss.getHeight() / row;

        final int number_frame_down = 6;
        final int number_frame_up = 6;
        final int number_frame_left = 5;
        final int number_frame_right = 5;
        down = new BufferedImage[number_frame_down];
        up = new BufferedImage[number_frame_up];
        left = new BufferedImage[number_frame_left];
        right = new BufferedImage[number_frame_right];

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                switch (y) { // Controla las filas
                    case 0 -> down[x] = Utils.scaleImage(ss.crop(x * w, 0, w, h), w * scale, h * scale);
                    case 1 -> up[x] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
                    // Los frames izquierdos y derechos solo tienen 5 frames, por lo tanto comprueba hasta el limite 5 para evitar un ArrayIndexOutOfBoundsException
                    case 2 -> {
                        if (x < number_frame_left)
                            left[x] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
                    }
                    case 3 -> {
                        if (x < number_frame_right)
                            right[x] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
                    }
                }
            }
        }

    }

    /**
     * Devuelve una subimagen definida por una region rectangular especificada. La BufferedImage devuelta comparte la
     * misma matriz de datos que la imagen original. En otras palabras, corta la subimagen del SpriteSheet.
     *
     * @param x coordenada x de la esquina superior izquierda de la region rectangular especificada.
     * @param y coordenada y de la esquina superior izquierda de la region rectangular especificada.
     * @param w ancho de la region rectangular especificada.
     * @param h altura de la region rectangular especificada.
     * @return una BufferedImage que es la subimagen de esta BufferedImage.
     */
    private BufferedImage crop(int x, int y, int w, int h) {
        return image.getSubimage(x, y, w, h);
    }

    private int getWidth() {
        return image.getWidth();
    }

    private int getHeight() {
        return image.getHeight();
    }

}
