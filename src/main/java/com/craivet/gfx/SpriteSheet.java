package com.craivet.gfx;

import com.craivet.utils.Utils;
import com.craivet.world.entity.Entity;

import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class SpriteSheet {

    // SpriteSheet
    private BufferedImage image;

    // Entities with two frames for each direction (movement and attack)
    public BufferedImage[] movement, attack, item;
    public int movementNum = 1, attackNum = 1;

    public BufferedImage[] down, up, left, right; // Player with more than one frame for each direction
    public BufferedImage[] weapon; // Item with a frame for each address

    // Represents the first frame of each entity
    public BufferedImage frame;

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

    public void loadItemFrames(SpriteSheet ss, int w, int h, int scale) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        item = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                item[i++] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), scale * tile, scale * tile);
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
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        movement = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                movement[i++] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), scale * tile, scale * tile);
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
     * @@param ss    SpriteSheet with the movement frames.
     */
    public void loadAttackFrames(SpriteSheet ss, int w, int h, int scale) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        attack = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                // if (y == 4) break;
                if (y < 2)
                    attack[i++] = Utils.scaleImage(ss.crop(x * w, y == 0 ? 0 : h * 2, w, h * 2), tile * scale, tile * scale * 2);
                if (y >= 2)
                    attack[i++] = Utils.scaleImage(ss.crop(0, y == 2 ? h * (4 + x) : h * (6 + x), w * 2, h), tile * scale * 2, tile * scale);
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
                switch (y) { // Control the lines
                    case 0 -> down[x] = Utils.scaleImage(ss.crop(x * w, 0, w, h), w * scale, h * scale);
                    case 1 -> up[x] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
                    // The left and right frames only have 5 frames, so check up to the limit 5 to avoid an ArrayIndexOutOfBoundsException
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

        frame = down[0];

    }

    public void loadBurstOfFireFrames(SpriteSheet ss, int scale) {
        int col = 5, row = 1;
        int w = ss.getWidth() / col, h = ss.getHeight() / row;
        int numberFrames = 5;

        down = new BufferedImage[numberFrames];
        up = new BufferedImage[numberFrames];
        left = new BufferedImage[numberFrames];
        right = new BufferedImage[numberFrames];

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                switch (y) {
                    case 0 -> right[x] = Utils.scaleImage(ss.crop(x * w, 0, w, h), w * scale, h * scale);
                    // case 1 -> up[x] = Utils.scaleImage(ss.crop(x * w, 0, w, h), w * scale, h * scale);
                   // case 2 -> left[x] = Utils.scaleImage(ss.crop(x * w, 0, w, h), w * scale, h * scale);
                   // case 3 -> right[x] = Utils.scaleImage(ss.crop(x * w, 0, w, h), w * scale, h * scale);

                }
            }
        }

        frame = right[0];

    }

    public void loadWeaponFrames(SpriteSheet ss, int w, int h, int scale) {
        int col = ss.getWidth() / w;
        int row = ss.getHeight() / h;
        weapon = new BufferedImage[col * row];
        int i = 0;
        for (int y = 0; y < row; y++)
            for (int x = 0; x < col; x++)
                weapon[i++] = Utils.scaleImage(ss.crop(x * w, y * h, w, h), w * scale, h * scale);
    }

    /**
     * Returns a subimage defined by a specified rectangular region. The returned BufferedImage shares the same data
     * array as the original image. In other words, it cuts the subimage from the SpriteSheet.
     *
     * @param x x coordinate of the upper left corner of the specified rectangular region.
     * @param y y coordinate of the upper left corner of the specified rectangular region.
     * @param w width of the specified rectangular region.
     * @param h height of the specified rectangular region.
     * @return a BufferedImage that is the subimage of this BufferedImage.
     */
    private BufferedImage crop(int x, int y, int w, int h) {
        return image.getSubimage(x, y, w, h);
    }

    /**
     * Gets the current frame.
     *
     * @param entity entity.
     * @return the current frame of the entity.
     */
    public BufferedImage getCurrentAnimationFrame(Entity entity) {
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
                    entity.screen.tempScreenY -= entity.sheet.frame.getHeight();
                    i = attackNum == 1 ? 2 : 3;
                }
                case LEFT -> {
                    entity.screen.tempScreenX -= entity.sheet.frame.getWidth();
                    i = attackNum == 1 ? 4 : 5;
                }
                case RIGHT -> i = attackNum == 1 ? 6 : 7;
            }
        }

        return !entity.flags.hitting ? movement[i] : attack[i];
    }

    private int getWidth() {
        return image.getWidth();
    }

    private int getHeight() {
        return image.getHeight();
    }

}
