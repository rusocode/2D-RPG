package com.craivet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.object.Heart;

import static com.craivet.utils.Constants.*;

/**
 * Interfaz de usuario.
 *
 * <p>TODO Implementar musica para pantalla de titulo (<a href="https://www.youtube.com/watch?v=blyK-QkZkQ8">...</a>)
 */

public class UI {

	private final Game game;
	private final BufferedImage heartFull, heartHalf, heartBlank;
	private Graphics2D g2;
	public String currentDialogue;
	public int commandNum;
	public int titleScreenState;

	private final ArrayList<String> message = new ArrayList<>();
	private final ArrayList<Integer> messageCounter = new ArrayList<>();

	public int slotCol, slotRow;

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

		// Fuente y color por defecto
		g2.setFont(Assets.medieval1);
		g2.setColor(Color.white);
		// Suaviza los bordes de la fuente
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (game.gameState == game.titleState) drawTitleScreen();
		if (game.gameState == game.playState) {
			drawPlayerLife();
			drawMessage();
		}
		if (game.gameState == game.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}
		if (game.gameState == game.dialogueState) {
			drawPlayerLife();
			drawDialogueScreen();
		}
		if (game.gameState == game.characterState) {
			drawCharacterScreen();
			drawInventory();
		}
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

	private void drawMessage() {
		int messageX = game.tileSize;
		int messageY = game.tileSize * 4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));

		for (int i = 0; i < message.size(); i++) {
			if (message.get(i) != null) {

				// Sombra
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX + 2, messageY + 2);
				// Color principal
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);

				// Actua como messageCounter++
				messageCounter.set(i, messageCounter.get(i) + 1);

				messageY += 50;

				// Despues de 3 segundos, elimina los mensajes en consola
				if (messageCounter.get(i) > 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
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

		// SubWindow
		final int frameX = game.tileSize * 2;
		final int frameY = game.tileSize / 2;
		final int frameWidth = game.screenWidth - (game.tileSize * 4);
		final int frameHeight = game.tileSize * 4;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight, 22f);

		// Text
		int textX = frameX + game.tileSize;
		int textY = frameY + game.tileSize;

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}
	}

	private void drawCharacterScreen() {

		// SubWindow
		final int frameX = game.tileSize;
		final int frameY = game.tileSize;
		final int frameWidth = game.tileSize * 7;
		final int frameHeight = game.tileSize * 10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight, 28f);

		int textX = frameX + 20;
		int textY = frameY + game.tileSize;
		final int gap = 37; // Espacio entre lineas

		// Names
		g2.drawString("Level", textX, textY);
		textY += gap;
		g2.drawString("Life", textX, textY);
		textY += gap;
		g2.drawString("Strength", textX, textY);
		textY += gap;
		g2.drawString("Dexterity", textX, textY);
		textY += gap;
		g2.drawString("Attack", textX, textY);
		textY += gap;
		g2.drawString("Defense", textX, textY);
		textY += gap;
		g2.drawString("Exp", textX, textY);
		textY += gap;
		g2.drawString("Next Level", textX, textY);
		textY += gap;
		g2.drawString("Coin", textX, textY);
		textY += gap + 15;
		g2.drawString("Weapon", textX, textY);
		textY += gap + 15;
		g2.drawString("Shield", textX, textY);

		// Values
		int tailX = (frameX + frameWidth) - 30; // Cola de la posicion x
		// Reset textY
		textY = frameY + game.tileSize;
		String value;

		value = String.valueOf(game.player.level);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.life + "/" + game.player.maxLife);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.strength);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.dexterity);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.attack);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.defense);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.exp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.nextLevelExp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.coin);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		g2.drawImage(game.player.currentWeapon.movementDown1, tailX - 40, textY - 15, null);
		textY += gap;

		g2.drawImage(game.player.currentShield.movementDown1, tailX - 40, textY, null);

	}

	private void drawInventory() {

		// TODO Estas constantes y variables no tendrian que declararse como globales?
		// SubWindow
		final int frameX = game.tileSize * 9;
		final int frameY = game.tileSize;
		final int frameWidth = game.tileSize * 6;
		final int frameHeight = game.tileSize * 5;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight, 0);

		// Slots
		final int slotXStart = frameX + 20;
		final int slotYStart = frameY + 20;
		int slotX = slotXStart;
		int slotY = slotYStart;
		int gap = game.tileSize + 3;
		// TODO Estas constantes y variables no tendrian que declararse como globales?

		// Draw player´s items
		for (int i = 0; i < game.player.inventory.size(); i++) {
			// Remarca de color amarillo el arma seleccionada
			if (game.player.inventory.get(i) == game.player.currentWeapon || game.player.inventory.get(i) == game.player.currentShield) {
				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, game.tileSize, game.tileSize, 10, 10);
			}

			g2.drawImage(game.player.inventory.get(i).movementDown1, slotX, slotY, null);

			slotX += gap;

			// Salta la fila
			if (i == 4 || i == 9 || i == 14) {
				slotX = slotXStart;
				slotY += gap;
			}

		}

		// Cursor
		int cursorX = slotXStart + (gap * slotCol);
		int cursorY = slotYStart + (gap * slotRow);
		int cursorWidth = game.tileSize;
		int cursorHeight = game.tileSize;

		// Draw cursor
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

		// Description frame
		final int dFrameX = frameX;
		final int dFrameY = frameY + frameHeight;
		final int dFrameWidth = frameWidth;
		final int dFrameHeight = game.tileSize * 3;

		// Draw description text
		int textX = dFrameX + 20;
		int textY = dFrameY + game.tileSize;
		int itemIndex = getItemIndexOnSlot();
		if (itemIndex < game.player.inventory.size()) {
			drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight, 18f);
			for (String line : game.player.inventory.get(itemIndex).itemDescription.split("\n")) {
				g2.drawString(line, textX, textY);
				textY += 32;
			}
		}

	}

	private void drawSubWindow(int x, int y, int width, int height, float fontSize) {
		g2.setFont(g2.getFont().deriveFont(fontSize));
		// Fondo
		g2.setColor(new Color(0, 0, 0, 210));
		g2.fillRoundRect(x, y, width, height, 35, 35);
		// Borde
		g2.setColor(new Color(255, 255, 255));
		g2.setStroke(new BasicStroke(5)); // Grosor del borde
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
	}

	public void addMessage(String text) {
		message.add(text);
		messageCounter.add(0); // Cro que evita un IndexOutOfBoundsException
	}

	/**
	 * Obtiene el indice del item en el slot.
	 */
	public int getItemIndexOnSlot() {
		return slotCol + (slotRow * 5);
	}

	/**
	 * Obtiene x para texo centrado.
	 *
	 * @param text el texto.
	 */
	private int getXForCenteredText(String text) {
		int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return game.screenWidth / 2 - textLength / 2;
	}

	/**
	 * Obtiene x alineada hacia la derecha.
	 *
	 * @param text  el texto.
	 * @param tailX la cola de x.
	 */
	private int getXforAlignToRightText(String text, int tailX) {
		int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return tailX - textLength;
	}

}
