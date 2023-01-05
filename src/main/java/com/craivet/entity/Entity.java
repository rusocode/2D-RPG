package com.craivet.entity;

import com.craivet.Game;
import com.craivet.gfx.SpriteSheet;
import com.craivet.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clase encargada de verificar las colisiones con npcs, mobs y el player.
 *
 * <p>TODO Se podria separar los npcs y mobs por package, y por clases abstractas.
 */

public abstract class Entity {

	Game game;

	public BufferedImage down1, down2, up1, up2, left1, left2, right1, right2;
	public BufferedImage attackDown1, attackDown2, attackUp1, attackUp2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage image1, image2, image3;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collision;
	public String[] dialogues = new String[20];

	// State
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	public int dialogueIndex;
	public boolean collisionOn; // Estado que depende de las colisiones con tiles, objetos y entidades
	public boolean invincible;
	public boolean attacking;

	// Counter
	public int spriteCounter;
	public int actionLockCounter;
	public int invincibleCounter;

	// Character atributes
	// Para diferenciar entre npcs y mobs, ya que los npcs no atacan
	public int type; // 0 = player, 1 = npc, 2 = mob
	public String name;
	public int speed;
	public int maxLife;
	public int life; // 2 de vida representa 1 corazon lleno (la imagen de heart_full)

	public Entity(Game game) {
		this.game = game;
	}

	public void setAction() {

	}

	public void speak() {
		if (dialogues[dialogueIndex] == null) dialogueIndex = 0;
		game.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;

		switch (game.player.direction) {
			case "down":
				direction = "up";
				break;
			case "up":
				direction = "down";
				break;
			case "left":
				direction = "right";
				break;
			case "right":
				direction = "left";
				break;
		}
	}

	public void update() {

		setAction();

		collisionOn = false;

		game.cChecker.checkTile(this);
		game.cChecker.checkObject(this);

		// Verifica la colision con npcs y mobs
		game.cChecker.checkEntity(this, game.npcs);
		game.cChecker.checkEntity(this, game.mobs);

		// Verifica la colision con el player
		boolean contactPlayer = game.cChecker.checkPlayer(this);

		// Si fue un mob el que colisiono con el player
		if (this.type == 2 && contactPlayer) {
			if (!game.player.invincible) {
				game.player.life--;
				game.player.invincible = true;
			}
		}

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
		BufferedImage image = null;
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
			g2.drawImage(image, screenX, screenY, null);
			// g2.setColor(Color.yellow);
			// g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
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
		BufferedImage[] subimages = SpriteSheet.getSubimages(image, width, height);
		if (subimages.length > 2) { // Npcs
			down1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			down2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			up1 = Utils.scaleImage(subimages[2], game.tileSize, game.tileSize);
			up2 = Utils.scaleImage(subimages[3], game.tileSize, game.tileSize);
			left1 = Utils.scaleImage(subimages[4], game.tileSize, game.tileSize);
			left2 = Utils.scaleImage(subimages[5], game.tileSize, game.tileSize);
			right1 = Utils.scaleImage(subimages[6], game.tileSize, game.tileSize);
			right2 = Utils.scaleImage(subimages[7], game.tileSize, game.tileSize);
		} else { // Mobs
			down1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			down2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			up1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			up2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			left1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			left2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			right1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			right2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
		}
	}

	public void initImagesAttack(SpriteSheet image, int width, int height) {
		BufferedImage[] subimages = SpriteSheet.getSubimages(image, width, height);
		attackDown1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
		attackDown2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
		attackUp1 = Utils.scaleImage(subimages[2], game.tileSize, game.tileSize);
		attackUp2 = Utils.scaleImage(subimages[3], game.tileSize, game.tileSize);
		attackLeft1 = Utils.scaleImage(subimages[4], game.tileSize, game.tileSize);
		attackLeft2 = Utils.scaleImage(subimages[5], game.tileSize, game.tileSize);
		attackRight1 = Utils.scaleImage(subimages[6], game.tileSize, game.tileSize);
		attackRight2 = Utils.scaleImage(subimages[7], game.tileSize, game.tileSize);
	}

}
