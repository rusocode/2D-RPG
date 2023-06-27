package com.craivet;

import com.craivet.ai.AStar;
import com.craivet.data.SaveLoad;
import com.craivet.entity.Entity;
import com.craivet.input.Key;
import com.craivet.states.GameState;
import com.craivet.states.StateManager;
import com.craivet.tile.Minimap;
import com.craivet.utils.TimeUtils;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.net.URL;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

/**
 * TODO Se podria crear un paquete que contenga todos los sistemas, de audio, eventos, etc. para mejor organizacion
 */

public class Game extends Canvas implements Runnable {

    // TODO Estos objetos los creo aca o en el constructor?
    // System
    public Key key = new Key(this);
    private final World world = new World(this);
    public Entity entity = new Entity(this, world);
    public UI ui = new UI(this, world);
    public Minimap minimap = new Minimap(world);
    public SaveLoad saveLoad = new SaveLoad(this, world); // TODO Nombre File?
    public Audio sound = new Audio();
    public Audio music = new Audio();
    public EventManager event = new EventManager(this, world);
    public Collider collider = new Collider(world);
    public Config config = new Config(this);
    public AStar aStar = new AStar(world);

    // States
    public StateManager stateManager = new StateManager();
    public int state;

    // Otros
    private BufferStrategy buffer;
    public int framesInRender;
    public boolean showFPS;
    private boolean running;

    public Game() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(key);
    }

    /**
     * Desde aca se bombea toda la sangre.
     */
    @Override
    public void run() {

        init();

        int ticks = 0, framesInConsole = 0;
        double timePerTick = 1E9 / TICKS; // Tiempo fijo entre cada tick
        double timePerFrame = 1E9 / MAX_FPS;
        double delta, timer = 0, unprocessedTime = 0;
        long lastTick = TimeUtils.nanoTime();
        long lastRender = TimeUtils.nanoTime();

        while (isRunning()) {
            long now = TimeUtils.nanoTime();
            delta = now - lastTick; // Diferencia entre el primer y ultimo tick
            unprocessedTime += delta; // Acumulador del tiempo delta
            timer += now - lastTick;
            lastTick = now;
            /* Actualiza la fisica cuando haya tiempo sin procesar. De esta manera, la fisica se actualiza a la misma
             * velocidad independientemente de la computadora. */
            if (unprocessedTime >= timePerTick) {
                update();
                ticks++;
                unprocessedTime -= timePerTick;
            }

            /* Renderiza los graficos cuando este activada la opcion FPS_UNLIMITED o cuando el ciclo alcanze el tiempo
             * entre cada frame especificado. */
            if (FPS_UNLIMITED || now - lastRender >= timePerFrame) {
                lastRender = TimeUtils.nanoTime();
                render();
                framesInConsole++;
            }

            if (timer >= 1E9) {
                System.out.println(ticks + " ticks, " + framesInConsole + " fps");
                timer = 0;
                ticks = 0;
                framesInRender = framesInConsole;
                framesInConsole = 0;
                showFPS = true;
            }

        }

    }

    private void init() {
        world.createEntities();
        playMusic(music_main);
        stateManager.setState(new GameState(this, world, ui, minimap));
    }

    private void update() {
        if (stateManager.getState() != null) stateManager.getState().update();
    }

    private void render() {
        // Obtiene un nuevo contexto de graficos en cada iteracion para asegurarse de que la estrategia este validada
        Graphics2D g2 = (Graphics2D) buffer.getDrawGraphics(); // Pincel
        // Limpia la ventana usando el color de fondo actual
        g2.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        // Renderiza los graficos en pantalla
        if (stateManager.getState() != null) stateManager.getState().render(g2);
        // Hace visible el buffer
        buffer.show();
        // Elimina este contexto de graficos y libera cualquier recurso del sistema que este utilizando
        g2.dispose();
        // TODO Primero se muestra el buffer o se elimina el contexto grafico? https://docs.oracle.com/javase/8/docs/api/index.html
    }

    private synchronized boolean isRunning() {
        return running;
    }

    public synchronized void start() {
        if (running) return;
        /* Crear una estrategia general de triple buffer. Tenga en cuenta que cuanto mas alto sea, mas potencia de
         * procesamiento necesitara. */
        createBufferStrategy(3);
        // Obtiene el buffer del Canvas
        buffer = getBufferStrategy();
        running = true;
        new Thread(this).start();
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

    /**
     * Reinicia el juego.
     *
     * @param restart vuelve a establecer los valores por defecto del player.
     */
    public void resetGame(boolean restart) {
        stopMusic();
        ui.message.clear();
        world.player.setDefaultPos();
        world.player.restoreStatus();
        world.player.timer.resetCounter();
        world.createNPCs();
        world.createMOBs();
        if (restart) {
            playMusic(music_main);
            world.player.setDefaultValues();
            world.createItems();
            world.createInteractiveTile();
            world.environment.lighting.resetDay();
            minimap.miniMapOn = false;
        }
    }

    public World getWorld() {
        return world;
    }

    public Key getKey() {
        return key;
    }

}

