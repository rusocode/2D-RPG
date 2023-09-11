package com.craivet.gfx;

import com.craivet.utils.Utils;

import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class SpriteSheet {

    private BufferedImage image;

    // Entidades con dos frames para cada direccion (movimiento y ataque)
    public BufferedImage[] movement, attack;
    public int movementNum = 1, attackNum = 1;

    public BufferedImage[] down, up, left, right; // Player con mas de un frame para cada direccion
    public BufferedImage[] item; // Item con un frame para cada direccion

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
     * Carga los frames de movimiento.
     *
     * <p>TODO Incluir funcion para anchos y altos de subimagenes diferentes (por ejemplo, si el parametro es true uso switch)
     * <p>TODO Arreglar la cantidad de iteraciones que hace el for y
     *
     * @param ss    SpriteSheet con los frames de movimiento.
     * @param w     ancho del frame.
     * @param h     alto del frame.
     * @param scale valor de escala o 1 para mantener el tamaño.
     */
    public void loadMovementFrames(SpriteSheet ss, int w, int h, int scale) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        movement = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                movement[i++] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), scale * tile, scale * tile);
    }

    /**
     * Carga los frames de ataque.
     *
     * <p>TODO Arreglar la cantidad de iteraciones que hace el for y
     *
     * @param ss    SpriteSheet con los frames de ataque.
     * @param w     ancho del frame.
     * @param h     alto del frame.
     * @param scale valor de escala o 1 para mantener el tamaño.
     */
    public void loadAttackFrames(SpriteSheet ss, int w, int h, int scale) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        attack = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                if (y < 2) // Controla la fila 0 y 1 de 16x32px
                    attack[i++] = Utils.scaleImage(ss.crop(x * w, y == 0 ? 0 : h * 2, w, h * 2), tile * scale, tile * scale * 2);
                if (y >= 2) // Controla la fila 2 y 3 de 32x16px, PERO NO FUNCIONA PARA EL SS del skeleton
                    attack[i++] = Utils.scaleImage(ss.crop(0, y == 2 ? h * (4 + x) : h * (6 + x), w * 2, h), tile * scale * 2, tile * scale);
                if (y == 2) {
                    if (x == 0) attack[i++] = Utils.scaleImage(ss.crop(0, h * 4, w * 2, h), tile * scale * 2, tile * scale);
                    if (x == 1) attack[i++] = Utils.scaleImage(ss.crop(0, h * 5, w * 2, h), tile * scale * 2, tile * scale);
                }
                if (y == 3) {

                }
            }
        }
    }

    public void loadPlayerMovementFrames(SpriteSheet ss, int scale) {
        int col = 6, row = 4;
        int w = ss.getWidth() / col, h = ss.getHeight() / row;
        int numberFramesDown = 6, numberFramesUp = 6, numberFramesLeft = 5, numberFramesRight = 5;

        down = new BufferedImage[numberFramesDown];
        up = new BufferedImage[numberFramesUp];
        left = new BufferedImage[numberFramesLeft];
        right = new BufferedImage[numberFramesRight];

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                switch (y) { // Controla las filas
                    case 0 -> down[x] = Utils.scaleImage(ss.crop(x * w, 0, w, h), w * scale, h * scale);
                    case 1 -> up[x] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
                    // Los frames izquierdos y derechos solo tienen 5 frames, por lo tanto comprueba hasta el limite 5 para evitar un ArrayIndexOutOfBoundsException
                    case 2 -> {
                        if (x < numberFramesLeft)
                            left[x] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
                    }
                    case 3 -> {
                        if (x < numberFramesRight)
                            right[x] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
                    }
                }
            }
        }

    }

    public void loadItem(SpriteSheet ss, int w, int h) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        item = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                item[i++] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w, h);
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
