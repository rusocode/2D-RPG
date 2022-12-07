package com.craivet;

import java.awt.*;

public class UI {

	Game game;
	Graphics2D g2;
	Font arial40, arial80B;

	public String currentDialogue;

	public UI(Game game) {
		this.game = game;
		arial40 = new Font("Arial", Font.PLAIN, 40);
		arial80B = new Font("Arial", Font.BOLD, 80);
	}

	public void draw(Graphics2D g2) {
		this.g2 = g2;
		g2.setFont(arial40);
		g2.setColor(Color.white);

		// if (game.gameState == game.playState) {}
		if (game.gameState == game.pauseState) drawPauseScreen();
		if (game.gameState == game.dialogueState) drawDialogueScreen();

	}

	public void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
		String text = "PAUSED";
		int x = getXForCenteredText(text);
		int y = game.screenHeight / 2;
		g2.drawString(text, x, y);
	}

	public void drawDialogueScreen() {
		// Window
		int x = game.tileSize * 2;
		int y = game.tileSize / 2;
		int width = game.screenWidth - (game.tileSize * 4);
		int height = game.tileSize * 4;
		drawSubWindow(x, y, width, height);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28f));
		x += game.tileSize;
		y += game.tileSize;

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}

	}

	private void drawSubWindow(int x, int y, int width, int height) {
		Color c = new Color(0, 0, 0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

	}

	public int getXForCenteredText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return game.screenWidth / 2 - length / 2;
	}

}