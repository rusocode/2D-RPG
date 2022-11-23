package com.craivet.gfx;

import com.craivet.utils.Utils;

import java.awt.image.BufferedImage;

public class Assets {

	public static SpriteSheet player = new SpriteSheet(Utils.loadImage("textures/player.png"));
	public static BufferedImage[] playerDown, playerUp, playerLeft, playerRight;

	private Assets() {
	}

	/**
	 * Inicializa los sprites del SpriteSheet.
	 */
	public static void init() {
		// initTexturePlayer()
	}

	/**
	 * Reeplaza las coordenadas manuales:
	 * <pre>{@code
	 * playerDown[0] = player.crop(0, 0, 16, 16);
	 * playerDown[1] = player.crop(16, 0, 16, 16);
	 * playerUp[0] = player.crop(0, 16, 16, 16);
	 * playerUp[1] = player.crop(16, 16, 16, 16);
	 * playerLeft[0] = player.crop(0, 32, 16, 16);
	 * playerLeft[1] = player.crop(16, 32, 16, 16);
	 * playerRight[0] = player.crop(0, 48, 16, 16);
	 * playerRight[1] = player.crop(16, 48, 16, 16);
	 * }</pre>
	 */
	public static void initTexturePlayer() {
		playerDown = new BufferedImage[2];
		playerUp = new BufferedImage[2];
		playerLeft = new BufferedImage[2];
		playerRight = new BufferedImage[2];

		int playerWidth = 16;
		int playerHeight = 16;

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 2; x++) {
				switch (y) { // Controla las filas
					case 0:
						playerDown[x] = player.crop(x * playerWidth, 0, playerWidth, playerHeight);
						break;
					case 1:
						playerUp[x] = player.crop(x * playerWidth, y * playerHeight, playerWidth, playerHeight);
						break;
					case 2:
						playerLeft[x] = player.crop(x * playerWidth, y * playerHeight, playerWidth, playerHeight);
						break;
					case 3:
						playerRight[x] = player.crop(x * playerWidth, y * playerHeight, playerWidth, playerHeight);
						break;
				}
			}
		}
	}

}
