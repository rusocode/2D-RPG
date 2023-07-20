package com.craivet.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private final BufferedImage image;

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
    public static BufferedImage[] getMovementFrames(SpriteSheet ss, int w, int h) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        BufferedImage[] frames = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                frames[i++] = ss.crop(x * w, y * h, w, h);
        return frames;
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
    public static BufferedImage[] getWeaponFrames(SpriteSheet ss, int w, int h) {
        int col = ss.getWidth() / w; // 2
        int row = ss.getHeight() / h; // 8
        BufferedImage[] frames = new BufferedImage[col * row]; // Necesito guardar 8 imagenes no 16
        int i = 0;
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                if (y == 0) frames[i++] = ss.crop(x * w, 0, 16, 32);
                if (y == 1) frames[i++] = ss.crop(x * w, 32, 16, 32);
                if (y == 2) {
                    if (x == 0) frames[i++] = ss.crop(0, 64, 32, 16);
                    if (x == 1) frames[i++] = ss.crop(0, 80, 32, 16);
                }
                if (y == 3) {
                    if (x == 0) frames[i++] = ss.crop(0, 96, 32, 16);
                    if (x == 1) frames[i++] = ss.crop(0, 112, 32, 16);
                }
            }
            if (y == 3) break; // Evita iterar hasta los espacios sobrantes de la matriz
        }
        return frames;
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
