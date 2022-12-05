package com.craivet.gfx;

import java.awt.image.BufferedImage;

import com.craivet.utils.Utils;

public class Assets {

	// Entities
	public static SpriteSheet player = new SpriteSheet(Utils.loadImage("textures/entity/player.png"));

	// Tiles
	public static BufferedImage earth = Utils.loadImage("textures/tiles/earth.png");
	public static BufferedImage floor01 = Utils.loadImage("textures/tiles/floor01.png");
	public static BufferedImage grass00 = Utils.loadImage("textures/tiles/grass00.png");
	public static BufferedImage grass01 = Utils.loadImage("textures/tiles/grass01.png");
	public static BufferedImage hut = Utils.loadImage("textures/tiles/hut.png");
	public static BufferedImage road00 = Utils.loadImage("textures/tiles/road00.png");
	public static BufferedImage road01 = Utils.loadImage("textures/tiles/road01.png");
	public static BufferedImage road02 = Utils.loadImage("textures/tiles/road02.png");
	public static BufferedImage road03 = Utils.loadImage("textures/tiles/road03.png");
	public static BufferedImage road04 = Utils.loadImage("textures/tiles/road04.png");
	public static BufferedImage road05 = Utils.loadImage("textures/tiles/road05.png");
	public static BufferedImage road06 = Utils.loadImage("textures/tiles/road06.png");
	public static BufferedImage road07 = Utils.loadImage("textures/tiles/road07.png");
	public static BufferedImage road08 = Utils.loadImage("textures/tiles/road08.png");
	public static BufferedImage road09 = Utils.loadImage("textures/tiles/road09.png");
	public static BufferedImage road10 = Utils.loadImage("textures/tiles/road10.png");
	public static BufferedImage road11 = Utils.loadImage("textures/tiles/road11.png");
	public static BufferedImage road12 = Utils.loadImage("textures/tiles/road12.png");
	public static BufferedImage table01 = Utils.loadImage("textures/tiles/table01.png");
	public static BufferedImage tree = Utils.loadImage("textures/tiles/tree.png");
	public static BufferedImage wall = Utils.loadImage("textures/tiles/wall.png");
	public static BufferedImage water00 = Utils.loadImage("textures/tiles/water00.png");
	public static BufferedImage water01 = Utils.loadImage("textures/tiles/water01.png");
	public static BufferedImage water02 = Utils.loadImage("textures/tiles/water02.png");
	public static BufferedImage water03 = Utils.loadImage("textures/tiles/water03.png");
	public static BufferedImage water04 = Utils.loadImage("textures/tiles/water04.png");
	public static BufferedImage water05 = Utils.loadImage("textures/tiles/water05.png");
	public static BufferedImage water06 = Utils.loadImage("textures/tiles/water06.png");
	public static BufferedImage water07 = Utils.loadImage("textures/tiles/water07.png");
	public static BufferedImage water08 = Utils.loadImage("textures/tiles/water08.png");
	public static BufferedImage water09 = Utils.loadImage("textures/tiles/water09.png");
	public static BufferedImage water10 = Utils.loadImage("textures/tiles/water10.png");
	public static BufferedImage water11 = Utils.loadImage("textures/tiles/water11.png");
	public static BufferedImage water12 = Utils.loadImage("textures/tiles/water12.png");
	public static BufferedImage water13 = Utils.loadImage("textures/tiles/water13.png");

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
