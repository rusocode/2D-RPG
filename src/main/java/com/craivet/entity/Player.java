package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.input.KeyHandler;
import com.craivet.gfx.Assets;

import static com.craivet.utils.Constants.*;

public class Player extends Entity {

	Game game;
	KeyHandler key;

	// El player permanece fijo en el centro de la pantalla
	public final int screenX;
	public final int screenY;

	public int hasKey;

	public Player(Game game, KeyHandler key) {
		this.game = game;
		this.key = key;

		// Posiciona al player en el centro de la pantalla
		screenX = game.screenWidth / 2 - (game.tileSize / 2);
		screenY = game.screenHeight / 2 - (game.tileSize / 2);

		// TODO Reemplazar numeros magicos
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;

		setDefaultValues();
		getPlayerImage();
	}

	public void setDefaultValues() {
		// Posiciona al player en el centro del mundo
		worldX = game.tileSize * 23;
		worldY = game.tileSize * 21;
		speed = 3;
		direction = "down"; // Direccion por defecto
	}

	public void getPlayerImage() {
		BufferedImage[] subimages = Assets.getSubimages(Assets.player, PLAYER_WIDTH, PLAYER_HEIGHT);
		down1 = subimages[0];
		down2 = subimages[1];
		up1 = subimages[2];
		up2 = subimages[3];
		left1 = subimages[4];
		left2 = subimages[5];
		right1 = subimages[6];
		right2 = subimages[7];
	}

	public void pickUpObject(int i) {
		if (i != 999) { // El 999 significa que no agarro ningun objeto
			String name = game.objs[i].name;
			switch (name) {
				case "Key":
					game.playSE(1);
					hasKey++;
					game.objs[i] = null;
					game.ui.showMessage("You got a key!");
					break;
				case "Door":
					if (hasKey > 0) {
						game.playSE(3);
						game.objs[i] = null;
						hasKey--;
						game.ui.showMessage("You opened the door!");
					} else game.ui.showMessage("You need a key!");
					break;
				case "Boots":
					game.playSE(2);
					speed += 2;
					game.objs[i] = null;
					game.ui.showMessage("Speed up!");
					break;
				case "Chest":
					game.ui.gameFinished = true;
					// game.stopMusic();
					game.playSE(4);
					break;
			}
		}
	}

	public void update() {
		if (key.s || key.w || key.a || key.d) { // Evita que el player se mueva cuando no se presiono ninguna tecla

			// Obtiene la direccion dependiendo de la tecla seleccionada
			if (key.s) direction = "down";
			else if (key.w) direction = "up";
			else if (key.a) direction = "left";
			else direction = "right";

			// Verifica la colision con el tile
			collisionOn = false;
			game.cChecker.checkTile(this);

			// Verifica la colision con el objeto
			int indexObj = game.cChecker.checkObject(this, true);
			pickUpObject(indexObj);

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
				// TODO if (spriteNum %2 == 0)
				if (spriteNum == 1) image = down1;
				if (spriteNum == 2) image = down2;
				break;
			case "up":
				if (spriteNum == 1) image = up1;
				if (spriteNum == 2) image = up2;
				break;
			case "left":
				if (spriteNum == 1) image = left1;
				if (spriteNum == 2) image = left2;
				break;
			case "right":
				if (spriteNum == 1) image = right1;
				if (spriteNum == 2) image = right2;
				break;
		}
		g2.drawImage(image, screenX, screenY, game.tileSize, game.tileSize, null);
		// g2.setColor(Color.red);
		// g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
	}

}
