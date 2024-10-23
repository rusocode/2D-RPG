package com.punkipunk.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class Utils {

    private Utils() {
    }

    public static URL loadAudio(String path) {
        // Primero verifica si el recurso existe usando getResourceAsStream
        try (InputStream is = Utils.class.getResourceAsStream("/" + path)) {
            if (is == null) throw new IllegalArgumentException("Cannot find resource: " + path);
            /* Obtiene la URL si el recurso existe. Al usar Utils.class.getResource() en lugar de
             * Utils.class.getClassLoader().getResource(), estas cambiando el punto de referencia para la busqueda de recursos.
             * Class.getResource() busca relativo a la ubicacion de la clase, mientras que ClassLoader.getResource() busca desde
             * la raiz del classpath. */
            URL url = Utils.class.getResource("/" + path);
            if (url == null) throw new IllegalArgumentException("Failed to get audio file URL: " + path);
            return url;
        } catch (IOException e) {
            throw new RuntimeException("Error al acceder al archivo de audio: " + path, e);
        }
    }

    /**
     * Load the font.
     *
     * @param path path of the asset.
     * @param size font size.
     * @return a new font created with the specified font type.
     */
    public static Font loadFont(String path, double size) {
        try {
            InputStream is = Utils.class.getResourceAsStream("/" + path);
            if (is == null) throw new NullPointerException("Cannot find resource: " + path);
            return Font.loadFont(is, size);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error during font loading");
            alert.setContentText("Failed to load font: " + path + "\n\n" + e);
            alert.showAndWait();
            System.exit(1);
        }
        return null;
    }

    /**
     * Load the texture.
     *
     * @param path path of the asset.
     * @return an Image containing the decoded content of the input.
     */
    public static Image loadTexture(String path) {
        try {
            /* Al añadir "/" al inicio de la ruta, estas indicando explicitamente que la busqueda debe comenzar desde la raiz del
             * classpath, independientemente de donde se encuentre la clase Utils. */
            InputStream is = Utils.class.getResourceAsStream("/" + path);
            if (is == null) throw new NullPointerException("Cannot find resource: " + path);
            return new Image(is);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error during texture loading");
            alert.setContentText("Failed to load texture: " + path + "\n\n" + e);
            alert.showAndWait();
            System.exit(1);
        }
        return null;
    }

    /**
     * Scale the texture before rendering for better performance.
     * <p>
     * It is important that the scale value for the width and height be a multiple of the original size to avoid stretching or
     * deformations in the scaled texture.
     * <p>
     * To maintain the original size of the texture, 1 is specified as the scale value.
     *
     * @param texture texture.
     * @param width   width of the texture.
     * @param height  height of the texture.
     * @return the scaled texture.
     */
    public static Image scaleTexture(Image texture, int width, int height) {
        if (texture == null) return null;

        WritableImage scaledImage = new WritableImage(width, height);
        PixelReader pixelReader = texture.getPixelReader();
        PixelWriter pixelWriter = scaledImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int srcX = (int) (x * texture.getWidth() / width);
                int srcY = (int) (y * texture.getHeight() / height);
                Color color = pixelReader.getColor(srcX, srcY);
                pixelWriter.setColor(x, y, color);
            }
        }

        return scaledImage;
    }

    /**
     * Change the transparency of the image.
     *
     * @param g2    graphic component.
     * @param alpha transparency value.
     */
    public static void changeAlpha(GraphicsContext g2, double alpha) {
        // Asegurarse de que alpha esté en el rango válido
        alpha = Math.max(0.0, Math.min(1.0, alpha));
        // Establecer la opacidad global para todas las operaciones de dibujo subsiguientes
        g2.setGlobalAlpha(alpha);
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
