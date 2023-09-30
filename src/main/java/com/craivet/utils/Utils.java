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
     * Carga la fuente utilizando la ruta especificada.
     *
     * @param path ruta del recurso.
     * @param size tamaño de la fuente.
     * @return una nueva fuente creada con el tipo de fuente especificado.
     */
    public static Font loadFont(String path, float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path))).deriveFont(size);
        } catch (IOException | FontFormatException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Error durante la carga de fuente " + path + "\n" + Arrays.toString(e.getStackTrace()).replace(" ", "\n"), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return null;
    }

    public static URL loadAudio(String path) {
        return Objects.requireNonNull(Utils.class.getClassLoader().getResource(path));
    }

    /**
     * Carga la imagen utilizando la ruta especificada.
     *
     * @param path ruta del recurso.
     * @return una BufferedImage que contiene el contenido decodificado de la entrada.
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path)));
        } catch (IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Error durante la carga de imagen " + path + "\n" + Arrays.toString(e.getStackTrace()).replace(" ", "\n"), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return null;
    }

    /**
     * Escala la imagen antes de renderizarla para un mejor rendimiento.
     * <p>
     * Es importante que el valor de escala para el ancho y alto sea multiplo del tamaño original para evitar
     * estiramientos o deformaciones en la imagen escalada.
     * <p>
     * Para mantener el tamaño original de la imagen, se especifica 1 como valor de escala.
     *
     * @param image  imagen.
     * @param width  ancho de la imagen.
     * @param height alto de la imagen.
     * @return la imagen escalada.
     */
    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledImage.createGraphics(); // Crea un Graphics2D, que se puede usar para dibujar en este BufferedImage
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    /**
     * Cambia la transparencia de la imagen.
     *
     * @param g2    componente grafico.
     * @param alpha valor de transparencia.
     */
    public static void changeAlpha(Graphics2D g2, float alpha) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    /**
     * Devuelve un valor pseudoaleatorio entre un minimo y maximo especificado, ambos incluidos.
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
