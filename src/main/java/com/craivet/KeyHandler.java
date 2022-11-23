package com.craivet;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

	public boolean up, down, left, right;

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				up = true;
				break;
			case KeyEvent.VK_S:
				down = true;
				break;
			case KeyEvent.VK_A:
				left = true;
				break;
			case KeyEvent.VK_D:
				right = true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Obtiene el codigo de tecla asociado con la tecla pulsada
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) up = false;
		if (code == KeyEvent.VK_S) down = false;
		if (code == KeyEvent.VK_A) left = false;
		if (code == KeyEvent.VK_D) right = false;
	}
}
