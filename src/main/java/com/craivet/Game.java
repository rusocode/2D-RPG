package com.craivet;

import javax.swing.*;
import java.awt.*;

import com.craivet.entity.Entity;
import com.craivet.entity.Player;
import com.craivet.input.KeyHandler;
import com.craivet.object.SuperObject;
import com.craivet.tile.TileManager;

public class Game extends JPanel implements Runnable {

	Thread thread;
	public KeyHandler keyHandler = new KeyHandler(this);
	TileManager tileManager = new TileManager(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public Sound music = new Sound();
	public Sound sound = new Sound();
	public UI ui = new UI(this);

	// Entities and objects
	public Player player = new Player(this, keyHandler);
	public Entity[] npcs = new Entity[10]; // TODO O entities?
	public SuperObject[] objs = new SuperObject[10];

	// Game state
	public int gameState;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;

	// Screen settings
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;
	public final int tileSize = originalTileSize * scale; // 48x48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

	// World settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;

	final int fps = 60;
	private boolean running;

	public Game() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.black);
		// Mejora el rendimiento de representacion (es algo parecido al metodo getBufferStrategy() de Canvas)
		setDoubleBuffered(true);
		addKeyListener(keyHandler);
		setFocusable(true);
	}

	@Override
	public void run() {

		setup();

		// Intervalo de tiempo entre cada frame aplicando la unidad de tiempo en nanosegundos y 60 fps
		double drawInterval = 1e9 / fps;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int frames = 0;

		while (isRunning()) {

			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += currentTime - lastTime;
			lastTime = currentTime;

			if (delta >= 1) {
				frames++;
				update();
				repaint();
				delta--;
			}

			if (timer >= 1e9) {
				System.out.println(frames + " fps");
				timer = 0;
				frames = 0;
			}

		}

	}

	public void setup() {
		aSetter.setObject();
		aSetter.setNPC();
		// playMusic(0);
		gameState = playState;

	}

	public void update() {
		if (gameState == playState) {
			player.update();
			for (Entity npc : npcs)
				if (npc != null) npc.update();
		}
		if (gameState == pauseState) {
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/* La clase Graphics2D extiende la clase Graphics para proporcionar un control mas sofisticado sobre la
		 * geometria, las transformaciones de coordenadas, la gestion del color y el diseño del texto. */
		Graphics2D g2 = (Graphics2D) g;

		// Debug
		// long drawStart = System.nanoTime();

		tileManager.draw(g2);

		// Objects
		for (SuperObject obj : objs)
			if (obj != null) obj.draw(g2, this);
		// Npcs
		for (Entity npc : npcs)
			if (npc != null) npc.draw(g2);

		player.draw(g2);

		ui.draw(g2);

		// Debug
		// System.out.println("Draw time: " + (System.nanoTime() - drawStart));

		g2.dispose(); // Desecha este contexto de graficos y libera cualquier recurso del sistema que este utilizando
	}

	public synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this, "Game Thread");
		thread.start();
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}

	public void stopMusic() {
		music.stop();
	}

	public void playSound(int i) {
		sound.setFile(i);
		sound.play();
	}


}

