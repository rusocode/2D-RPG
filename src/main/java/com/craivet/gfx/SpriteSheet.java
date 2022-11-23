package com.craivet.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private final BufferedImage image;

	public SpriteSheet(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Devuelve una subimagen definida por una region rectangular especificada. La BufferedImage devuelta comparte la
	 * misma matriz de datos que la imagen original.
	 *
	 * @param x      la coordenada X de la esquina superior izquierda de la region rectangular especificada.
	 * @param y      la coordenada Y de la esquina superior izquierda de la region rectangular especificada.
	 * @param width  el ancho de la region rectangular especificada.
	 * @param height la altura de la region rectangular especificada.
	 * @return una BufferedImage que es la subimagen de esta BufferedImage.
	 */
	public BufferedImage crop(int x, int y, int width, int height) {
		return image.getSubimage(x, y, width, height);
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}


}
