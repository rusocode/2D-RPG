package com.craivet.input;

import com.craivet.Game;

import java.awt.event.*;

public class KeyHandler extends KeyAdapter {

	Game game;
	public boolean w, s, a, d, enter;

	public KeyHandler(Game game) {
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		// Title state
		if (game.gameState == game.titleState) {
			if (game.ui.titleScreenState == 0) {
				if (code == KeyEvent.VK_W) {
					game.ui.commandNum--;
					if (game.ui.commandNum < 0) game.ui.commandNum = 2;
				}
				if (code == KeyEvent.VK_S) {
					game.ui.commandNum++;
					if (game.ui.commandNum > 2) game.ui.commandNum = 0;
				}
				if (code == KeyEvent.VK_ENTER) {
					if (game.ui.commandNum == 0) game.ui.titleScreenState = 1;
					// if (game.ui.commandNum == 1) {}
					if (game.ui.commandNum == 2) System.exit(0);
				}
			} else if (game.ui.titleScreenState == 1) {
				if (code == KeyEvent.VK_W) {
					game.ui.commandNum--;
					if (game.ui.commandNum < 0) game.ui.commandNum = 3;
				}
				if (code == KeyEvent.VK_S) {
					game.ui.commandNum++;
					if (game.ui.commandNum > 3) game.ui.commandNum = 0;
				}
				if (code == KeyEvent.VK_ENTER) {
					if (game.ui.commandNum == 0) game.gameState = game.playState;
					if (game.ui.commandNum == 1) game.gameState = game.playState;
					if (game.ui.commandNum == 2) game.gameState = game.playState;
					if (game.ui.commandNum == 3) {
						game.ui.commandNum = 0;
						game.ui.titleScreenState = 0;
					}
				}
			}
		}

		// Play state
		if (game.gameState == game.playState) {
			if (code == KeyEvent.VK_W) w = true;
			if (code == KeyEvent.VK_S) s = true;
			if (code == KeyEvent.VK_A) a = true;
			if (code == KeyEvent.VK_D) d = true;
			if (code == KeyEvent.VK_P) game.gameState = game.pauseState;
			if (code == KeyEvent.VK_ENTER) enter = true;
		}

		// Pause state
		else if (game.gameState == game.pauseState) {
			if (code == KeyEvent.VK_P) game.gameState = game.playState;
		}

		// Dialogue state
		else if (game.gameState == game.dialogueState) {
			if (code == KeyEvent.VK_ENTER) game.gameState = game.playState;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) w = false;
		if (code == KeyEvent.VK_S) s = false;
		if (code == KeyEvent.VK_A) a = false;
		if (code == KeyEvent.VK_D) d = false;
		// if (code == KeyEvent.VK_ENTER) enter = false;
	}

}
