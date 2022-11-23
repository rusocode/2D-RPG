package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.KeyHandler;
import com.craivet.gfx.Assets;

import static com.craivet.utils.Constants.*;

public class Player extends Entity {

	Game game;
	KeyHandler key;

	public Player(Game game, KeyHandler key) {
		this.game = game;
		this.key = key;
		setDefaultValues();
		getPlayerImage();
	}

	public void setDefaultValues() {
		x = 100;
		y = 100;
		speed = 4;
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

	public void update() {
		if (key.s || key.w || key.a || key.d) { // Evita que el player se mueva cuando no se presiono ninguna tecla
			if (key.s) {
				direction = "down";
				y += speed;
			} else if (key.w) {
				direction = "up";
				y -= speed;
			} else if (key.a) {
				direction = "left";
				x -= speed;
			} else if (key.d) {
				direction = "right";
				x += speed;
			}
			// Funciona como Timer para las animaciones
			spriteCounter++;
			if (spriteCounter > 10 - speed) {
				if (spriteNum == 1) spriteNum = 2;
				else if (spriteNum == 2) spriteNum = 1;
				spriteCounter = 0;
			}
		}
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
		g2.drawImage(image, x, y, game.tileSize, game.tileSize, null);
	}

}
