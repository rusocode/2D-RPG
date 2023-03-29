package com.craivet;

import javax.swing.*;

/**
 * IMPORTANTE!
 * Este programa depende de la CPU para renderizar, por lo que el rendimiento grafico sera mas debil que el de los
 * juegos que utilizan GPU. Para utilizar la GPU, debemos dar un paso adelante y acceder a OpenGL. Por lo tanto, al usar
 * el modo fullScreen en una pc de bajos recursos, los FPS se verian afectados.
 */

public class Launcher {

	public static JFrame window;

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		window = new JFrame("2D Game");
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Game game = new Game();

		game.config.loadConfig();
		window.add(game);
		window.pack(); // Ajusta el tamaño de la ventana al tamaño y diseño preferidos de sus subcomponentes (=GamePanel)
		window.setLocationRelativeTo(null);
		game.start();
		window.setVisible(true);
	}

}
