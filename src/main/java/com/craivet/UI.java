package com.craivet;

import com.craivet.object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {

	Game game;
	Font arial40, arial80B;
	BufferedImage keyImage;

	public boolean messageOn;
	public String message;
	public int messageCounter;
	public boolean gameFinished;

	double playTime;
	public DecimalFormat dFormat = new DecimalFormat("#0.00");

	public UI(Game game) {
		this.game = game;
		arial40 = new Font("Arial", Font.PLAIN, 40);
		arial80B = new Font("Arial", Font.BOLD, 80);
		OBJ_Key key = new OBJ_Key(game);
		keyImage = key.image;
	}

	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}

	public void draw(Graphics2D g2) {

		if (gameFinished) {

			String text;
			int textLength, x, y;

			g2.setFont(arial40);
			g2.setColor(Color.white);
			text = "You found the treasure!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth(); // Obtiene el ancho de la cadena de texto
			x = game.screenWidth / 2 - textLength / 2;
			y = game.screenHeight / 2 - (game.tileSize * 3);
			g2.drawString(text, x, y);

			g2.setFont(g2.getFont().deriveFont(Font.ITALIC));
			text = "Your time is " + dFormat.format(playTime) + "!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = game.screenWidth / 2 - textLength / 2;
			y = game.screenHeight / 2 + (game.tileSize * 4);
			g2.drawString(text, x, y);

			g2.setFont(arial80B);
			g2.setColor(Color.yellow);
			text = "Congratulations!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = game.screenWidth / 2 - textLength / 2;
			y = game.screenHeight / 2 + (game.tileSize * 2);
			g2.drawString(text, x, y);

			game.thread = null;

		} else {

			g2.setFont(arial40);
			g2.setColor(Color.white);
			g2.drawImage(keyImage, game.tileSize / 2, game.tileSize / 2, game.tileSize, game.tileSize, null);
			g2.drawString("x " + game.player.hasKey, 74, 65);

			playTime += (double) 1 / 60;
			g2.drawString("Time: " + dFormat.format(playTime), game.tileSize * 11, 65);

			// Mensaje
			if (messageOn) {
				g2.setFont(g2.getFont().deriveFont(30F));
				g2.drawString(message, game.tileSize / 2, game.tileSize * 5);

				messageCounter++;

				// El mensaje aparece por dos segundos
				if (messageCounter > 120) {
					messageCounter = 0;
					messageOn = false;
				}
			}
		}

	}

}
