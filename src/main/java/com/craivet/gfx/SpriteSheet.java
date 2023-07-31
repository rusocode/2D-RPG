package com.craivet.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private final BufferedImage image;
    public static BufferedImage[] player_down, player_up, player_left, player_right;

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
        int col = ss.getWidth() / w; // 6
        int row = ss.getHeight() / h; // 4
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

    public static BufferedImage[] getMovementFramesDown(SpriteSheet ss, int w /* 25 */, int h /* 45 */) {
        final int frames_down = 6;
        int col = ss.getWidth() / w; // 6
        int row = ss.getHeight() / h; // 4
        BufferedImage[] framesDown = new BufferedImage[frames_down];
        int i = 0;
        for (int y = 0; y < row; y++) {
            if (y == 0) { // Fila 1
                for (int x = 0; x < col; x++)
                    framesDown[i++] = ss.crop(x * w, 0, w, h);
            }
        }
        return framesDown;
    }

    public static BufferedImage[] getMovementFramesUp(SpriteSheet ss, int w /* 25 */, int h /* 45 */) {
        final int frames_up = 6;
        int col = ss.getWidth() / w; // 6
        int row = ss.getHeight() / h; // 4
        BufferedImage[] framesUp = new BufferedImage[frames_up];
        int i = 0;
        for (int y = 0; y < row; y++) {
            if (y == 1) { // Fila 2
                for (int x = 0; x < col; x++)
                    framesUp[i++] = ss.crop(x * w, y * h, w, h);
            }
        }
        return framesUp;
    }

    public static BufferedImage[] getMovementFramesLeft(SpriteSheet ss, int w /* 25 */, int h /* 45 */) {
        final int frames_left = 5;
        int col = ss.getWidth() / w; // 6
        int row = ss.getHeight() / h; // 4
        BufferedImage[] framesLeft = new BufferedImage[frames_left];
        int i = 0;
        for (int y = 0; y < row; y++) {
            if (y == 2) { // Fila 3
                for (int x = 0; x < col; x++)
                    if (x < 5) framesLeft[i++] = ss.crop(x * w, y * h, w, h);
            }
        }
        return framesLeft;
    }

    public static BufferedImage[] getMovementFramesRight(SpriteSheet ss, int w /* 25 */, int h /* 45 */) {
        final int frames_right = 5;
        int col = ss.getWidth() / w; // 6
        int row = ss.getHeight() / h; // 4
        BufferedImage[] framesRight = new BufferedImage[frames_right];
        int i = 0;
        for (int y = 0; y < row; y++) {
            if (y == 3) { // Fila 4
                for (int x = 0; x < col; x++)
                    if (x < 5) framesRight[i++] = ss.crop(x * w, y * h, w, h);
            }
        }
        return framesRight;
    }

    public static void initPlayer(SpriteSheet player) {

        player_down = new BufferedImage[6];
        player_up = new BufferedImage[6];
        player_left = new BufferedImage[5];
        player_right = new BufferedImage[5];

        int player_width = player.getWidth() / player_down.length;
        int player_height = player.getHeight() / (player_left.length - 1);

        for (int y = 0; y < player.getHeight() / player_height; y++) {
            for (int x = 0; x < player.getWidth() / player_width; x++) {
                switch (y) { // Controla las filas
                    case 0 -> player_down[x] = player.crop(x * player_width, 0, player_width, player_height);
                    case 1 ->
                            player_up[x] = player.crop(x * player_width, y * player_height, player_width, player_height);
                    case 2 -> {
                        /* Los movimientos izquierdos y derechos solo tienen 5 frames, por lo tanto comprueba hasta el
                         * limite 5 para evitar un ArrayIndexOutOfBoundsException. */
                        if (x < 5)
                            player_left[x] = player.crop(x * player_width, y * player_height, player_width, player_height);
                    }
                    case 3 -> {
                        if (x < 5)
                            player_right[x] = player.crop(x * player_width, y * player_height, player_width, player_height);
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
