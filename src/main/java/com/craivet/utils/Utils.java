package com.craivet.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Utils {

	public static Font loadFont(String path, int size) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path))).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	// TODO getResourceAsStream()?
	public static URL loadSound(String path) {
		return Utils.class.getClassLoader().getResource(path);
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
		} catch (IOException e) {
			e.printStackTrace();
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

	private Utils() {
	}

}
