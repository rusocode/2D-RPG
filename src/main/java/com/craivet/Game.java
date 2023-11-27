package com.craivet;

import com.craivet.ai.AStar;
import com.craivet.physics.Event;
import com.craivet.utils.Loop;
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
 * The Game class uses a Canvas and implements the Runnable interface to manage the game logic, update and render.
 * <p>
 * A Canvas component represents a blank rectangular area of the screen on which the application can draw or from which
 * the application can catch user input events. To organize the Canvas memory, uses a double buffer (for this case).
 * <p>
 * Sequential ring buffering (i.e., double or triple buffering) is the most common; An application draws in a single
 * <i>back buffer</i> and then moves the content to the front (screen) in a single step by either copying the data or
 * moving the video pointer. When you move the video pointer, the buffers are swap, so the first buffer drawn becomes
 * the <i>front buffer</i>, or what is shown currently on the device; This is called <i>page flipping</i>.
 * <br><br>
 * <h3>BufferedStrategy</h3>
 * Uses "active paint". Gives you direct control over when something is painted
 * <ul>
 * <li>Provides better control over frame rate</li>
 * <li>Has a low-level connection and provides access to hardware acceleration.</li>
 * </ul>
 * <h3>Swing</h3>
 * Uses "passive paint". There is a {@code RepaintManager} that controls the scheduling of when and what is painted,
 * this is published to the Event Dispatching Thread and processed along with all other events.
 * <br><br>
 * <h3>Both</h3>
 * They are linked through the same rendering pipeline, usually this is DirectX or OpenGL.
 * <br><br>
 * <h3>Game Engines</h3>
 * Most game engines HAVE to start with a Canvas, as it is usually the only way to link Java with the underlying
 * rendering engine. Most game engines are low-level native bindings or lightweight wrappers over DirectX or OpenGL,
 * giving you (in)direct access to those APIs.
 * <p>
 * Some engines can still use Swing directly, but they would be limited and may be intended for situations in the ones
 * that don't need 3D or a high/consistent frame rate.
 * <br><br>
 * Resources:
 * <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/rendering.html">Passive vs. Active Rendering</a>
 * <a href="https://www.oracle.com/java/technologies/painting.html">Painting in AWT and Swing</a>
 * <p>
 * TODO You could create a package that contains all the systems, audio, events, etc. for better organization
 */

public class Game extends Canvas implements Runnable {

    // Systems
    public Keyboard keyboard = new Keyboard(this);
    public World world = new World(this);
    public UI ui = new UI(this, world);
    public ItemGenerator itemGenerator = new ItemGenerator(this, world); // TODO Here?
    public Minimap minimap = new Minimap(this, world);
    public Audio sound = new Audio();
    public Audio music = new Audio();
    public File file = new File(this, world);
    public Collision collision = new Collision(world);
    public Event event = new Event(this, world);
    public AStar aStar = new AStar(world);
    public Loop loop = new Loop();

    // States
    public StateManager stateManager = new StateManager();
    public int state = MAIN_STATE;

    // Screen
    private final Screen screen;
    private BufferedImage tempScreen;
    private BufferStrategy buffer; // It is like a hidden screen that is used to avoid blinking
    private Graphics2D g2;

    // Others
    private boolean running;

    public Game() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
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
            if (loop.shouldUpdate()) update();
            if (loop.shouldRender()) render();
            loop.timer(1000);
        }
    }

    private void init() {
        file.load();
        minimap.create();
        event.create();
        playMusic(music_main);
        stateManager.set(new GameState(this, world, ui, minimap));
        // Create a temporary screen for the fullscreen
        tempScreen = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        // Use the "brush" (g2) on the temporary screen
        g2 = (Graphics2D) tempScreen.getGraphics();
    }

    private void update() {
        if (stateManager.get() != null) stateManager.get().update();
    }

    private void render() {
        renderToTempScreen();
        renderToScreen();
    }

    /**
     * Renders the graphics on the temporary screen.
     */
    private void renderToTempScreen() {
        // Clear the window using the current background color
        g2.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        // Render graphics in the temporary screen buffer
        if (stateManager.get() != null) stateManager.get().render(g2);
        // When you're done drawing and want to present your information on the temporary screen, call show() show the buffer
        buffer.show();
    }

    /**
     * Renders the temporary screen to the Canvas using the specified width and height of the window.
     */
    private void renderToScreen() {
        // Get the graphics from the current buffer
        Graphics g = buffer.getDrawGraphics();
        // Render the temporary screen on the Canvas
        g.drawImage(tempScreen, 0, 0, screen.getWidth(), screen.getHeight(), null);
        // Delete this graphics context and free any system resources it is using
        g.dispose();
    }

    private synchronized boolean isRunning() {
        return running;
    }

    /**
     * Start the game.
     */
    public synchronized void start() {
        if (running) return;
        /* Create a double buffer strategy for this component (Canvas). Keep in mind that the higher it is, the more
         * processing power will be needed. */
        createBufferStrategy(2);
        // Gets the Canvas buffer
        buffer = getBufferStrategy();
        running = true;
        new Thread(this).start();
    }

    /**
     * Reset the game.
     *
     * @param fullReset true to fully reset; false otherwise.
     */
    public void reset(boolean fullReset) {
        ui.console.clear();
        world.entities.player.reset(fullReset);
        world.entities.factory.createMobs();
        world.entities.removeTempEntities();
        world.entities.player.bossBattleOn = false;
        playMusic(ambient_overworld);
        if (fullReset) {
            playMusic(music_main);
            world.entities.factory.createEntities();
            world.environment.lighting.resetDay();
            keyboard.minimap = false;
        }
    }

    public void playMusic(URL url) {
        music.stop(); // Stop the previous clip
        music.play(url);
        music.loop();
    }

    public void playSound(URL url) {
        sound.play(url);
    }

}

