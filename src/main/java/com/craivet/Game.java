package com.craivet;

import com.craivet.ai.AStar;
import com.craivet.entity.Player;
import com.craivet.gfx.Display;
import com.craivet.input.KeyManager;
import com.craivet.states.GameState;
import com.craivet.states.StateManager;
import com.craivet.utils.TimeUtils;

import java.awt.*;
import java.awt.image.BufferStrategy;
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

public class Game extends Canvas implements Runnable {

	// System
	private Thread thread;
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

	// Game state
	public StateManager stateManager = new StateManager();
	public GameState gState;
	public int gameState;
	private boolean running;

	public Graphics2D g2; // Pincel
	public boolean fullScreen;

	public Game() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setFocusable(true);
		addKeyListener(key);
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
				render();
			}

			if (timer >= 1E9) {
				System.out.println(ticks + " ticks, " + frames + " fps");
				timer = 0;
				ticks = 0;
				frames = 0;
			}
		}

		stop();

	}

	private void init() {
		setAssets();

		playMusic(music_blue_boy_adventure);
		gameState = PLAY_STATE;

		// Crea la ventana y agrega el Canvas
		new Display(this);

		gState = new GameState(world);
		stateManager.setState(gState);

	}

	private void update() {
		if (stateManager.getState() != null) stateManager.getState().update();
	}

	private void render() {
		// Obtiene el buffer del Canvas
		BufferStrategy buffer = getBufferStrategy();
		/* Crea 3 buffers para el Canvas en caso de que no haya uno. Tenga en cuenta que cuanto mas alto vaya, mas
		 * potencia de procesamiento necesitara, por lo que 10k seria un buffer muy malo. */
		if (buffer == null) {
			createBufferStrategy(3);
			return;
		}
		// Obtiene el pincel a partir del buffer
		g2 = (Graphics2D) buffer.getDrawGraphics();
		// Limpia la ventana usando el color de fondo actual
		g2.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		// Dibuja las imagenes en pantalla
		if (stateManager.getState() != null) stateManager.getState().render(g2);
		// Hace visible el buffer
		buffer.show();
		// Elimina este contexto de graficos y libera cualquier recurso del sistema que este utilizando
		g2.dispose();
	}

	public synchronized boolean isRunning() {
		return running;
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

