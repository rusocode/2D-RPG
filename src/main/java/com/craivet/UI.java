package com.craivet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.object.Coin;
import com.craivet.object.Heart;
import com.craivet.object.Mana;
import com.craivet.utils.Utils;
import jdk.jshell.execution.Util;

import static com.craivet.utils.Constants.*;

/**
 * Interfaz de usuario.
 *
 * <p>TODO Implementar musica para pantalla de titulo (<a href="https://www.youtube.com/watch?v=blyK-QkZkQ8">...</a>)
 */

public class UI {

	private final Game game;
	private final BufferedImage heartFull, heartHalf, heartBlank;
	private final BufferedImage manaFull, manaBlank;
	private Graphics2D g2;
	public String currentDialogue;
	public int commandNum;
	public int titleScreenState;

	private final ArrayList<String> message = new ArrayList<>();
	private final ArrayList<Integer> messageCounter = new ArrayList<>();

	public int playerSlotCol, playerSlotRow;
	public int npcSlotCol, npcSlotRow;
	public int subState;
	public int counter;
	public Entity npc;

	public UI(Game game) {
		this.game = game;

		// Create HUD object
		Entity heart = new Heart(game);
		heartFull = heart.heartFull;
		heartHalf = heart.heartHalf;
		heartBlank = heart.heartBlank;

		Entity mana = new Mana(game);
		manaFull = mana.manaFull;
		manaBlank = mana.manaBlank;
	}

	public void draw(Graphics2D g2) {

		this.g2 = g2;

		// Fuente y color por defecto
		g2.setFont(Assets.medieval1);
		g2.setColor(Color.white);
		// Suaviza los bordes de la fuente
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (game.gameState == TITLE_STATE) drawTitleScreen();
		if (game.gameState == PLAY_STATE) {
			drawPlayerLife();
			drawConsole();
		}
		if (game.gameState == PAUSE_STATE) {
			drawPlayerLife();
			drawPauseText();
		}
		if (game.gameState == DIALOGUE_STATE) drawDialogueScreen();
		if (game.gameState == CHARACTER_STATE) {
			drawCharacterScreen();
			drawInventory(game.player, true);
		}
		if (game.gameState == OPTION_STATE) drawOptionScreen();
		if (game.gameState == GAME_OVER_STATE) drawGameOverScreen();
		if (game.gameState == TRANSITION_STATE) drawTransition();
		if (game.gameState == TRADE_STATE) drawTradeScreen();
	}

	private void drawTitleScreen() {
		if (titleScreenState == MAIN_SCREEN) {
			// Title
			changeFontSize(96f);
			String text = "Hersir";
			int x = getXForCenteredText(text);
			int y = TILE_SIZE * 3;
			// Title

			// Shadow
			g2.setColor(Color.gray);
			g2.drawString(text, x + 5, y + 5);

			// Main color
			g2.setColor(Color.white);
			g2.drawString(text, x, y);

			// Image
			x = SCREEN_WIDTH / 2 - (TILE_SIZE * 2) / 2;
			y += TILE_SIZE * 2;
			g2.drawImage(game.player.movementDown1, x, y, TILE_SIZE * 2, TILE_SIZE * 2, null);

			// Menu
			changeFontSize(48f);
			text = "NEW GAME";
			x = getXForCenteredText(text);
			y += TILE_SIZE * 3.5;
			g2.drawString(text, x, y);
			if (commandNum == 0) g2.drawString(">", x - TILE_SIZE, y);

			text = "LOAD GAME";
			x = getXForCenteredText(text);
			y += TILE_SIZE;
			g2.drawString(text, x, y);
			if (commandNum == 1) g2.drawString(">", x - TILE_SIZE, y);

			text = "QUIT";
			x = getXForCenteredText(text);
			y += TILE_SIZE;
			g2.drawString(text, x, y);
			if (commandNum == 2) g2.drawString(">", x - TILE_SIZE, y);
		} else if (titleScreenState == SELECTION_SCREEN) {
			g2.setColor(Color.white);
			changeFontSize(42f);

			String text = "Select your class!";
			int x = getXForCenteredText(text);
			int y = TILE_SIZE * 3;
			g2.drawString(text, x, y);

			text = "Fighter";
			x = getXForCenteredText(text);
			y += TILE_SIZE * 3;
			g2.drawString(text, x, y);
			if (commandNum == 0) g2.drawString(">", x - TILE_SIZE, y);

			text = "Thief";
			x = getXForCenteredText(text);
			y += TILE_SIZE;
			g2.drawString(text, x, y);
			if (commandNum == 1) g2.drawString(">", x - TILE_SIZE, y);

			text = "Sorcerer";
			x = getXForCenteredText(text);
			y += TILE_SIZE;
			g2.drawString(text, x, y);
			if (commandNum == 2) g2.drawString(">", x - TILE_SIZE, y);

			text = "Back";
			x = getXForCenteredText(text);
			y += TILE_SIZE * 2;
			g2.drawString(text, x, y);
			if (commandNum == 3) g2.drawString(">", x - TILE_SIZE, y);
		}
	}

