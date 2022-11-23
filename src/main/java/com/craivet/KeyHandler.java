package com.craivet;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

	public boolean w, s, a, d;

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				w = true;
				break;
			case KeyEvent.VK_S:
				s = true;
				break;
			case KeyEvent.VK_A:
				a = true;
				break;
			case KeyEvent.VK_D:
				d = true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Obtiene el codigo de tecla asociado con la tecla pulsada
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) w = false;
		if (code == KeyEvent.VK_S) s = false;
		if (code == KeyEvent.VK_A) a = false;
		if (code == KeyEvent.VK_D) d = false;
	}
}
