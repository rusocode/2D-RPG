package com.craivet;

import javax.swing.*;

public class Launcher {

	public static JFrame window;

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// System.setProperty("sun.java2d.d3d", "false"); // ?
		window = new JFrame("2D Game");
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Game game = new Game();
		window.add(game);
		window.pack(); // Ajusta el tamaño de la ventana al tamaño y diseño preferidos de sus subcomponentes (=GamePanel)
		window.setLocationRelativeTo(null);
		game.start();
		window.setVisible(true);
	}

}
