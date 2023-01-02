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

	/**
	 * Obtiene las subimagenes del sprite sheet.
	 *
	 * <p>TODO Incluir funcion para anchos y altos de subimagenes diferentes (por ejemplo, si el parametro es true uso switch)
	 *
	 * @param image  el sprite sheet.
	 * @param width  el ancho de la subimagen.
	 * @param height el alto de la subimagen.
	 * @return una matriz con las subimagenes del sprite sheet.
	 */
	public static BufferedImage[] getSubimages(SpriteSheet image, int width, int height) {
		int col = image.getWidth() / width;
		int row = image.getHeight() / height;
		BufferedImage[] subimages = new BufferedImage[col * row];
		int i = 0;
		for (int y = 0; y < row; y++)
			for (int x = 0; x < col; x++)
				subimages[i++] = image.crop(x * width, y * height, width, height);
		return subimages;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}


}