	private void drawConsole() {
		int messageX = TILE_SIZE;
		int messageY = TILE_SIZE * 4;
		changeFontSize(29f);

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

		int x = TILE_SIZE / 2;
		int y = TILE_SIZE / 2;
		int i = 0;

		// Dibuja el corazon vacio
		while (i < game.player.maxLife / 2) {
			g2.drawImage(heartBlank, x, y, null);
			i++;
			x += TILE_SIZE;
		}

		// Reset
		x = TILE_SIZE / 2;
		y = TILE_SIZE / 2;
		i = 0;

		// Dibuja la vida actual
		while (i < game.player.life) {
			g2.drawImage(heartHalf, x, y, null);
			i++;
			if (i < game.player.life) g2.drawImage(heartFull, x, y, null);
			i++;
			x += TILE_SIZE;
		}

		// Dibuja la mana vacia
		x = (TILE_SIZE / 2) - 4;
		y = (int) (TILE_SIZE * 1.5);
		i = 0;
		while (i < game.player.maxMana) {
			g2.drawImage(manaBlank, x, y, null);
			i++;
			x += 35;
		}

		// Dibuja la mana actual
		x = (TILE_SIZE / 2) - 4;
		y = (int) (TILE_SIZE * 1.5);
		i = 0;
		while (i < game.player.mana) {
			g2.drawImage(manaFull, x, y, null);
			i++;
			x += 35;
		}

	}

	private void drawPauseText() {
		changeFontSize(80);
		String text = "PAUSED";
		int x = getXForCenteredText(text);
		int y = getYForCenteredText(text);
		g2.drawString(text, x, y);
	}

