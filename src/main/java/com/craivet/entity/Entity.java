package com.craivet.entity;

import java.awt.image.BufferedImage;

/**
 * Almacena variables que se utilizaran en las clases de player, monster y NPC.
 *
 * <p>Las coordenadas worldX y screenX son diferentes.
 */

public abstract class Entity {

	public BufferedImage down1, down2, up1, up2, left1, left2, right1, right2;
	public String direction;

	public int worldX, worldY;
	public int speed;

	public int spriteCounter;
	public int spriteNum = 1;

}
