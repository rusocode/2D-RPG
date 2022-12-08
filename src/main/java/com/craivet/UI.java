package com.craivet;

import java.awt.*;

import com.craivet.gfx.Assets;

/**
 * Interfaz de usuario.
 */

public class UI {

	private final Game game;
	private Graphics2D g2;
	public String currentDialogue;

	public UI(Game game) {
		this.game = game;
	}

	public void draw(Graphics2D g2) {

		this.g2 = g2;

		g2.setFont(Assets.medieval1);
		g2.setColor(Color.white);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// if (game.gameState == game.playState) {}
		if (game.gameState == game.pauseState) drawPauseScreen();
		if (game.gameState == game.dialogueState) drawDialogueScreen();

	}

	private void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(80f));
		String text = "PAUSED";
		int x = getXForCenteredText(text);
		int y = game.screenHeight / 2;
		g2.drawString(text, x, y);
	}

	private void drawDialogueScreen() {
		// Window
		int x = game.tileSize * 2;
		int y = game.tileSize / 2;
		int width = game.screenWidth - (game.tileSize * 4);
		int height = game.tileSize * 4;
		drawSubWindow(x, y, width, height);

		x += game.tileSize;
		y += game.tileSize;

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}

	}

	private void drawSubWindow(int x, int y, int width, int height) {
		// Fondo
		g2.setColor(new Color(0, 0, 0, 210));
		g2.fillRoundRect(x, y, width, height, 35, 35);
		// Borde
		g2.setColor(new Color(255, 255, 255));
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
	}

	private int getXForCenteredText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return game.screenWidth / 2 - length / 2;
	}

}
