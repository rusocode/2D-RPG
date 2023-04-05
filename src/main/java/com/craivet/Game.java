package com.craivet;

import com.craivet.ai.AStar;
import com.craivet.entity.Player;
import com.craivet.input.KeyManager;
import com.craivet.states.GameState;
import com.craivet.states.StateManager;
import com.craivet.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

/**
 * TODO Algo que me di cuenta hasta hace poco, es que se nota un pequeño lag al ejecutar una accion (por ejemplo,
 * pulsar la tecla l constantemente) mientras se camina. Supongo que se tendria que utilizar multiprocesos para
 * solucionar esto.
 * TODO Separar los diferentes componentes en clases separadas, como la logica del juego, la logica de dibujo, la logica
 * de sonido, etc.
 * TODO Use un sistema de eventos para manejar los eventos del juego en lugar de la clase EventHandler.
 */

public class Game extends JPanel implements Runnable {

	// System
	public Thread thread;
	public AudioManager sound = new AudioManager();
	public AudioManager music = new AudioManager();
	public World world = new World(this);
	public EventHandler event = new EventHandler(this, world);
	public AssetSetter aSetter = new AssetSetter(this, world);
	public Collider collider = new Collider(this, world);
	public UI ui = new UI(this, world);
	public Config config = new Config(this);
	public AStar aStar = new AStar(world);
	public KeyManager key = new KeyManager(this, world);
	public Player player = new Player(this, world);

	public StateManager stateManager = new StateManager();
	public GameState gState;
	public int gameState;
	private boolean running;

	public boolean fullScreen;

	public Graphics2D g2;
	public BufferedImage tempScreen;
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

		init();

		double deltaTime = 0;
		double nsPerTick = 1E9 / TICKS;
		double nsPerFrame = 1E9 / MAX_FPS;
		long lastTick = TimeUtils.nanoTime();
		long lastRender = TimeUtils.nanoTime();
		long timer = 0;
		int ticks = 0, frames = 0;

		while (isRunning()) {
			long now = TimeUtils.nanoTime();
			deltaTime += (now - lastTick) / nsPerTick;
			timer += now - lastTick;
			lastTick = now;

			if (deltaTime >= 1) {
				ticks++;
				update();
				deltaTime--;
			}

			if (FPS_UNLIMITED || now - lastRender >= nsPerFrame) {
				frames++;
				lastRender = TimeUtils.nanoTime();
				renderToTempScreen();
				renderToScreen();
			}

			if (timer >= 1E9) {
				System.out.println(ticks + " ticks, " + frames + " fps");
				timer = 0;
				ticks = 0;
				frames = 0;
			}
		}

	}

	public void init() {
		setAssets();

		playMusic(music_blue_boy_adventure);
		gameState = PLAY_STATE;

		gState = new GameState(world);
		stateManager.setState(gState);

		/* Hasta ahora dibujamos todo directamente en el JPanel. Pero esta vez seguimos dos pasos:
		 * 1. Dibujamos todo en la pantalla temporal para la imagen que esta detras de escena.
		 * 2. Dibujamos la pantalla temporal en el JPanel.
		 * La razon de esto, es que queremos mostrar el juego en pantalla completa o en otras resoluciones diferentes.
		 * Eso significa que debemos cambiar el tamaña de todas las imagenes (tiles, entidades, ui, etc.). Y si
		 * cambiambos el tamaño de cada objeto uno por uno, es mucho trabajo y poco eficiente. Asi que primero dibujamos
		 * todo en una unica imagen almacenada en buffer y luego cambiamos el tamaño de esta imagen grande para que se
		 * ajuste a nuestra pantalla completa en cada bucle. Ademas, parece que dibujando directamente en el JPanel
		 * desde el metodo repaint(), se generaban algunos efectos extranios en el renderizado (cortes, pantalla negra). */
		// Crea una imagen blanca en el buffer que es tan grande como nuestra pantalla
		tempScreen = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		// Enlaza g2 con la imagen de buffer de pantalla temporal
		g2 = (Graphics2D) tempScreen.getGraphics();

	}

	public void update() {
		if (stateManager.getState() != null) stateManager.getState().update();
	}

	public void renderToTempScreen() {
		if (stateManager.getState() != null) stateManager.getState().render(g2);
	}

	private void renderToScreen() {
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth, screenHeight, null);
		g.dispose();
	}

	public synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this, "Game Thread");
		thread.start();
	}

	public synchronized void stop() {
		if (!running) return;
		running = false;

		System.out.println("Se detuvo el game loop!");

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!thread.isAlive()) System.out.println("Se mato al subproceso " + thread.getName());
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

	private void setAssets() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMOB();
		aSetter.setInteractiveTile();
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

