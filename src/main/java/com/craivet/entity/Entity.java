package com.craivet.entity;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.gfx.SpriteSheet;
import com.craivet.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Constants.PLAYER_HEIGHT;
import static com.craivet.utils.Constants.PLAYER_WIDTH;

/**
 * Almacena variables que se utilizaran en las clases de player, monster y NPC.
 *
 * <p>Las coordenadas worldX y screenX no son lo mismo.
 */

public abstract class Entity {

	Game game;

	// TODO Esta bien declarlas los frames en esta clase, pero si un sprite tiene mas frames?
	public BufferedImage down1, down2, up1, up2, left1, left2, right1, right2;
	public String direction;

	public int worldX, worldY;
	public int speed;

	public int spriteCounter;
	public int spriteNum = 1;

	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn;

	public int actionLockCounter;

	public Entity(Game game) {
		this.game = game;
	}

	public void setAction() {

	}

	public void update() {

		setAction();

		collisionOn = false;

		game.cChecker.checkTile(this);
		game.cChecker.checkObject(this, false);
		game.cChecker.checkPlayer(this);

		// Si no hay colision, la entidad se puede mover dependiendo de la direccion
		if (!collisionOn) {
			switch (direction) {
				case "down":
					worldY += speed;
					break;
				case "up":
					worldY -= speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
			}
		}

		spriteCounter++;
		if (spriteCounter > 10 - speed) {
			if (spriteNum == 1) spriteNum = 2;
			else if (spriteNum == 2) spriteNum = 1;
			spriteCounter = 0;
		}
	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null; // TODO Local?
		int screenX = (worldX - game.player.worldX) + game.player.screenX;
		int screenY = (worldY - game.player.worldY) + game.player.screenY;
		if (worldX + game.tileSize > game.player.worldX - game.player.screenX &&
				worldX - game.tileSize < game.player.worldX + game.player.screenX &&
				worldY + game.tileSize > game.player.worldY - game.player.screenY &&
				worldY - game.tileSize < game.player.worldY + game.player.screenY) {
			switch (direction) {
				case "down":
					image = spriteNum == 1 || collisionOn ? down1 : down2;
					break;
				case "up":
					image = spriteNum == 1 || collisionOn ? up1 : up2;
					break;
				case "left":
					image = spriteNum == 1 || collisionOn ? left1 : left2;
					break;
				case "right":
					image = spriteNum == 1 || collisionOn ? right1 : right2;
					break;
			}
			g2.drawImage(image, screenX, screenY, game.tileSize, game.tileSize, null);
		}
	}

	/**
	 * Inicializa las subimagenes del sprite sheet y escala cada una.
	 *
	 * @param image  el sprite sheet.
	 * @param width  el ancho de la subimagen.
	 * @param height el alto de la subimagen.
	 */
	public void initImages(SpriteSheet image, int width, int height) {
		BufferedImage[] subimages = Assets.getSubimages(image, width, height);
		down1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
		down2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
		up1 = Utils.scaleImage(subimages[2], game.tileSize, game.tileSize);
		up2 = Utils.scaleImage(subimages[3], game.tileSize, game.tileSize);
		left1 = Utils.scaleImage(subimages[4], game.tileSize, game.tileSize);
		left2 = Utils.scaleImage(subimages[5], game.tileSize, game.tileSize);
		right1 = Utils.scaleImage(subimages[6], game.tileSize, game.tileSize);
		right2 = Utils.scaleImage(subimages[7], game.tileSize, game.tileSize);
	}

}
