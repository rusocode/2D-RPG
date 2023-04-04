package com.craivet;

import com.craivet.ai.AStar;
import com.craivet.entity.Player;
import com.craivet.input.KeyManager;
import com.craivet.states.GameState;
import com.craivet.states.StateManager;
import com.craivet.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;
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
	public Collider collider = new Collider(this, world);
	public Config config = new Config(this);
	public AStar aStar = new AStar(world);
	public UI ui = new UI(this, world);
	public KeyManager key = new KeyManager(this, world);
	public Player player = new Player(this, world);

	public StateManager stateManager = new StateManager();
	public GameState gState;
	public int gameState;
	private boolean running;

	public boolean fullScreen;

	public Game() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setBackground(Color.black);
		setDoubleBuffered(true); // Mejora el rendimiento de representacion (es algo parecido al metodo getBufferStrategy() de Canvas)
		// addKeyListener(key);
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
				// renderToTempScreen();
				// renderToScreen();
				repaint();
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

		playMusic(music_blue_boy_adventure);
		gameState = PLAY_STATE;

		addKeyListener(key);

		gState = new GameState(world);
		stateManager.setState(gState);

		/* Hasta ahora dibujamos todo directamente en el JPanel. Pero esta vez seguimos dos pasos:
		 * 1. Dibujamos todo en la pantalla temporal para la imagen que esta detras de escena.
		 * 2. Dibujamos la pantalla temporal en el JPanel.
		 * La razon de esto, es que queremos mostrar el juego en pantalla completa o en otras resoluciones diferentes.
		 * Eso significa que debemos cambiar el tamaña de todas las imagenes (tiles, entidades, ui, etc.). Y si
		 * cambiambos el tamaño de cada objeto uno por uno, es mucho trabajo y poco eficiente. Asi que primero dibujamos
		 * todo en una unica imagen almacenada en buffer y luego cambiamos el tamaño de esta imagen grande para que se
		 * ajuste a nuestra pantalla completa en cada bucle. */

	}

	public void update() {

		if (stateManager.getState() != null) stateManager.getState().update();

		/* if (gameState == PLAY_STATE) {
			player.update();
			for (int i = 0; i < npcs[1].length; i++)
				if (npcs[map][i] != null) npcs[map][i].update();
			for (int i = 0; i < mobs[1].length; i++) {
				if (mobs[map][i] != null) {
					// Cuando muere el mob, primero establece el estado dead a true evitando que siga moviendose. Luego
					// genera la animacion de muerte y al finalizarla, establece alive en false para que no genere
					// movimiento y elimine el objeto.
					if (mobs[map][i].alive && !mobs[map][i].dead) mobs[map][i].update();
					if (!mobs[map][i].alive) {
						mobs[map][i].checkDrop();
						mobs[map][i] = null;
					}
				}
			}
			for (int i = 0; i < projectiles[1].length; i++) {
				if (projectiles[map][i] != null) {
					if (projectiles[map][i].alive) projectiles[map][i].update();
					if (!projectiles[map][i].alive) projectiles[map][i] = null;
				}
			}
			for (int i = 0; i < particles.size(); i++) {
				if (particles.get(i) != null) {
					if (particles.get(i).alive) particles.get(i).update();
					if (!particles.get(i).alive) particles.remove(i);
				}
			}
			for (int i = 0; i < iTile[1].length; i++)
				if (iTile[map][i] != null) iTile[map][i].update();
		} */

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (stateManager.getState() != null) stateManager.getState().render(g2);
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

	/* public void retry() {
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
	} */

}

