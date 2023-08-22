package com.craivet;

import com.craivet.ai.AStar;
import com.craivet.utils.GameTimer;
import com.craivet.world.entity.item.ItemGenerator;
import com.craivet.gfx.Screen;
import com.craivet.io.File;
import com.craivet.input.Keyboard;
import com.craivet.physics.*;
import com.craivet.states.*;
import com.craivet.world.World;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.net.URL;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

/**
 * La clase Game utiliza un Canvas y implementa la interfaz Runnable para poder manejar la logica del juego, actualizar
 * y renderizar.
 * <p>
 * Un componente Canvas representa un area rectangular en blanco de la pantalla en la que la aplicacion puede dibujar o
 * desde la cual la aplicacion puede atrapar eventos de entrada del usuario. Para organizar la memoria del Canvas se
 * utiliza un doble buffer (para este caso).
 * <p>
 * El almacenamiento en buffer en anillo secuencial (es decir, el almacenamiento en buffer doble o triple) es el mas
 * comun; una aplicacion dibuja en un solo <i>back buffer</i> y luego mueve el contenido al frente (pantalla) en un solo
 * paso, ya sea copiando los datos o moviendo el puntero de video. Al mover el puntero de video, los bufferes se
 * intercambian, de modo que el primer buffer dibujado se convierte en el <i>front buffer</i>, o lo que se muestra
 * actualmente en el dispositivo; esto se llama <i>page flipping</i>.
 * <br><br>
 * <h3>BufferedStrategy</h3>
 * Utiliza "pintura activa". Te da control directo sobre cuando algo se pinta
 * <ul>
 *     <li>Proporciona un mejor control sobre la velocidad de fotogramas</li>
 *     <li>Tiene una conexión de bajo nivel y proporciona acceso a la aceleración de hardware.</li>
 * </ul>
 * <h3>Swing</h3>
 * Utiliza "pintura pasiva". Hay un {@code RepaintManager} que controla la programacion de cuando y que se pinta, esto
 * se publica en el Event Dispatching Thread (hilo de envio de eventos) y se procesa junto con todos los demas eventos.
 * <br><br>
 * <h3>Ambos</h3>
 * Estan vinculados a traves de la misma canalizacion de representacion, por lo general, esto es DirectX u OpenGL.
 * <br><br>
 * <h3>Game Engines</h3>
 * La mayoria de los motores de juegos TIENEN que comenzar con un Canvas, ya que generalmente es la unica forma de
 * vincular Java con el motor de renderizado subyacente. La mayoria de los motores de juegos son enlaces nativos de bajo
 * nivel o envolturas ligeras sobre DirectX u OpenGL, lo que le brinda acceso (in)directo a esas API.
 * <p>
 * Algunos motores aun pueden usar Swing directamente, pero serian limitados y pueden estar destinados a situaciones en
 * las que no necesita 3D o una velocidad de cuadros alta/consistente.
 * <br><br>
 * Recursos:
 * <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/rendering.html">Passive vs. Active Rendering</a>
 * <a href="https://www.oracle.com/java/technologies/painting.html">Painting in AWT and Swing</a>
 * <p>
 * TODO Se podria crear un paquete que contenga todos los sistemas, de audio, eventos, etc. para mejor organizacion
 */

public class Game extends Canvas implements Runnable {

    // Systems
    public Keyboard keyboard = new Keyboard(this);
    public World world = new World(this);
    public UI ui = new UI(this, world);
    public ItemGenerator itemGenerator = new ItemGenerator(this, world);
    public Minimap minimap = new Minimap(world);
    public Audio sound = new Audio();
    public Audio music = new Audio();
    public File file = new File(this, world);
    public Collision collision = new Collision(world);
    public CollisionEvent event = new CollisionEvent(this, world);
    public AStar aStar = new AStar(world);
    public GameTimer gameTimer = new GameTimer();

    // States
    public StateManager stateManager = new StateManager();
    public int state;

    // Screen
    private final Screen screen;
    private BufferedImage tempScreen;
    private BufferStrategy buffer; // Es como una pantalla oculta que se usa para de evitar parpadedos
    private Graphics2D g2;

    // Otros
    private boolean running;

    public Game() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(keyboard);
        // setIgnoreRepaint(true);
        screen = new Screen(this, false);
    }

    @Override
    public void run() {
        init();
        while (isRunning()) {
            if (gameTimer.shouldUpdate()) update();
            if (gameTimer.shouldRender()) render();
            gameTimer.timer(1000);
        }
    }

    private void init() {
        file.loadConfig();
        file.loadTiles();
        file.loadMaps();
        minimap.createMinimap();
        event.createEvents();
        playMusic(music_main);
        stateManager.set(new GameState(this, world, ui, minimap));

        // Crea una pantalla temporal para el fullscreen
        tempScreen = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        // Utiliza el "pincel" (g2) de la pantalla temporal
        g2 = (Graphics2D) tempScreen.getGraphics();

    }

    private void update() {
        if (stateManager.get() != null) stateManager.get().update();
    }

    private void render2() {
        Graphics2D g2 = (Graphics2D) buffer.getDrawGraphics();
        g2.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        // Renderiza los graficos en pantalla
        if (stateManager.get() != null) stateManager.get().render(g2);
        // Cuando haya terminado de dibujar y desee presentar su informacion en la pantalla, llame a show()
        buffer.show();
        g2.dispose();
    }

    private void render() {
        drawToTempScreen();
        drawToScreen();
    }

    /**
     * Dibuja los graficos en la pantalla temporal.
     */
    private void drawToTempScreen() {
        // Limpia la ventana usando el color de fondo actual
        g2.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        // Renderiza los graficos en el buffer de la pantalla temporal
        if (stateManager.get() != null) stateManager.get().render(g2);
        // Muestra el buffer
        buffer.show();
    }

    /**
     * Dibuja la pantalla temporal en el Canvas utilizando el ancho y alto especificado de la ventana.
     */
    private void drawToScreen() {
        // Obtienen los graficos del buffer actual
        Graphics g = buffer.getDrawGraphics();
        // Renderiza la pantalla temporal en el Canvas
        g.drawImage(tempScreen, 0, 0, screen.getWidth(), screen.getHeight(), null);
        // Elimina este contexto de graficos y libera cualquier recurso del sistema que este utilizando
        g.dispose();
    }

    private synchronized boolean isRunning() {
        return running;
    }

    public synchronized void start() {
        if (running) return;
        /* Crear una estrategia de doble buffer para este componente (Canvas). Tenga en cuenta que cuanto mas alto sea,
         * mas potencia de procesamiento sera necesaria. */
        createBufferStrategy(2);
        // Obtiene el buffer del Canvas
        buffer = getBufferStrategy();
        running = true;
        new Thread(this).start();
    }

    /**
     * Reinicia el juego.
     *
     * @param restart vuelve a establecer los valores por defecto del player.
     */
    public void reset(boolean restart) {
        stopMusic();
        ui.message.clear();
        world.player.setDefaultPos();
        world.player.restoreStatus();
        world.player.timer.resetCounter();
        world.createMOBs();
        if (restart) {
            playMusic(music_main);
            world.player.setDefaultValues();
            world.createItems();
            world.createInteractiveTile();
            world.environment.lighting.resetDay();
            minimap.minimapOn = false;
        }
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

}

