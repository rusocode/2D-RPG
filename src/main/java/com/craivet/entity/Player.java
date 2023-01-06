package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.input.KeyHandler;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class Player extends Entity {

	private final KeyHandler key;

	// El player permanece fijo en el centro de la pantalla
	public final int screenX;
	public final int screenY;

	public Player(Game game, KeyHandler key) {
		super(game);

		this.key = key;

		// Posiciona al player en el centro de la pantalla
		screenX = game.screenWidth / 2 - (game.tileSize / 2);
		screenY = game.screenHeight / 2 - (game.tileSize / 2);

		setDefaultValues();

	}

	private void setDefaultValues() {

		// Posicion en el mundo
		worldX = game.tileSize * 23;
		worldY = game.tileSize * 21;
		speed = PLAYER_SPEED;
		maxLife = 6;
		life = maxLife; // 1 = heart_half, 2 = heart_full
		direction = "down";

		solidArea.x = 8;
		solidArea.y = 16;
		solidArea.width = 32;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		initImages(Assets.player, ENTITY_WIDTH, ENTITY_HEIGHT);
		attackDown1 = Utils.scaleImage(Assets.boy_attack_down_1, game.tileSize, game.tileSize * 2);
		attackDown2 = Utils.scaleImage(Assets.boy_attack_down_2, game.tileSize, game.tileSize * 2);
	}

	public void update() {

		if (attacking) attacking();

		if (key.s || key.w || key.a || key.d || key.enter) { // Evita que el player se mueva cuando no se presiono ninguna tecla

			// Obtiene la direccion dependiendo de la tecla seleccionada
			if (key.s) direction = "down";
			else if (key.w) direction = "up";
			else if (key.a) direction = "left";
			else if (key.d) direction = "right";

			// Verifica las colisiones
			collisionOn = false;
			game.cChecker.checkTile(this);
			pickUpObject(game.cChecker.checkObject(this));
			interactNPC(game.cChecker.checkEntity(this, game.npcs));
			contactMOB(game.cChecker.checkEntity(this, game.mobs));
			game.eHandler.checkEvent();

			// Si no hay colision y si no presiono la tecla enter, el player se puede mover dependiendo de la direccion
			if (!collisionOn && !key.enter) {
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

			game.keyHandler.enter = false; // TODO No tendria que hacerlo desde el KeyHandler?

			// Funciona como Timer para las animaciones
			spriteCounter++;
			if (spriteCounter > 10 - speed) {
				if (spriteNum == 1) spriteNum = 2;
				else if (spriteNum == 2) spriteNum = 1;
				spriteCounter = 0;
			}

		} else {
			standCounter++;
			if (standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
			// spriteNum = 1; // Vuelve al sprite inicial (movimiento natural)
		}

		// Esto debe estar fuera de la declaracion if
		if (invincible) {
			invincibleCounter++; // EL PROBLEMA VIENE DEL spriteCounter
			if (invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}

	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		switch (direction) {
			case "down":
				if (!attacking) {
					if (spriteNum == 1) image = down1;
					if (spriteNum == 2) image = down2;
					// image = spriteNum == 1 || collisionOn ? down1 : down2;
				}
				if (attacking) {
					if (spriteNum == 1) image = attackDown1;
					if (spriteNum == 2) image = attackDown2;
					// image = spriteNum == 1 ? attackDown1 : attackDown2;
				}
				break;
			case "up":
				if (!attacking) image = spriteNum == 1 || collisionOn ? up1 : up2;
				// if (attacking) image = spriteNum == 1 ? attackUp1 : attackUp2;
				break;
			case "left":
				if (!attacking) image = spriteNum == 1 || collisionOn ? left1 : left2;
				// if (attacking) image = spriteNum == 1 ? attackLeft1 : attackLeft2;
				break;
			case "right":
				if (!attacking) image = spriteNum == 1 || collisionOn ? right1 : right2;
				// if (attacking) image = spriteNum == 1 ? attackRight1 : attackRight2;
				break;
		}

		if (invincible) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		g2.drawImage(image, screenX, screenY, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Reset alpha
		// g2.setColor(Color.red);
		// g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

	}

	public void attacking() {
		spriteCounter++;
		if (spriteCounter <= 5) spriteNum = 1; // (0-5 frame)
		if (spriteCounter > 5 && spriteCounter <= 25) spriteNum = 2; // (6-25 frame)
		if (spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}

	/**
	 * Recoge el objeto si el indice de este es distinto a -1.
	 *
	 * @param objIndex indice del objeto.
	 */
	private void pickUpObject(int objIndex) {
		if (objIndex != -1) {
			System.out.println("Object picked!");
		}
	}

	/**
	 * Interactua con el npc si el indice de este es distinto a -1.
	 *
	 * @param npcIndex indice del npc.
	 */
	private void interactNPC(int npcIndex) {
		if (game.keyHandler.enter) {
			if (npcIndex != -1) {
				game.gameState = game.dialogueState;
				game.npcs[npcIndex].speak();
			} else attacking = true;
		}
	}

	private void contactMOB(int mobIndex) {
		if (mobIndex != -1) {
			if (!invincible) {
				life--;
				invincible = true;
			}
		}
	}

}
