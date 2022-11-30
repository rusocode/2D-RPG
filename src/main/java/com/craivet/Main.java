package com.craivet;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2D Game");

		Game game = new Game();
		window.add(game);
		window.pack(); // Ajusta el tamaño de la ventana al tamaño y diseño preferidos de sus subcomponentes (=GamePanel)
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		// game.setup();
		game.startGameThread();
	}


}
