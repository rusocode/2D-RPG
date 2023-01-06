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

	public BufferedImage movementDown1, movementDown2, movementUp1, movementUp2, movementLeft1, movementLeft2, movementRight1, movementRight2;
	public BufferedImage attackDown1, attackDown2, attackUp1, attackUp2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage heart_full, heart_half, heart_blank;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collision;
	public String[] dialogues = new String[20];

	// States
	public int worldX, worldY;
	public String direction = "down";
	public int movementNum = 1, attackNum = 1;
	public int dialogueIndex;
	public boolean collisionOn; // Estado que depende de las colisiones con tiles, objetos y entidades
	public boolean invincible;
	public boolean attacking;

	// Counters (TODO Se podria crear una clase Timer para las animaciones)
	public int movementCounter;
	public int attackCounter;
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

		movementCounter++;
		if (movementCounter > 10 - speed) {
			if (movementNum == 1) movementNum = 2;
			else if (movementNum == 2) movementNum = 1;
			movementCounter = 0;
		}

		if (invincible) {
			invincibleCounter++;
			if (invincibleCounter > 40) {
				invincible = false;
				invincibleCounter = 0;
			}
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
					image = movementNum == 1 || collisionOn ? movementDown1 : movementDown2;
					break;
				case "up":
					image = movementNum == 1 || collisionOn ? movementUp1 : movementUp2;
					break;
				case "left":
					image = movementNum == 1 || collisionOn ? movementLeft1 : movementLeft2;
					break;
				case "right":
					image = movementNum == 1 || collisionOn ? movementRight1 : movementRight2;
					break;
			}

			if (invincible) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g2.drawImage(image, screenX, screenY, null);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

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
	public void initMovementImages(SpriteSheet image, int width, int height) {
		BufferedImage[] subimages = SpriteSheet.getMovementSubimages(image, width, height);
		if (subimages.length > 2) { // Npcs
			movementDown1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementDown2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			movementUp1 = Utils.scaleImage(subimages[2], game.tileSize, game.tileSize);
			movementUp2 = Utils.scaleImage(subimages[3], game.tileSize, game.tileSize);
			movementLeft1 = Utils.scaleImage(subimages[4], game.tileSize, game.tileSize);
			movementLeft2 = Utils.scaleImage(subimages[5], game.tileSize, game.tileSize);
			movementRight1 = Utils.scaleImage(subimages[6], game.tileSize, game.tileSize);
			movementRight2 = Utils.scaleImage(subimages[7], game.tileSize, game.tileSize);
		} else { // Mobs
			movementDown1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementDown2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			movementUp1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementUp2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			movementLeft1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementLeft2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			movementRight1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementRight2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
		}
	}

	public void initAttackImages(SpriteSheet image, int width, int height) {
		BufferedImage[] subimages = SpriteSheet.getAttackSubimages(image, width, height);
		attackDown1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize * 2);
		attackDown2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize * 2);
		attackUp1 = Utils.scaleImage(subimages[2], game.tileSize, game.tileSize * 2);
		attackUp2 = Utils.scaleImage(subimages[3], game.tileSize, game.tileSize * 2);
		attackLeft1 = Utils.scaleImage(subimages[4], game.tileSize * 2, game.tileSize);
		attackLeft2 = Utils.scaleImage(subimages[5], game.tileSize * 2, game.tileSize);
		attackRight1 = Utils.scaleImage(subimages[6], game.tileSize * 2, game.tileSize);
		attackRight2 = Utils.scaleImage(subimages[7], game.tileSize * 2, game.tileSize);
	}

}
