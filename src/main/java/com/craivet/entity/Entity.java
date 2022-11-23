package com.craivet.entity;

import java.awt.image.BufferedImage;

/**
 * Almacena variables que se utilizaran en las clases de player, monster y NPC.
 */

public abstract class Entity {

	// TODO Se podria crear una matriz para todas las imagenes
	public BufferedImage down1, down2, up1, up2, left1, left2, right1, right2;
	public String direction;

	public int x, y;
	public int speed;

	public int spriteCounter;
	public int spriteNum = 1;

}
