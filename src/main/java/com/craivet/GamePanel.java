package com.craivet;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

	// Screen settings
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;

	final int tileSize = originalTileSize * scale; // 48x48 tile
	// ¿Cuantos tiles se pueden mostrar en una sola pantalla horizontal y verticalmente?
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	final int screenHeight = tileSize * maxScreenRow; // 576 pixels

	Thread gameThread;

	public GamePanel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.black);
		// Mejora el rendimiento de representacion de los juegos (es algo parecido al metodo getBufferStrategy() de Canvas)
		setDoubleBuffered(true);
	}

	@Override
	public void run() {

	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
}

