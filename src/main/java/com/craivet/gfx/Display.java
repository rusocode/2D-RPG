package com.craivet.gfx;

import com.craivet.Game;

import javax.swing.*;

public class Display extends JFrame {

	public Display(Game game) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(game);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
