package com.craivet.gfx;

import java.awt.image.BufferedImage;

import com.craivet.utils.Utils;

public class Assets {

	// Entities
	public static SpriteSheet player = new SpriteSheet(Utils.loadImage("textures/entity/player.png"));

	// Tiles
	public static BufferedImage earth = Utils.loadImage("textures/tiles/earth.png");
	public static BufferedImage grass = Utils.loadImage("textures/tiles/grass.png");
	public static BufferedImage sand = Utils.loadImage("textures/tiles/sand.png");
	public static BufferedImage tree = Utils.loadImage("textures/tiles/tree.png");
	public static BufferedImage wall = Utils.loadImage("textures/tiles/wall.png");
	public static BufferedImage water = Utils.loadImage("textures/tiles/water.png");

	// Objects
	public static BufferedImage boots = Utils.loadImage("textures/objs/boots.png");
	public static BufferedImage chest = Utils.loadImage("textures/objs/chest.png");
	public static BufferedImage door = Utils.loadImage("textures/objs/door.png");
	public static BufferedImage key = Utils.loadImage("textures/objs/key.png");

	/**
	 * Obtiene las subimagenes del sprite sheet.
	 *
	 * @param ss     el sprite sheet.
	 * @param width  el ancho de la subimagen.
	 * @param height el alto de la subimagen.
	 * @return una matriz con las subimagenes del sprite sheet.
	 *
	 * <p>TODO Incluir funcion para anchos y altos de subimagenes diferentes (por ejemplo, si el parametro es true uso switch)
	 */
	public static BufferedImage[] getSubimages(SpriteSheet ss, int width, int height) {
		int col = ss.getWidth() / width;
		int row = ss.getHeight() / height;
		BufferedImage[] subimages = new BufferedImage[col * row];
		int i = 0;
		for (int y = 0; y < row; y++)
			for (int x = 0; x < col; x++)
				subimages[i++] = ss.crop(x * width, y * height, width, height);
		return subimages;
	}

	private Assets() {
	}

}
