package com.punkipunk.gfx;

import com.punkipunk.utils.Utils;
import com.punkipunk.entity.Entity;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import static com.punkipunk.utils.Global.tile;

public class SpriteSheet {

    // Entities with two frames for each direction (movement and attack)
    public Image[] movement, attack, item;
    public int movementNum = 1, attackNum = 1;
    public Image[] down, up, left, right; // Player with more than one frame for each direction
    public Image[] weapon; // Item with a frame for each direction
    // Represents the first frame of each entity
    public Image frame; // TODO Cambiar nombre y mover
    // SpriteSheet
    private Image image;

    public SpriteSheet() {
    }

    public SpriteSheet(Image image) {
        this.image = image;
    }

    public static Image[] getIconsSubimages(SpriteSheet ss, int w, int h) {
        double col = ss.getWidth() / w;
        double row = ss.getHeight() / h;
        Image[] subimages = new Image[(int) (col * row)];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                subimages[i++] = ss.crop(x * w, y * h, w, h);
        return subimages;
    }

    public void loadItemFrames(SpriteSheet ss, int w, int h, int scale) {
        double col = ss.getWidth() / w;
        double row = ss.getHeight() / h;
        item = new Image[(int) (col * row)];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                item[i++] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), scale * tile, scale * tile);
        frame = item[0];
    }

    /**
     * Load movement frames.
     *
     * <p>TODO Include functions for different subimage widths and heights (for example, if the parameter is true use switch)
     * <p>FIXED Fix the number of iterations that the for y
     *
     * @param ss    SpriteSheet with the movement frames.
     * @param w     frame width.
     * @param h     frame height.
     * @param scale scale value or 1 to maintain size.
     */
    public void loadMovementFrames(SpriteSheet ss, int w, int h, int scale) {
        double col = ss.getWidth() / w;
        double row = ss.getHeight() / h;
        movement = new Image[(int) (col * row)];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                movement[i++] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), scale * w, scale * h);
        // Stores the first frame of motion
        frame = movement[0];
    }

    /**
     * Load attack frames.
     *
     * <p>FIXED Fix the number of iterations that the for y
     *
     * @param w     frame width.
     * @param h     frame height.
     * @param scale scale value or 1 to maintain size.
     * @param ss    SpriteSheet with the movement frames.
     */
    public void loadAttackFrames(SpriteSheet ss, int w, int h, int scale) {
        double col = ss.getWidth() / w;
        double row = ss.getHeight() / h;
        attack = new Image[(int) (col * row)];
        int i = 0;
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                // if (y == 4) break;
                if (y < 2)
                    attack[i++] = Utils.scaleTexture(ss.crop(x * w, y == 0 ? 0 : h * 2, w, h * 2), tile * scale, tile * scale * 2);
                if (y >= 2)
                    attack[i++] = Utils.scaleTexture(ss.crop(0, y == 2 ? h * (4 + x) : h * (6 + x), w * 2, h), tile * scale * 2, tile * scale);
            }
        }
    }

    public void loadPlayerMovementFrames(SpriteSheet ss, int scale) {
        int col = 6, row = 4;
        double w = ss.getWidth() / col, h = ss.getHeight() / row;
        int framesDown = 6, framesUp = 6, framesLeft = 5, framesRight = 5;

        down = new Image[framesDown];
        up = new Image[framesUp];
        left = new Image[framesLeft];
        right = new Image[framesRight];

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                switch (y) { // Control the lines
                    case 0 -> down[x] = Utils.scaleTexture(ss.crop(x * w, 0, w, h), (int) w * scale, (int) h * scale);
                    case 1 -> up[x] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), (int) w * scale, (int) h * scale);
                    // The left and right frames only have 5 frames, so check up to the limit 5 to avoid an ArrayIndexOutOfBoundsException
                    case 2 -> {
                        if (x < framesLeft)
                            left[x] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), (int) w * scale, (int) h * scale);
                    }
                    case 3 -> {
                        if (x < framesRight)
                            right[x] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), (int) w * scale, (int) h * scale);
                    }
                }
            }
        }

        frame = up[0];

    }

    public void loadOrcMovementFrames(SpriteSheet ss, int scale) {
        int col = 6, row = 4;
        double w = ss.getWidth() / col, h = ss.getHeight() / row;
        int framesDown = 6, framesUp = 6, framesLeft = 5, framesRight = 5;

        down = new Image[framesDown];
        up = new Image[framesUp];
        left = new Image[framesLeft];
        right = new Image[framesRight];

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                switch (y) {
                    case 0 -> down[x] = Utils.scaleTexture(ss.crop(x * w, 0, w, h), (int) w * scale, (int) h * scale);
                    case 1 -> up[x] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), (int) w * scale, (int) h * scale);
                    case 2 -> {
                        if (x < framesLeft)
                            left[x] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), (int) w * scale, (int) h * scale);
                    }
                    case 3 -> {
                        if (x < framesRight)
                            right[x] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), (int) w * scale, (int) h * scale);
                    }
                }
            }
        }

        frame = down[0];

    }

    public void loadOldmanFrames(SpriteSheet ss, int scale) {
        int col = 3, row = 4;
        double w = ss.getWidth() / col, h = ss.getHeight() / row;
        int frames = 3;

        down = new Image[frames];
        up = new Image[frames];
        left = new Image[frames];
        right = new Image[frames];

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                switch (y) {
                    // TODO Volar numeros magicos
                    case 0 -> down[x] = Utils.scaleTexture(ss.crop(x * w, 0, w, h), (int) w * scale, (int) h * scale);
                    case 1 -> up[x] = Utils.scaleTexture(ss.crop(x * w, 48, w, h), (int) w * scale, (int) h * scale);
                    case 2 -> left[x] = Utils.scaleTexture(ss.crop(x * w, 96, w, h), (int) w * scale, (int) h * scale);
                    case 3 -> right[x] = Utils.scaleTexture(ss.crop(x * w, 144, w, h), (int) w * scale, (int) h * scale);
                }
            }
        }

        frame = down[0];

    }

    public void loadBurstOfFireFrames(SpriteSheet ss, int scale) {
        int col = 5, row = 7;
        int w = (int) (ss.getWidth() / col), h = (int) (ss.getHeight() / row);
        int frames = 5;

        down = new Image[frames];
        up = new Image[frames];
        left = new Image[frames];
        right = new Image[frames];

        /* Indices para controlar los frames up y left, en donde estos empiezan desde el "ultimo" (en realidad es el
         * primer frame) frame en el SpriteSheet. */
        int iUp = 4, iLeft = 4;

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                switch (y) {
                    case 0 -> down[x] = Utils.scaleTexture(ss.crop(0, x * w, w, h), w * scale, h * scale);
                    case 1 -> up[x] = Utils.scaleTexture(ss.crop(w, iUp-- * w, w, h), w * scale, h * scale);
                    case 2 -> left[x] = Utils.scaleTexture(ss.crop(iLeft-- * w, 160, w, h), w * scale, h * scale);
                    case 3 -> right[x] = Utils.scaleTexture(ss.crop(x * w, 192, w, h), w * scale, h * scale);
                }
            }
        }

        frame = right[0];

    }

    public void loadWeaponFrames(SpriteSheet ss, int w, int h, int scale) {
        int col = (int) (ss.getWidth() / w);
        int row = (int) (ss.getHeight() / h);
        weapon = new Image[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                weapon[i++] = Utils.scaleTexture(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
    }

    /**
     * Returns a subimage defined by a specified rectangular region. The returned BufferedImage shares the same data array as the
     * original image. In other words, it cuts the subimage from the SpriteSheet.
     *
     * @param x x coordinate of the upper left corner of the specified rectangular region.
     * @param y y coordinate of the upper left corner of the specified rectangular region.
     * @param w width of the specified rectangular region.
     * @param h height of the specified rectangular region.
     * @return a BufferedImage that is the subimage of this BufferedImage.
     */
    private Image crop(double x, double y, double w, double h) {
        PixelReader pixelReader = image.getPixelReader();
        WritableImage newImage = new WritableImage((int) w, (int) h);
        PixelWriter pixelWriter = newImage.getPixelWriter();
        for (int ys = 0; ys < h; ys++) {
            for (int xs = 0; xs < w; xs++) {
                int argb = pixelReader.getArgb((int) (x + xs), (int) (y + ys));
                pixelWriter.setArgb(xs, ys, argb);
            }
        }
        return newImage;
    }

    /**
     * Gets the current frame.
     *
     * @param entity entity.
     * @return the current frame of the entity.
     */
    public Image getCurrentAnimationFrame(Entity entity) {
        int i = 0;

        if (!entity.flags.hitting) {
            if (movement.length == 2) { // If these are entities with two frames
                switch (entity.direction) {
                    case DOWN, UP, LEFT, RIGHT -> i = movementNum == 1 || entity.flags.colliding ? 0 : 1;
                }
            } else {
                switch (entity.direction) {
                    case DOWN -> i = movementNum == 1 || entity.flags.colliding ? 0 : 1;
                    case UP -> i = movementNum == 1 || entity.flags.colliding ? 2 : 3;
                    case LEFT -> i = movementNum == 1 || entity.flags.colliding ? 4 : 5;
                    case RIGHT -> i = movementNum == 1 || entity.flags.colliding ? 6 : 7;
                }
            }
        } else {
            switch (entity.direction) {
                case DOWN -> i = attackNum == 1 ? 0 : 1;
                case UP -> {
                    // The width of the image is subtracted in case the frame is larger than the tile
                    entity.tempScreenY -= (int) entity.sheet.frame.getHeight();
                    i = attackNum == 1 ? 2 : 3;
                }
                case LEFT -> {
                    entity.tempScreenX -= (int) entity.sheet.frame.getWidth();
                    i = attackNum == 1 ? 4 : 5;
                }
                case RIGHT -> i = attackNum == 1 ? 6 : 7;
            }
        }

        return !entity.flags.hitting ? movement[i] : attack[i];
    }

    private double getWidth() {
        return image.getWidth();
    }

    private double getHeight() {
        return image.getHeight();
    }

}
