package com.craivet;

import com.craivet.ai.AStar;
import com.craivet.entity.Entity;
import com.craivet.entity.item.Item;
import com.craivet.entity.mob.Mob;
import com.craivet.entity.npc.Npc;
import com.craivet.entity.projectile.Projectile;
import com.craivet.entity.Player;
import com.craivet.gfx.Assets;
import com.craivet.input.KeyManager;
import com.craivet.tile.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import static com.craivet.utils.Constants.*;

/**
 * TODO Algo que me di cuenta hasta hace poco, es que se nota un pequeño lag al ejecutar una accion (por ejemplo,
 * pulsar la tecla l constantemente) mientras se camina. Supongo que se tendria que utilizar multiprocesos para
 * solucionar esto.
 * TODO Aplico interpolacion de renderizado?
 * TODO Separar los diferentes componentes en clases separadas, como la logica del juego, la logica de dibujo, la logica
 * de sonido, etc.
 * TODO Use un sistema de eventos para manejar los eventos del juego en lugar de la clase EventHandler.
 */

public class Game extends JPanel implements Runnable {

	// System
	public Thread thread;
	public KeyManager key = new KeyManager(this);
	public EventHandler event = new EventHandler(this);
	public TileManager tileManager = new TileManager(this);
	public Collider collider = new Collider(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public AudioManager sound = new AudioManager();
	public AudioManager music = new AudioManager();
	public UI ui = new UI(this);
	public Config config = new Config(this);
	public AStar aStar = new AStar(this);

	// Entities
	public Player player = new Player(this, key);
	public Entity[][] items = new Item[MAX_MAP][20];
	public Entity[][] mobs = new Mob[MAX_MAP][20];
	public Entity[][] npcs = new Npc[MAX_MAP][10];
	public Entity[][] projectiles = new Projectile[MAX_MAP][20];
	public ArrayList<Entity> entities = new ArrayList<>();
	public ArrayList<Entity> itemList = new ArrayList<>();
	public ArrayList<Entity> particles = new ArrayList<>();
	// Interactive tiles
	public InteractiveTile[][] iTile = new InteractiveTile[MAX_MAP][50];

	public int gameState;
	public int currentMap;
	private boolean running;
	public boolean fullScreen;

	/* La clase Graphics2D extiende la clase Graphics para proporcionar un control mas sofisticado sobre la geometria,
	 * las transformaciones de coordenadas, la gestion del color y el diseño del texto. */
	public Graphics2D g2;
	public BufferedImage tempScree;
	// Para pantalla completa
	public int screenWidth = SCREEN_WIDTH;
	public int screenHeight = SCREEN_HEIGHT;

	public Game() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setBackground(Color.black);
		setDoubleBuffered(true); // Mejora el rendimiento de representacion (es algo parecido al metodo getBufferStrategy() de Canvas)
		addKeyListener(key);
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
				// repaint();
				drawToTempScreen();
				drawToScreen();
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
		playMusic(Assets.music_blue_boy_adventure);
		gameState = PLAY_STATE;

		/* Hasta ahora dibujamos todo directamente en el JPanel. Pero esta vez seguimos dos pasos:
		 * 1. Dibujamos todo en la pantalla temporal para la imagen que esta detras de escena.
		 * 2. Dibujamos la pantalla temporal en el JPanel.
		 * La razon de esto, es que queremos mostrar el juego en pantalla completa o en otras resoluciones diferentes.
		 * Eso significa que debemos cambiar el tamaña de todas las imagenes (tiles, entidades, ui, etc.). Y si
		 * cambiambos el tamaño de cada objeto uno por uno, es mucho trabajo y poco eficiente. Asi que primero dibujamos
		 * todo en una unica imagen almacenada en buffer y luego cambiamos el tamaño de esta imagen grande para que se
		 * ajuste a nuestra pantalla completa en cada bucle. */

		// Crea una imagen blanca en el buffer que es tan grande como nuestra pantalla
		tempScree = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		// Enlaza g2 con la imagen de buffer de pantalla temporal
		g2 = (Graphics2D) tempScree.getGraphics();

		if (fullScreen) setFullScreen2();

	}

