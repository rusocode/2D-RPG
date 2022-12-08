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
		if (code == KeyEvent.VK_ENTER) enter = false;
	}

}
