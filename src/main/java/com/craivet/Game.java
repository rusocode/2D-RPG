package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.entity.Player;
import com.craivet.input.KeyHandler;
import com.craivet.tile.InteractiveTile;
import com.craivet.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import static com.craivet.utils.Constants.*;

/**
 * TODO Interpolacion de renderizado?
 * TODO Separe los diferentes componentes en clases separadas, como la logica del juego, la logica de dibujo, la logica
 * de sonido, etc.
 * TODO Use un sistema de eventos para manejar los eventos del juego en lugar de la clase EventHandler.
 */

public class Game extends JPanel implements Runnable {

	// System
	public Thread thread;
	public KeyHandler keyH = new KeyHandler(this);
	public EventHandler eHandler = new EventHandler(this);
	public TileManager tileManager = new TileManager(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);

	// Entities
	public ArrayList<Entity> entities = new ArrayList<>();
	public ArrayList<Item> items = new ArrayList<>();
	public ArrayList<Entity> projectiles = new ArrayList<>();
	public Player player = new Player(this, keyH);
	public Item[] objs = new Item[20];
	public Entity[] npcs = new Entity[10];
	public Entity[] mobs = new Entity[20];
	public InteractiveTile[] iTile = new InteractiveTile[50];

	public int gameState;
	private boolean running;

	public Game() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setBackground(Color.black);
		// Mejora el rendimiento de representacion (es algo parecido al metodo getBufferStrategy() de Canvas)
		setDoubleBuffered(true);
		addKeyListener(keyH);
		setFocusable(true);
	}

	@Override
	public void run() {

		setup();

		// Intervalo de tiempo entre cada frame aplicando la unidad de tiempo en nanosegundos y 60 fps
		double drawInterval = 1e9 / FPS;
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
		aSetter.setMOB();
		aSetter.setInteractiveTile();
		/* Cuando balancea la espada o interactua con algo (como tomar una pocion, un hacha, una llave, etc.) por
		 * primera vez despues de que comienza el juego, este se congela durante 0,5 a 1 segundo. Para evitar este
		 * retraso, reproduzca la musica o use un archivo de audio en blanco si no desea reproducir musica. */
		// Sound.play(Assets.blue_boy_adventure);
		// Sound.loop();
		gameState = TITLE_STATE;

	}

	public void update() {
		if (gameState == PLAY_STATE) {
			player.update();
			for (Entity npc : npcs)
				if (npc != null) npc.update();
			for (int i = 0; i < mobs.length; i++) {
				if (mobs[i] != null) {
					/* Cuando muere el mob, primero establece el estado dead a true evitando que siga moviendose. Luego
					 * genera la animacion de muerte y al finalizarla, establece alive en false para que no genere
					 * movimiento y elimine el objeto. */
					if (mobs[i].alive && !mobs[i].dead) mobs[i].update();
					if (!mobs[i].alive) {
						mobs[i].checkDrop();
						mobs[i] = null;
					}
				}
			}
			for (int i = 0; i < projectiles.size(); i++) {
				if (projectiles.get(i) != null) {
					if (projectiles.get(i).alive) projectiles.get(i).update();
					if (!projectiles.get(i).alive) projectiles.remove(i);
				}
			}
			for (int i = 0; i < iTile.length; i++) {
				if (iTile[i] != null) iTile[i].update();
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/* La clase Graphics2D extiende la clase Graphics para proporcionar un control mas sofisticado sobre la
		 * geometria, las transformaciones de coordenadas, la gestion del color y el diseño del texto. */
		Graphics2D g2 = (Graphics2D) g;

		// Debug
		long drawStart = 0;
		if (keyH.showDebugText) drawStart = System.nanoTime();

		// Title screen
		if (gameState == TITLE_STATE) {
			ui.draw(g2);
		} else {
			tileManager.draw(g2);

			for (int i = 0; i < iTile.length; i++) {
				if (iTile[i] != null) iTile[i].draw(g2);
			}

			// Add entities to the list
			entities.add(player);
			for (Entity npc : npcs)
				if (npc != null) entities.add(npc);
			for (Item obj : objs)
				if (obj != null) items.add(obj);
			for (Entity mob : mobs)
				if (mob != null) entities.add(mob);
			for (Entity projectile : projectiles)
				if (projectile != null) entities.add(projectile);

			/* Ordena la lista de entidades dependiendo de la posicion Y. Es decir, si el player esta por encima del npc
			 * entonces este se dibuja por debajo. */
			entities.sort(Comparator.comparingInt(o -> o.worldY));

			// TODO Se podria usar esta forma mas entendible para comparar las entidades
			/*
			 * private Comparator<Entity> spriteSorter = new Comparator<Entity>() {
			 * public int compare(Entity e0, Entity e1) {
			 * if (e1.y < e0.y) return +1;
			 * if (e1.y > e0.y) return -1;
			 * return 0;
			 * }
			 * };
			 * */

			// Draw
			for (Item item : items) item.draw(g2);
			for (Entity entity : entities) entity.draw(g2);

			entities.clear();
			items.clear();

			ui.draw(g2);
		}

		// Debug
		if (keyH.showDebugText) {
			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.setColor(Color.white);
			int x = 8, y = SCREEN_HEIGHT - 25, gap = 20;
			g2.drawString("X: " + (player.worldX + player.bodyArea.x) / TILE_SIZE, x, y);
			y += gap;
			g2.drawString("Y: " + (player.worldY + player.bodyArea.y) / TILE_SIZE, x, y);
			// y += lineHeight;
			// g2.drawString("Draw time: " + (System.nanoTime() - drawStart), x, y);
		}

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

}

