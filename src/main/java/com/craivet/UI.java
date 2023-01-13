package com.craivet;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.object.Heart;

import static com.craivet.utils.Constants.*;

/**
 * Interfaz de usuario.
 */

public class UI {

	private final Game game;
	private Graphics2D g2;
	BufferedImage heartFull, heartHalf, heartBlank;
	public String currentDialogue;
	public int commandNum;
	// TODO Implementar musica para pantalla de titulo (https://www.youtube.com/watch?v=blyK-QkZkQ8)
	public int titleScreenState;

	public UI(Game game) {
		this.game = game;

		// Create HUD object
		Entity heart = new Heart(game);
		heartFull = heart.heartFull;
		heartHalf = heart.heartHalf;
		heartBlank = heart.heartBlank;

	}

	public void draw(Graphics2D g2) {

		this.g2 = g2;

		g2.setFont(Assets.medieval1);
		g2.setColor(Color.white);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (game.gameState == game.titleState) drawTitleScreen();
		if (game.gameState == game.playState) drawPlayerLife();
		if (game.gameState == game.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}
		if (game.gameState == game.dialogueState) {
			drawPlayerLife();
			drawDialogueScreen();
		}
		if (game.gameState == game.characterState) drawCharacterScreen();
	}

	private void drawTitleScreen() {
		if (titleScreenState == MAIN_SCREEN) {
			// Title
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 96f));
			String text = "Hersir";
			int x = getXForCenteredText(text);
			int y = game.tileSize * 3;

			// Shadow
			g2.setColor(Color.gray);
			g2.drawString(text, x + 5, y + 5);

			// Main color
			g2.setColor(Color.white);
			g2.drawString(text, x, y);

			// Image
			x = game.screenWidth / 2 - (game.tileSize * 2) / 2;
			y += game.tileSize * 2;
			g2.drawImage(game.player.movementDown1, x, y, game.tileSize * 2, game.tileSize * 2, null);

			// Menu
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
			text = "NEW GAME";
			x = getXForCenteredText(text);
			y += game.tileSize * 3.5;
			g2.drawString(text, x, y);
			if (commandNum == 0) g2.drawString(">", x - game.tileSize, y);

			text = "LOAD GAME";
			x = getXForCenteredText(text);
			y += game.tileSize;
			g2.drawString(text, x, y);
			if (commandNum == 1) g2.drawString(">", x - game.tileSize, y);

			text = "QUIT";
			x = getXForCenteredText(text);
			y += game.tileSize;
			g2.drawString(text, x, y);
			if (commandNum == 2) g2.drawString(">", x - game.tileSize, y);
		} else if (titleScreenState == SELECTION_SCREEN) {
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42f));

			String text = "Select your class!";
			int x = getXForCenteredText(text);
			int y = game.tileSize * 3;
			g2.drawString(text, x, y);

			text = "Fighter";
			x = getXForCenteredText(text);
			y += game.tileSize * 3;
			g2.drawString(text, x, y);
			if (commandNum == 0) g2.drawString(">", x - game.tileSize, y);

			text = "Thief";
			x = getXForCenteredText(text);
			y += game.tileSize;
			g2.drawString(text, x, y);
			if (commandNum == 1) g2.drawString(">", x - game.tileSize, y);

			text = "Sorcerer";
			x = getXForCenteredText(text);
			y += game.tileSize;
			g2.drawString(text, x, y);
			if (commandNum == 2) g2.drawString(">", x - game.tileSize, y);

			text = "Back";
			x = getXForCenteredText(text);
			y += game.tileSize * 2;
			g2.drawString(text, x, y);
			if (commandNum == 3) g2.drawString(">", x - game.tileSize, y);
		}
	}

	private void drawPlayerLife() {

		// game.player.life = 1;

		int x = game.tileSize / 2;
		int y = game.tileSize / 2;
		int i = 0;

		// Dibuja los corazones blancos
		while (i < game.player.maxLife / 2) {
			g2.drawImage(heartBlank, x, y, null);
			i++;
			x += game.tileSize;
		}

		// Reset
		x = game.tileSize / 2;
		y = game.tileSize / 2;
		i = 0;

		// Draw current life
		while (i < game.player.life) {
			g2.drawImage(heartHalf, x, y, null);
			i++;
			if (i < game.player.life) g2.drawImage(heartFull, x, y, null);
			i++;
			x += game.tileSize;
		}

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

	private void drawCharacterScreen() {
		final int frameX = game.tileSize * 2;
		final int frameY = game.tileSize;
		final int frameWidth = game.tileSize * 5;
		final int frameHeight = game.tileSize * 10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);

		// Text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32f));

		int textX = frameX + 20;
		int textY = frameY + game.tileSize;
		final int lineHeight = 32;

		// Names
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += lineHeight;
		g2.drawString("Weapon", textX, textY);
		textY += lineHeight;
		g2.drawString("Shield", textX, textY);
		// textY += lineHeight;
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
