package com.craivet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import com.craivet.entity.Entity;
import com.craivet.entity.Player;
import com.craivet.input.KeyHandler;
import com.craivet.tile.TileManager;

/**
 * TODO interpolacion de renderizado?
 */

public class Game extends JPanel implements Runnable {

	// System
	public Thread thread;
	public KeyHandler keyHandler = new KeyHandler(this);
	public EventHandler eHandler = new EventHandler(this);
	public TileManager tileManager = new TileManager(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public Sound music = new Sound();
	public Sound sound = new Sound();

	// Entities
	public ArrayList<Entity> entities = new ArrayList<>();
	public Player player = new Player(this, keyHandler);
	public Entity[] objs = new Entity[10];
	public Entity[] npcs = new Entity[10];

	// Game state
	public int gameState;
	public final int titleState = 0;
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

	// Game Loop
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
		gameState = titleState;

	}

	public void update() {
		if (gameState == playState) {
			player.update();
			for (Entity npc : npcs)
				if (npc != null) npc.update();
		}
		// if (gameState == pauseState) {}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/* La clase Graphics2D extiende la clase Graphics para proporcionar un control mas sofisticado sobre la
		 * geometria, las transformaciones de coordenadas, la gestion del color y el diseño del texto. */
		Graphics2D g2 = (Graphics2D) g;

		// Debug
		// long drawStart = System.nanoTime();

		// Title screen
		if (gameState == titleState) {
			ui.draw(g2);
		} else {
			tileManager.draw(g2);

			// Add entities to the list
			entities.add(player);

			for (Entity npc : npcs)
				if (npc != null) entities.add(npc);
			for (Entity obj : objs)
				if (obj != null) entities.add(obj);

			/* Ordena la lista de entidades dependiendo de la posicion Y. Es decir, si el player esta por encima del npc
			 * entonces este se dibuja por debajo. */
			entities.sort(Comparator.comparingInt(o -> o.worldY));

			// Draw
			for (Entity entity : entities) entity.draw(g2);

			entities.clear();

			ui.draw(g2);
		}


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

