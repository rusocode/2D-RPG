package com.craivet.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public final class Utils {

    private Utils() {
    }

    /**
     * Load the font.
     *
     * @param path path of the resource.
     * @param size font size.
     * @return a new font created with the specified font type.
     */
    public static Font loadFont(String path, float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path))).deriveFont(size);
        } catch (IOException | FontFormatException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Error during font loading " + path + "\n" + Arrays.toString(e.getStackTrace()).replace(" ", "\n"), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return null;
    }

    public static URL loadAudio(String path) {
        return Objects.requireNonNull(Utils.class.getClassLoader().getResource(path));
    }

    /**
     * Load the image.
     *
     * @param path path of the resource.
     * @return a BufferedImage containing the decoded content of the input.
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path)));
        } catch (IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Error during image loading " + path + "\n" + Arrays.toString(e.getStackTrace()).replace(" ", "\n"), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return null;
    }

    /**
     * Scale the image before rendering for better performance.
     * <p>
     * It is important that the scale value for the width and height be a multiple of the original size to avoid
     * stretching or deformations in the scaled image.
     * <p>
     * To maintain the original size of the image, 1 is specified as the scale value.
     *
     * @param image  image.
     * @param width  width of the image.
     * @param height height of the image.
     * @return the scaled image.
     */
    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledImage.createGraphics(); // Creates a Graphics2D, which can be used to draw on this BufferedImage
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    /**
     * Change the transparency of the image.
     *
     * @param g2    graphic component.
     * @param alpha transparency value.
     */
    public static void changeAlpha(Graphics2D g2, float alpha) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    /**
     * Returns a pseudorandom value between a specified minimum and maximum, both included.
     *
     * @param min minimum value.
     * @param max maximum value.
     * @return the pseudorandom value.
     */
    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Returns a pseudorandom value between 1 and the specified maximum, both included.
     *
     * @param max maximum value.
     * @return the pseudorandom value.
     */
    public static int random(int max) {
        return (int) (Math.random() * max) + 1;
    }

}
