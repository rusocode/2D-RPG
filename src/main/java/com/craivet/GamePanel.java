package com.craivet;

import com.craivet.entity.Player;
import com.craivet.gfx.Assets;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

	// Screen settings
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // 48x48 tile
	// ¿Cuantos tiles se pueden mostrar en una sola pantalla horizontal y verticalmente?
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	final int screenHeight = tileSize * maxScreenRow; // 576 pixels

	Thread gameThread;
	KeyHandler keyHandler = new KeyHandler();
	Player player = new Player(this, keyHandler);

	final int fps = 60;

	public GamePanel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.black);
		// Mejora el rendimiento de representacion (es algo parecido al metodo getBufferStrategy() de Canvas)
		setDoubleBuffered(true);
		addKeyListener(keyHandler);
		setFocusable(true);
	}

	@Override
	public void run() {

		// init();

		// Intervalo de tiempo entre cada frame aplicando la unidad de tiempo en nanosegundos y 60 fps
		double drawInterval = 1e9 / fps;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int frames = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += currentTime - lastTime;
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				frames++;
			}

			if (timer >= 1e9) {
				System.out.println("FPS: " + frames);
				timer = 0;
				frames = 0;
			}

		}

	}

	public void update() {
		player.update();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/* La clase Graphics2D extiende la clase Graphics para proporcionar un control mas sofisticado sobre la
		 * geometria, las transformaciones de coordenadas, la gestion del color y el diseño del texto. */
		Graphics2D g2 = (Graphics2D) g;
		player.draw(g2);
		g2.dispose(); // Desecha este contexto de graficos y libera cualquier recurso del sistema que este utilizando
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
}

