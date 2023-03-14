package com.craivet.utils;

import com.craivet.OggPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class Utils {

	/**
	 * Carga la fuente utilizando la ruta especificada.
	 *
	 * @param path la ruta del recurso.
	 * @param size el tamaño de la fuente.
	 * @return una nueva fuente creada con el tipo de fuente especificado.
	 */
	public static Font loadFont(String path, int size) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path))).deriveFont(Font.PLAIN, size);
		} catch (IOException | FontFormatException | NullPointerException e) {
			JOptionPane.showMessageDialog(null, "Error durante la carga de fuente " + path + "\n" + Arrays.toString(e.getStackTrace()).replace(" ", "\n"), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return null;
	}

	// TODO getResourceAsStream()?
	public static URL loadSound(String path) {
		return Utils.class.getClassLoader().getResource(path);
	}

	public static String loadSound2(String path) {
		String p = Objects.requireNonNull(OggPlayer.class.getClassLoader().getResource(path)).getPath();
		String formatPath = p.replace("%20", " ");
		return formatPath.replaceAll("/", "\\\\");
	}

	/**
	 * Carga la imagen utilizando la ruta especificada.
	 *
	 * @param path la ruta del recurso.
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
	 *
	 * @param image  la imagen.
	 * @param width  el ancho de la imagen.
	 * @param height el alto de la imagen.
	 * @return la imagen escalada.
	 */
	public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
		BufferedImage scaledImage = new BufferedImage(width, height, image.getType());
		Graphics2D g2 = scaledImage.createGraphics(); // Crea un Graphics2D, que se puede usar para dibujar en este BufferedImage
		g2.drawImage(image, 0, 0, width, height, null);
		g2.dispose();
		return scaledImage;
	}

	/**
	 * Establece la transparencia a la imagen.
	 *
	 * @param g2         componente grafico.
	 * @param alphaValue valor de transparencia.
	 */
	public static void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}

	/**
	 * Devuelve un valor pseudoaleatorio entre un minimo y maximo especificado.
	 *
	 * @param min el valor minimo.
	 * @param max el valor maximo.
	 * @return el valor pseudoaleatorio.
	 */
	public static int azar(int min, int max) {
		return (int) ((Math.random() * (max - min + 1)) + min);
	}

	/**
	 * Devuelve un valor pseudoaleatorio entre 1 y el maximo especificado.
	 *
	 * @param max el valor maximo.
	 * @return el valor pseudoaleatorio.
	 */
	public static int azar(int max) {
		return (int) (Math.random() * max + 1);
	}

	private Utils() {
	}

}
