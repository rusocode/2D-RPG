package com.craivet.input;

import java.awt.event.*;

import com.craivet.Game;
import com.craivet.gfx.Assets;

import static com.craivet.utils.Constants.*;

public class KeyHandler extends KeyAdapter {

	private final Game game;
	public boolean w, a, s, d, enter, shot;
	public boolean showDebugText;

	public KeyHandler(Game game) {
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (game.gameState == game.titleState) titleState(code);
		else if (game.gameState == game.playState) playState(code);
		else if (game.gameState == game.pauseState) pauseState(code);
		else if (game.gameState == game.dialogueState) dialogueState(code);
		else if (game.gameState == game.characterState) characterState(code);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) w = false;
		if (code == KeyEvent.VK_A) a = false;
		if (code == KeyEvent.VK_S) s = false;
		if (code == KeyEvent.VK_D) d = false;
		if (code == KeyEvent.VK_F) shot = false;
	}

	private void titleState(int code) {
		if (game.ui.titleScreenState == MAIN_SCREEN) {
			if (code == KeyEvent.VK_W) {
				game.ui.commandNum--;
				if (game.ui.commandNum < 0) game.ui.commandNum = 2;
			}
			if (code == KeyEvent.VK_S) {
				game.ui.commandNum++;
				if (game.ui.commandNum > 2) game.ui.commandNum = 0;
			}
			if (code == KeyEvent.VK_ENTER) {
				if (game.ui.commandNum == 0)
					game.gameState = game.playState; // game.ui.titleScreenState = SELECTION_SCREEN;
				// if (game.ui.commandNum == 1) {}
				if (game.ui.commandNum == 2) System.exit(0);
			}
		} else if (game.ui.titleScreenState == SELECTION_SCREEN) {
			if (code == KeyEvent.VK_W) {
				game.ui.commandNum--;
				if (game.ui.commandNum < 0) game.ui.commandNum = 3;
			}
			if (code == KeyEvent.VK_S) {
				game.ui.commandNum++;
				if (game.ui.commandNum > 3) game.ui.commandNum = 0;
			}
			if (code == KeyEvent.VK_ENTER) {
				if (game.ui.commandNum == 0 || game.ui.commandNum == 1 || game.ui.commandNum == 2)
					game.gameState = game.playState;
				if (game.ui.commandNum == 3) {
					game.ui.commandNum = 0;
					game.ui.titleScreenState = MAIN_SCREEN;
				}
			}
		}
	}

	private void playState(int code) {
		if (code == KeyEvent.VK_W) w = true;
		if (code == KeyEvent.VK_A) a = true;
		if (code == KeyEvent.VK_S) s = true;
		if (code == KeyEvent.VK_D) d = true;
		if (code == KeyEvent.VK_P) game.gameState = game.pauseState;
		if (code == KeyEvent.VK_C) game.gameState = game.characterState;
		if (code == KeyEvent.VK_ENTER) enter = true;
		if (code == KeyEvent.VK_F) shot = true;
		if (code == KeyEvent.VK_T) showDebugText = !showDebugText;
		/* Necesita guardar el archivo de texto editado presionando Ctrl + F9 o seleccionando Build > Build Project. Lo
		 * que reconstruira el proyecto y puede aplicar el cambio presionando la tecla R. */
		if (code == KeyEvent.VK_R) game.tileManager.loadMap("maps/worldV2.txt");
	}

	private void pauseState(int code) {
		if (code == KeyEvent.VK_P) game.gameState = game.playState;
	}

	private void dialogueState(int code) {
		if (code == KeyEvent.VK_ENTER) game.gameState = game.playState;
	}

	private void characterState(int code) {
		if (code == KeyEvent.VK_C) game.gameState = game.playState;
		if (code == KeyEvent.VK_W) {
			if (game.ui.slotRow > 0) {
				game.playSound(Assets.cursor);
				game.ui.slotRow--;
			}
		}
		if (code == KeyEvent.VK_A) {
			if (game.ui.slotCol > 0) {
				game.playSound(Assets.cursor);
				game.ui.slotCol--;
			}
		}
		if (code == KeyEvent.VK_S) {
			if (game.ui.slotRow < 3) {
				game.playSound(Assets.cursor);
				game.ui.slotRow++;
			}
		}
		if (code == KeyEvent.VK_D) {
			if (game.ui.slotCol < 4) {
				game.playSound(Assets.cursor);
				game.ui.slotCol++;
			}
		}
		if (code == KeyEvent.VK_ENTER) game.player.selectItem();
	}

}
