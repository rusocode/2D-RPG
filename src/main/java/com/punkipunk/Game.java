package com.punkipunk;

import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.controllers.GameController;
import com.punkipunk.engine.core.GameLoop;
import com.punkipunk.engine.core.System;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;

import static com.punkipunk.utils.Global.*;

/**
 * <p>
 * The Game class uses a Canvas and implements the Runnable interface to manage the game logic, update and render.
 * <p>
 * A Canvas component represents a blank rectangular area of the screen on which the application can draw or from which the
 * application can catch user input events. To organize the Canvas memory, uses a double buffer (for this case).
 * <p>
 * Sequential ring buffering (i.e., double or triple buffering) is the most common; An application draws in a single
 * <i>back buffer</i> and then moves the content to the front (screen) in a single step by either copying the data or
 * moving the video pointer. When you move the video pointer, the buffers are swap, so the first buffer drawn becomes the <i>front
 * buffer</i>, or what is shown currently on the device; This is called <i>page flipping</i>.
 * <h3>BufferedStrategy</h3>
 * <p>
 * Uses "active paint". Gives you direct control over when something is painted
 * <ul>
 * <li>Provides better control over frame rate</li>
 * <li>Has a low-level connection and provides access to hardware acceleration.</li>
 * </ul>
 * <h3>Swing</h3>
 * <p>
 * Uses "passive paint". There is a {@code RepaintManager} that controls the scheduling of when and what is painted, this is
 * published to the Event Dispatching Thread and processed along with all other events.
 * <h3>Both</h3>
 * <p>
 * They are linked through the same rendering pipeline, usually this is DirectX or OpenGL.
 * <h3>Game Engines</h3>
 * <p>
 * Most game engines HAVE to start with a Canvas, as it is usually the only way to link Java with the underlying rendering engine.
 * Most game engines are low-level native bindings or lightweight wrappers over DirectX or OpenGL, giving you (in)direct access to
 * those APIs.
 * <p>
 * Some engines can still use Swing directly, but they would be limited and may be intended for situations in the ones that don't
 * need 3D or a high/consistent frame rate.
 * <h3>JavaFX</h3>
 * <p>
 * JavaFX maneja el doble buffer internamente por lo tanto los metodos {@code renderToTempScreen()} y {@code renderToScreen()} se
 * combinan en este metodo. Tampoco es necesario crear manualmente un {@code BufferStrategy}.
 * <p>
 * Links:
 * <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/rendering.html">Passive vs. Active Rendering</a>
 * <a href="https://www.oracle.com/java/technologies/painting.html">Painting in AWT and Swing</a>
 * <p>
 * TODO You could create a package that contains all the systems, audio, events, etc. for better organization
 */

// TODO Reformatear archivo Ctrl+Alt+Shift+L
// TODO Optimizar todas la importaciones Ctrl+Alt+O aunque parece que al utilizarr Ctrl+Shift+F lo hace tambien
public class Game {

    private final Scene scene;
    private final GameController gameController;
    private final Canvas canvas;
    private final GraphicsContext context;
    public System system;
    public int xOffset = WINDOW_WIDTH / 2 - (tile / 2);
    public int yOffset = WINDOW_HEIGHT / 2 - (tile * 2 / 2);
    private boolean running; // TODO Realmente es necesario?

    public Game(Scene scene, GameController gameController, Canvas canvas, GraphicsContext context) {
        this.scene = scene;
        this.gameController = gameController;
        this.canvas = canvas;
        this.context = context;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        new GameLoop(this::tick).start();
    }

    /**
     * Game cycle.
     */
    private void tick() {
        prepare();
        update();
        render();
    }

    private void prepare() {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void update() {
        system.updater.update();
    }

    private void render() {
        system.renderer.render(context);
    }

    /**
     * Reset the game.
     *
     * @param fullReset true to fully reset; false otherwise.
     */
    public void reset(boolean fullReset) {
        system.reset(fullReset);
        if (fullReset) playMusic(Assets.getAudio(AudioAssets.MUSIC_MAIN));
        else playAmbient(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
        // playMusic(fullReset ? Assets.getAudio(AudioAssets.MUSIC_MAIN) : Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
    }

    public void setup() {
        gameController.setGame(this);
    }

    // TODO tendrian que moverse a su sistema
    public void playMusic(URL url) {
        system.music.stop(); // Stop the previous clip
        system.music.play(url);
        system.music.loop();
    }

    public void playAmbient(URL url) {
        system.ambient.stop();
        system.ambient.play(url);
        system.ambient.loop();
    }

    public void playSound(URL url) {
        system.sound.play(url);
    }

    public void createSystem() {
        system = System.createDefault(this);
    }

    public void initializeSystem() {
        system.initialize();
    }

    public Scene getScene() {
        return scene;
    }

    public GameController getGameController() {
        return gameController;
    }

}
