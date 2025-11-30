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

import java.io.InputStream;

public final class Utils {

    private Utils() {
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
     * Load the texturePath.
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
            alert.setHeaderText("Error during texturePath loading");
            alert.setContentText("Failed to load texturePath: " + path + "\n\n" + e);
            alert.showAndWait();
            System.exit(1);
        }
        return null;
    }

    /**
     * Scale the texturePath before rendering for better performance.
     * <p>
     * It is important that the scale value for the width and height be a multiple of the original size to avoid stretching or
     * deformations in the scaled texturePath.
     * <p>
     * To maintain the original size of the texturePath, 1 is specified as the scale value.
     *
     * @param texture texturePath.
     * @param width   width of the texturePath.
     * @param height  height of the texturePath.
     * @return the scaled texturePath.
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
     */
    public static void changeAlpha(GraphicsContext context, double alpha) {
        // Asegurarse de que alpha este en el rango valido
        alpha = Math.max(0.0, Math.min(1.0, alpha));
        // Establecer la opacidad global para todas las operaciones de dibujo subsiguientes
        context.setGlobalAlpha(alpha);
    }

    /**
     * Devuelve un valor pseudoaleatorio entre un minimo y un maximo especificados, ambos incluidos.
     *
     * @param min valor minimo.
     * @param max valor maximo.
     * @return el valor pseudoaleatorio.
     */
    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Devuelve un valor pseudoaleatorio entre 1 y el maximo especificado, ambos incluidos.
     *
     * @param max valor maximo.
     * @return el valor pseudoaleatorio.
     */
    public static int random(int max) {
        return (int) (Math.random() * max) + 1;
    }

}
