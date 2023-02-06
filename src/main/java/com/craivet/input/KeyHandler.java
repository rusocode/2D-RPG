package com.craivet.input;

import java.awt.event.*;

import com.craivet.Game;
import com.craivet.Sound;
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
		if (game.gameState == TITLE_STATE) titleState(code);
		else if (game.gameState == PLAY_STATE) playState(code);
		else if (game.gameState == PAUSE_STATE) pauseState(code);
		else if (game.gameState == DIALOGUE_STATE) dialogueState(code);
		else if (game.gameState == CHARACTER_STATE) characterState(code);
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
				if (game.ui.commandNum == 0){
					Sound.play(Assets.spawn);
					game.gameState = PLAY_STATE; // game.ui.titleScreenState = SELECTION_SCREEN;
				}
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
					game.gameState = PLAY_STATE;
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
		if (code == KeyEvent.VK_P) game.gameState = PAUSE_STATE;
		if (code == KeyEvent.VK_C) game.gameState = CHARACTER_STATE;
		if (code == KeyEvent.VK_ENTER) enter = true;
		if (code == KeyEvent.VK_F) shot = true;
		if (code == KeyEvent.VK_T) showDebugText = !showDebugText;
		/* Necesita guardar el archivo de texto editado presionando Ctrl + F9 o seleccionando Build > Build Project. Lo
		 * que reconstruira el proyecto y puede aplicar el cambio presionando la tecla R. */
		if (code == KeyEvent.VK_R) game.tileManager.loadMap("maps/worldV2.txt");
	}

	private void pauseState(int code) {
		if (code == KeyEvent.VK_P) game.gameState = PLAY_STATE;
	}

	private void dialogueState(int code) {
		if (code == KeyEvent.VK_ENTER) game.gameState = PLAY_STATE;
	}

	private void characterState(int code) {
		if (code == KeyEvent.VK_C) game.gameState = PLAY_STATE;
		if (code == KeyEvent.VK_W) {
			if (game.ui.slotRow > 0) {
				Sound.play(Assets.cursor);
				game.ui.slotRow--;
			}
		}
		if (code == KeyEvent.VK_A) {
			if (game.ui.slotCol > 0) {
				Sound.play(Assets.cursor);
				game.ui.slotCol--;
			}
		}
		if (code == KeyEvent.VK_S) {
			if (game.ui.slotRow < 3) {
				Sound.play(Assets.cursor);
				game.ui.slotRow++;
			}
		}
		if (code == KeyEvent.VK_D) {
			if (game.ui.slotCol < 4) {
				Sound.play(Assets.cursor);
				game.ui.slotCol++;
			}
		}
		if (code == KeyEvent.VK_ENTER) game.player.selectItem();
	}

}
