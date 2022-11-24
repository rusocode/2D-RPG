package com.craivet.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Utils {

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

	private Utils() {
	}

}
