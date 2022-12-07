package com.craivet;

import com.craivet.object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {

	Game game;
	Graphics2D g2;
	Font arial40, arial80B;

	public boolean messageOn;
	public String message;
	public int messageCounter;
	public boolean gameFinished;

	public UI(Game game) {
		this.game = game;
		arial40 = new Font("Arial", Font.PLAIN, 40);
		arial80B = new Font("Arial", Font.BOLD, 80);
	}

	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}

	public void draw(Graphics2D g2) {
		this.g2 = g2;
		g2.setFont(arial40);
		g2.setColor(Color.white);

		if (game.gameState == game.playState) {

		}
		if (game.gameState == game.pauseState) {
			drawPauseScreen();
		}

	}

	public void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
		String text = "PAUSED";
		int x = getXForCenteredText(text);
		int y = game.screenHeight / 2;
		g2.drawString(text, x, y);
	}

	public int getXForCenteredText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return game.screenWidth / 2 - length / 2;
	}

}
