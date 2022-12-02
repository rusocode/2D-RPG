package com.craivet.input;

import java.awt.event.*;

public class KeyHandler extends KeyAdapter {

	public boolean w, s, a, d;

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) w = true;
		if (code == KeyEvent.VK_S) s = true;
		if (code == KeyEvent.VK_A) a = true;
		if (code == KeyEvent.VK_D) d = true;
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