	public void update() {
		if (gameState == PLAY_STATE) {
			player.update();
			for (int i = 0; i < npcs[1].length; i++)
				if (npcs[currentMap][i] != null) npcs[currentMap][i].update();
			for (int i = 0; i < mobs[1].length; i++) {
				if (mobs[currentMap][i] != null) {
					/* Cuando muere el mob, primero establece el estado dead a true evitando que siga moviendose. Luego
					 * genera la animacion de muerte y al finalizarla, establece alive en false para que no genere
					 * movimiento y elimine el objeto. */
					if (mobs[currentMap][i].alive && !mobs[currentMap][i].dead) mobs[currentMap][i].update();
					if (!mobs[currentMap][i].alive) {
						mobs[currentMap][i].checkDrop();
						mobs[currentMap][i] = null;
					}
				}
			}
			for (int i = 0; i < projectiles[1].length; i++) {
				if (projectiles[currentMap][i] != null) {
					if (projectiles[currentMap][i].alive) projectiles[currentMap][i].update();
					if (!projectiles[currentMap][i].alive) projectiles[currentMap][i] = null;
				}
			}
			for (int i = 0; i < particles.size(); i++) {
				if (particles.get(i) != null) {
					if (particles.get(i).alive) particles.get(i).update();
					if (!particles.get(i).alive) particles.remove(i);
				}
			}
			for (int i = 0; i < iTile[1].length; i++)
				if (iTile[currentMap][i] != null) iTile[currentMap][i].update();
		}
	}

	/**
	 * Dibuja todo en la imagen almacenada en el buffer.
	 */
	public void drawToTempScreen() {
		// Debug
		long drawStart = 0;
		if (key.t) drawStart = System.nanoTime();

		if (gameState == TITLE_STATE) {
			g2.setColor(Color.black);
			g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			ui.draw(g2);
		} else {
			tileManager.draw(g2);

			for (int i = 0; i < iTile[1].length; i++)
				if (iTile[currentMap][i] != null) iTile[currentMap][i].draw(g2);

			// Agrega las entidades a la lista de entidades
			entities.add(player);
			for (int i = 0; i < items[1].length; i++)
				if (items[currentMap][i] != null) itemList.add(items[currentMap][i]);
			for (int i = 0; i < npcs[1].length; i++)
				if (npcs[currentMap][i] != null) entities.add(npcs[currentMap][i]);
			for (int i = 0; i < mobs[1].length; i++)
				if (mobs[currentMap][i] != null) entities.add(mobs[currentMap][i]);
			for (int i = 0; i < projectiles[1].length; i++)
				if (projectiles[currentMap][i] != null) entities.add(projectiles[currentMap][i]);
			for (Entity particle : particles)
				if (particle != null) entities.add(particle);

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

			for (Entity item : itemList) item.draw(g2);
			for (Entity entity : entities) entity.draw(g2);

			entities.clear();
			itemList.clear();

			ui.draw(g2);
		}

		// Debug
		if (key.t) {
			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.setColor(Color.white);
			int x = 8, y = SCREEN_HEIGHT - 25, gap = 20;
			g2.drawString("X: " + (player.worldX + player.hitbox.x) / tile_size, x, y);
			y += gap;
			g2.drawString("Y: " + (player.worldY + player.hitbox.y) / tile_size, x, y);
			// y += lineHeight;
			// g2.drawString("Draw time: " + (System.nanoTime() - drawStart), x, y);
		}

		// g2.dispose(); // Desecha este contexto de graficos y libera cualquier recurso del sistema que este utilizando
	}

	/**
	 * Dibuja la imagen almacenada en el buffer en la pantalla.
	 */
	public void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(tempScree, 0, 0, screenWidth, screenHeight, null);
		g.dispose();
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

	public void playMusic(URL url) {
		music.play(url);
		music.loop();
	}

	public void stopMusic() {
		music.stop();
	}

	public void playSound(URL url) {
		sound.play(url);
	}

	public void setFullScreen() {
		// Obtiene el dispositivo de pantalla local, independientemente de si estas en un portatil o escritorio
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Launcher.window); // Establece la ventana en pantalla completa
		// Obtiene el ancho y alto de la pantalla completa para utilizarlos en la pantalla temporal
		// TODO Esto va aca?
		screenWidth = Launcher.window.getWidth();
		screenHeight = Launcher.window.getHeight();
	}

	// Soluciona el problema de la caida de FPS
	public void setFullScreen2() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Launcher.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		screenWidth = (int) width;
		screenHeight = (int) height;
		// offset factor to be used by mouse listener or mouse motion listener if you are using cursor in your game. Multiply your e.getX()e.getY() by this.
		// fullScreenOffsetFactor = (float) screenWidth / (float) screenWidth2;
	}

	public void retry() {
		player.setDefaultPosition();
		player.restoreLifeAndMana();
		aSetter.setNPC();
		aSetter.setMOB();
	}

	public void restart() {
		player.initDefaultValues();
		player.setDefaultPosition();
		player.setItems();
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMOB();
		aSetter.setInteractiveTile();
	}

}

