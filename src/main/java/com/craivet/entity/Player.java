package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.input.KeyHandler;

import static com.craivet.utils.Constants.*;

public class Player extends Entity {

	KeyHandler key;

	// El player permanece fijo en el centro de la pantalla
	public final int screenX;
	public final int screenY;

	public Player(Game game, KeyHandler key) {
		super(game);

		this.key = key;

		// Posiciona al player en el centro de la pantalla
		screenX = game.screenWidth / 2 - (game.tileSize / 2);
		screenY = game.screenHeight / 2 - (game.tileSize / 2);

		// TODO Reemplazar numeros magicos
		solidArea = new Rectangle(); // TODO Hace falta crear este objeto?
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;

		setDefaultValues();
		initImages(Assets.player, PLAYER_WIDTH, PLAYER_HEIGHT);
	}

	public void update() {

		if (key.s || key.w || key.a || key.d) { // Evita que el player se mueva cuando no se presiono ninguna tecla

			// Obtiene la direccion dependiendo de la tecla seleccionada
			if (key.s) direction = "down";
			else if (key.w) direction = "up";
			else if (key.a) direction = "left";
			else direction = "right";

			collisionOn = false;

			// Verifica las colisiones
			game.cChecker.checkTile(this);
			pickUpObject(game.cChecker.checkObject(this));
			interactNPC(game.cChecker.checkEntity(this, game.npcs));
			game.eHandler.checkEvent();

			game.keyHandler.enter = false;

			// Si no hay colision, el player se puede mover dependiendo de la direccion
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

			// Funciona como Timer para las animaciones
			spriteCounter++;
			if (spriteCounter > 10 - speed) {
				if (spriteNum == 1) spriteNum = 2;
				else if (spriteNum == 2) spriteNum = 1;
				spriteCounter = 0;
			}

		} else spriteNum = 1; // Vuelve al sprite inicial (movimiento natural)

	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
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
		// g2.setColor(Color.red);
		// g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
	}

	private void setDefaultValues() {
		// Centro del mundo
		worldX = game.tileSize * 23;
		worldY = game.tileSize * 21;

		speed = PLAYER_SPEED;

		direction = "down";

		// Player status
		maxLife = 6;
		life = maxLife; // 1 = heart_half, 2 = heart_full
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
		if (npcIndex != -1) {
			if (game.keyHandler.enter) {
				game.gameState = game.dialogueState;
				game.npcs[npcIndex].speak();
			}
		}
	}

}
