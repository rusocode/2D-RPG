package com.craivet.input;

import com.craivet.Game;

import java.awt.event.*;

public class KeyHandler extends KeyAdapter {

	Game game;
	public boolean w, s, a, d;

	public KeyHandler(Game game) {
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) w = true;
		if (code == KeyEvent.VK_S) s = true;
		if (code == KeyEvent.VK_A) a = true;
		if (code == KeyEvent.VK_D) d = true;
		if (code == KeyEvent.VK_P) {
			if (game.gameState == game.playState) game.gameState = game.pauseState;
			else if (game.gameState == game.pauseState) game.gameState = game.playState;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) w = false;
		if (code == KeyEvent.VK_S) s = false;
		if (code == KeyEvent.VK_A) a = false;
		if (code == KeyEvent.VK_D) d = false;
	}

}