	private void drawDialogueScreen() {
		final int frameX = TILE_SIZE * 3;
		final int frameY = TILE_SIZE / 2;
		final int frameWidth = SCREEN_WIDTH - (TILE_SIZE * 6);
		final int frameHeight = TILE_SIZE * 4;
		drawSubwindow(frameX, frameY, frameWidth, frameHeight, SUBWINDOW_ALPHA);

		changeFontSize(22f);

		// Text
		int textX = frameX + TILE_SIZE;
		int textY = frameY + TILE_SIZE;

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}
	}

	private void drawCharacterScreen() {
		final int frameWidth = TILE_SIZE * 10;
		final int frameHeight = TILE_SIZE * 11;
		final int frameX = (SCREEN_WIDTH / 2 - frameWidth / 2) - TILE_SIZE * 4;
		final int frameY = TILE_SIZE - 15;
		drawSubwindow(frameX, frameY, frameWidth, frameHeight, SUBWINDOW_ALPHA);

		final int gap = 37; // Espacio entre lineas
		changeFontSize(28f);

		// Names
		int textX = frameX + 20;
		int textY = frameY + TILE_SIZE;
		g2.drawString("Level", textX, textY);
		textY += gap;
		g2.drawString("Life", textX, textY);
		textY += gap;
		g2.drawString("Mana", textX, textY);
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
		textY = frameY + TILE_SIZE;
		String value;

		value = String.valueOf(game.player.level);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.life + "/" + game.player.maxLife);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += gap;

		value = String.valueOf(game.player.mana + "/" + game.player.maxMana);
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

		g2.drawImage(game.player.currentWeapon.image, tailX - 40, textY - 15, null);
		textY += gap;

		g2.drawImage(game.player.currentShield.image, tailX - 40, textY - 5, null);

	}

	private void drawInventory(Entity entity, boolean cursor) {
		int frameX, frameY;
		int frameWidth, frameHeight;
		int slotCol, slotRow;

		// Inventario del lado derecho (player)
		if (entity == game.player) {
			frameWidth = TILE_SIZE * 6;
			frameHeight = TILE_SIZE * 5;
			frameX = (SCREEN_WIDTH / 2 - frameWidth / 2) + TILE_SIZE * 5;
			frameY = TILE_SIZE - 15;
			slotCol = playerSlotCol;
			slotRow = playerSlotRow;
		} else { // Inventario del lado izquierdo (npc)
			frameWidth = TILE_SIZE * 6;
			frameHeight = TILE_SIZE * 5;
			frameX = (SCREEN_WIDTH / 2 - frameWidth / 2) - TILE_SIZE * 5;
			frameY = TILE_SIZE - 15;
			slotCol = npcSlotCol;
			slotRow = npcSlotRow;
		}

		drawSubwindow(frameX, frameY, frameWidth, frameHeight, SUBWINDOW_ALPHA);

		// Slots
		final int slotXStart = frameX + 20;
		final int slotYStart = frameY + 20;
		int slotX = slotXStart;
		int slotY = slotYStart;
		int gap = TILE_SIZE + 3;
		// TODO Estas constantes y variables no tendrian que declararse como globales?

		// Draw player´s items
		for (int i = 0; i < entity.inventory.size(); i++) {
			// Remarca de color amarillo el item seleccionado
			if (entity.inventory.get(i) == entity.currentWeapon || entity.inventory.get(i) == entity.currentShield) {
				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, TILE_SIZE, TILE_SIZE, 10, 10);
			}

			g2.drawImage(entity.inventory.get(i).image, slotX, slotY, null);
			// g2.fillRect(slotX, slotY, 10, 10); // GRILLA DE SLOTS

			slotX += gap;

			// Salta la fila
			if (i == 4 || i == 9 || i == 14) {
				slotX = slotXStart;
				slotY += gap;
			}

		}

		// Cursor
		if (cursor) {
			int cursorX = slotXStart + (gap * slotCol);
			int cursorY = slotYStart + (gap * slotRow);

			// Draw cursor
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect(cursorX, cursorY, TILE_SIZE, TILE_SIZE, 10, 10);

			// Description frame
			int dFrameY = frameY + frameHeight;
			int dFrameHeight = TILE_SIZE * 3;

			// Draw description text
			int textX = frameX + 20;
			int textY = dFrameY + TILE_SIZE + 5;
			int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
			if (itemIndex < entity.inventory.size()) {
				drawSubwindow(frameX, dFrameY, frameWidth, dFrameHeight, SUBWINDOW_ALPHA);
				changeFontSize(18f);
				for (String line : entity.inventory.get(itemIndex).itemDescription.split("\n")) {
					g2.drawString(line, textX, textY);
					textY += 32;
				}
			}
		}

	}

	private void drawOptionScreen() {
		int frameWidth = TILE_SIZE * 10;
		int frameHeight = TILE_SIZE * 10;
		int frameX = (SCREEN_WIDTH / 2 - frameWidth / 2);
		int frameY = TILE_SIZE;
		drawSubwindow(frameX, frameY, frameWidth, frameHeight, SUBWINDOW_ALPHA);

		switch (subState) {
			case 0:
				optionsMain(frameX, frameY, frameWidth, frameHeight);
				break;
			case 1:
				optionsFullScreenNotification(frameX, frameY, frameHeight);
				break;
			case 2:
				optionsControl(frameX, frameY, frameWidth, frameHeight);
				break;
			case 3:
				optionsEndGameConfirmation(frameX, frameY);
				break;
		}

		game.keyH.enter = false;

	}

	private void optionsMain(int frameX, int frameY, int frameWidth, int frameHeight) {
		int textX, textY;

		// Title
		changeFontSize(33f);
		String text = "Options";
		textX = getXForCenteredText(text);
		textY = frameY + TILE_SIZE;
		g2.drawString(text, textX, textY);
		// Title

		changeFontSize(22f);

		// Full Screen ON/OFF
		textX = frameX + TILE_SIZE;
		textY += TILE_SIZE + 24;
		g2.drawString("Full Screen", textX, textY);
		if (commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if (game.keyH.enter) {
				game.fullScreen = !game.fullScreen;
				subState = 1;
			}
		}
		// Music
		textY += TILE_SIZE;
		g2.drawString("Music", textX, textY);
		if (commandNum == 1) g2.drawString(">", textX - 25, textY);
		// Sound
		textY += TILE_SIZE;
		g2.drawString("Sound", textX, textY);
		if (commandNum == 2) g2.drawString(">", textX - 25, textY);
		// Control
		textY += TILE_SIZE;
		g2.drawString("Control", textX, textY);
		if (commandNum == 3) {
			g2.drawString(">", textX - 25, textY);
			if (game.keyH.enter) {
				subState = 2;
				commandNum = 0;
			}
		}
		// End Game
		textY += TILE_SIZE;
		g2.drawString("End Game", textX, textY);
		if (commandNum == 4) {
			g2.drawString(">", textX - 25, textY);
			if (game.keyH.enter) {
				subState = 3;
				commandNum = 0;
			}
		}

		// Full Screen Check Box
		textX = frameX + frameWidth - TILE_SIZE * 3;
		textY = frameY + (TILE_SIZE * 2 + 7);
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(textX, textY, 24, 24);
		if (game.fullScreen) g2.fillRect(textX, textY, 24, 24);
		// Music volume
		textY += TILE_SIZE;
		g2.drawRect(textX, textY, 120, 24);
		int volumeWidth = 24 * game.music.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		// Sound volume
		textY += TILE_SIZE;
		g2.drawRect(textX, textY, 120, 24);
		volumeWidth = 24 * game.sound.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);

		// Back
		textX = frameX + TILE_SIZE;
		textY = frameHeight;
		g2.drawString("Back", textX, textY);
		if (commandNum == 5) {
			g2.drawString(">", textX - 25, textY);
			if (game.keyH.enter) {
				game.gameState = PLAY_STATE;
				commandNum = 0;
			}
		}

		game.config.saveConfig();

	}

	private void optionsFullScreenNotification(int frameX, int frameY, int frameHeight) {
		int textX = frameX + TILE_SIZE;
		int textY = frameY + TILE_SIZE * 3;
		currentDialogue = "The change will take effect \nafter restarting the game";

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}

		// Back
		textX = frameX + TILE_SIZE;
		textY = frameHeight;
		g2.drawString("Back", textX, textY);
		g2.drawString(">", textX - 25, textY);
		if (game.keyH.enter) subState = 0;

	}

	private void optionsControl(int frameX, int frameY, int frameWidth, int frameHeight) {
		int textX, textY;

		// Title
		changeFontSize(33f);
		String text = "Control";
		textX = getXForCenteredText(text);
		textY = frameY + TILE_SIZE;
		g2.drawString(text, textX, textY);
		// Title

		changeFontSize(22f);

		textX = frameX + TILE_SIZE;
		textY += TILE_SIZE * 1.5;
		g2.drawString("Move", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("Confirm/Attack", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("Shoot/Cast", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("Character Screen", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("Pause", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("Options", textX, textY);

		textX = frameX + frameWidth - TILE_SIZE * 3;
		textY = (int) (frameY + TILE_SIZE * 2.5);
		g2.drawString("WASD", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("ENTER", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("F", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("C", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("P", textX, textY);
		textY += TILE_SIZE;
		g2.drawString("ESC", textX, textY);

		// Back
		textX = frameX + TILE_SIZE;
		textY = frameHeight;
		g2.drawString("Back", textX, textY);
		if (commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if (game.keyH.enter) {
				subState = 0;
				commandNum = 3;
			}
		}
	}

	private void optionsEndGameConfirmation(int frameX, int frameY) {
		int textX = frameX + TILE_SIZE;
		int textY = frameY + TILE_SIZE * 3;

		currentDialogue = "Quit the game and \nreturn to the title screen?";
		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}

		// YES
		String text = "Yes";
		textX = getXForCenteredText(text);
		textY += TILE_SIZE * 3;
		g2.drawString(text, textX, textY);
		if (commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if (game.keyH.enter) {
				subState = 0;
				game.gameState = TITLE_STATE;
				game.keyH.t = false;
			}
		}

		// NO
		text = "NO";
		textX = getXForCenteredText(text);
		textY += TILE_SIZE;
		g2.drawString(text, textX, textY);
		if (commandNum == 1) {
			g2.drawString(">", textX - 25, textY);
			if (game.keyH.enter) {
				subState = 0;
				commandNum = 4;
			}
		}

	}

	private void drawGameOverScreen() {
		g2.setColor(new Color(0, 0, 0, 150));
		g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		int x, y;
		String text;

		changeFontSize(110);

		text = "Game Over";
		// Sombra
		g2.setColor(Color.black);
		x = getXForCenteredText(text);
		y = TILE_SIZE * 4;
		g2.drawString(text, x, y);
		// Principal
		g2.setColor(Color.white);
		g2.drawString(text, x - 4, y - 4);
		// Retry
		changeFontSize(50);
		text = "Retry";
		x = getXForCenteredText(text);
		y += TILE_SIZE * 4;
		g2.drawString(text, x, y);
		if (commandNum == 0) g2.drawString(">", x - 40, y);
		// Quit
		text = "Quit";
		x = getXForCenteredText(text);
		y += 55;
		g2.drawString(text, x, y);
		if (commandNum == 1) g2.drawString(">", x - 40, y);

	}

	/**
	 * Cuando el efecto de transicion termina, entonces teletransporta al player.
	 */
	private void drawTransition() {
		counter++;
		g2.setColor(new Color(0, 0, 0, counter * 5));
		g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		if (counter == 50) {
			counter = 0;
			game.gameState = PLAY_STATE;
			game.currentMap = game.eHandler.tempMap;
			game.player.worldX = TILE_SIZE * game.eHandler.tempCol;
			game.player.worldY = TILE_SIZE * game.eHandler.tempRow;
			game.eHandler.previousEventX = game.player.worldX;
			game.eHandler.previousEventY = game.player.worldY;
		}
	}

	private void drawTradeScreen() {
		switch (subState) {
			case 0:
				tradeSelect();
				break;
			case 1:
				tradeBuy();
				break;
			case 2:
				tradeSell();
				break;
		}
		game.keyH.enter = false; // Reinicia la entrada de teclado
	}

	private void tradeSelect() {
		drawDialogueScreen();

		int x = TILE_SIZE * 14;
		int y = TILE_SIZE * 4 + 32;
		int width = TILE_SIZE * 3;
		int height = (int) (TILE_SIZE * 3.5);
		drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);

		// Draw texts
		x += TILE_SIZE;
		y += TILE_SIZE;
		g2.drawString("Buy", x, y);
		if (commandNum == 0) {
			g2.drawString(">", x - 24, y);
			if (game.keyH.enter) subState = 1;
		}
		y += TILE_SIZE;

		g2.drawString("Sell", x, y);
		if (commandNum == 1) {
			g2.drawString(">", x - 24, y);
			if (game.keyH.enter) subState = 2;
		}
		y += TILE_SIZE;

		g2.drawString("Leave", x, y);
		if (commandNum == 2) {
			g2.drawString(">", x - 24, y);
			if (game.keyH.enter) {
				commandNum = 0;
				game.gameState = PLAY_STATE;
			}
		}

	}

	private void tradeBuy() {
		drawInventory(game.player, false);
		drawInventory(npc, true);

		// Draw hint window
		int x = TILE_SIZE * 2;
		int y = TILE_SIZE * 9;
		int width = TILE_SIZE * 6;
		int height = TILE_SIZE * 2;
		drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
		changeFontSize(23f);
		g2.drawString("[esc] Back", x + 24, y + 60);

		// Draw player coin window
		x = TILE_SIZE * 12;
		drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
		g2.drawString("Your coin: " + game.player.coin, x + 24, y + 60);

		int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
		if (itemIndex < npc.inventory.size()) {
			// Draw price window
			x = (int) (TILE_SIZE * 5.5);
			y = (int) (TILE_SIZE * 5.2);
			width = (int) (TILE_SIZE * 2.5);
			height = TILE_SIZE;
			drawSubwindow(x, y, width, height, 240);

			// Draw image coin
			g2.drawImage(Assets.coin, x + 14, y + 10, 32, 32, null);

			// Draw price item
			int price = npc.inventory.get(itemIndex).price;
			String text = "" + price;
			x = getXforAlignToRightText(text, TILE_SIZE * 8 - 20);
			g2.drawString(text, x, y + 32);

			// BUY AN ITEM
			if (game.keyH.enter) {
				if (npc.inventory.get(itemIndex).price > game.player.coin) {
					subState = 0;
					game.gameState = DIALOGUE_STATE;
					currentDialogue = "You need more coin to buy that!";
					drawDialogueScreen();
				} else if (game.player.inventory.size() == MAX_INVENTORY_SIZE) {
					subState = 0;
					game.gameState = DIALOGUE_STATE;
					currentDialogue = "You cannot carry any more!";
				} else {
					game.playSound(Assets.coin_up);
					game.player.coin -= npc.inventory.get(itemIndex).price;
					game.player.inventory.add(npc.inventory.get(itemIndex));
				}
			}

		}

	}

	private void tradeSell() {
		drawInventory(game.player, true);

		// Draw hint window
		int x = TILE_SIZE * 2;
		int y = TILE_SIZE * 9;
		int width = TILE_SIZE * 6;
		int height = TILE_SIZE * 2;
		drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
		changeFontSize(23f);
		g2.drawString("[esc] Back", x + 24, y + 60);

		// Draw player coin window
		x = TILE_SIZE * 12;
		drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
		g2.drawString("Your coin: " + game.player.coin, x + 24, y + 60);

		int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
		if (itemIndex < game.player.inventory.size()) {
			// Draw price window
			x = (int) (TILE_SIZE * 15.5);
			y = (int) (TILE_SIZE * 5.2);
			width = (int) (TILE_SIZE * 2.5);
			height = TILE_SIZE;
			drawSubwindow(x, y, width, height, 255);

			// Draw image icon
			g2.drawImage(Assets.coin, x + 14, y + 10, 32, 32, null);

			// Draw price item
			int price = game.player.inventory.get(itemIndex).price / 2;
			String text = "" + price;
			x = getXforAlignToRightText(text, TILE_SIZE * 18 - 20);
			g2.drawString(text, x, y + 32);

			// SELL AN ITEM
			if (game.keyH.enter) {
				if (game.player.inventory.get(itemIndex) == game.player.currentWeapon || game.player.inventory.get(itemIndex) == game.player.currentShield) {
					commandNum = 0;
					subState = 0;
					game.gameState = DIALOGUE_STATE;
					currentDialogue = "You cannot sell an equipped item!";
				} else {
					game.playSound(Assets.coin_up);
					game.player.inventory.remove(itemIndex);
					game.player.coin += price;
				}
			}

		}

	}

	private void drawSubwindow(int x, int y, int width, int height, int alpha) {
		// Fondo negro
		g2.setColor(new Color(0, 0, 0, alpha));
		g2.fillRoundRect(x, y, width, height, 10, 10);
		// Borde blanco
		g2.setColor(new Color(255, 255, 255));
		g2.setStroke(new BasicStroke(3)); // Grosor del borde
		g2.drawRoundRect(x, y, width, height, 10, 10);
	}

	private void changeFontSize(float fontSize) {
		g2.setFont(g2.getFont().deriveFont(fontSize));
	}

	public void addMessage(String text) {
		message.add(text);
		messageCounter.add(0); // Creo que evita un IndexOutOfBoundsException
	}

	/**
	 * Obtiene el indice del item en el slot.
	 */
	public int getItemIndexOnSlot(int slotCol, int slotRow) {
		return slotCol + (slotRow * 5);
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

	/**
	 * Obtiene x para texo centrado.
	 *
	 * @param text el texto.
	 */
	private int getXForCenteredText(String text) {
		int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return SCREEN_WIDTH / 2 - textLength / 2;
	}

	private int getYForCenteredText(String text) {
		int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getHeight();
		return SCREEN_HEIGHT / 2 + textLength / 2;
	}

}
