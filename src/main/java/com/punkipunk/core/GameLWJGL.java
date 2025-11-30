package com.punkipunk.core;

import com.punkipunk.audio.AudioID;
import com.punkipunk.controllers.GameController;
import com.punkipunk.ui.GameUI;
import com.punkipunk.utils.Utils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Clase Game adaptada para LWJGL. Mantiene toda la logica original pero sin heredar de AnimationTimer.
 * <p>
 * NOTA: Por ahora mantenemos Canvas y GraphicsContext de JavaFX para minimizar cambios.
 * <p>
 * El Canvas renderiza en memoria y luego se copia a una textura OpenGL para visualizacion.
 */

public class GameLWJGL implements IGame {

    private final Window window;
    private final Canvas canvas;
    private final GraphicsContext context;

    // Sistema de transferencia Canvas -> OpenGL
    private final CanvasToTexture canvasToTexture;
    private final GameUI gameUI;
    public GameSystem gameSystem;
    public GameController gameController; // Por ahora null, lo migraremos despues
    private boolean paused, running;
    private int frames, fps;
    private long lastTime = System.nanoTime();

    public GameLWJGL(Window window) {
        this.window = window;

        // Crea Canvas temporal de JavaFX
        this.canvas = new Canvas(window.getWidth(), window.getHeight());
        this.context = canvas.getGraphicsContext2D();

        // Crear el puente Canvas -> OpenGL
        this.canvasToTexture = new CanvasToTexture(canvas);

        gameUI = new GameUI();
        gameUI.init(window.getWindowHandle(), this);

        System.out.println("GameLWJGL created (using JavaFX temporary Canvas with OpenGL bridge)");
    }

    /**
     * Inicializa todos los sistemas del juego. Equivalente al metodo setup() original.
     */
    public void setup() {
        // IMPORTANTE: Ejecuta la inicializacion en el FX Thread porque algunos sistemas (como Lighting) usan operaciones JavaFX
        JavaFXHelper.runAndWait(() -> {
            try {
                // Crea el GameSystem
                gameSystem = GameSystem.createDefault(this);

                // Reproduce la musica inicial
                gameSystem.audio.playMusic(AudioID.Music.MAIN);

                // Configura la fuente
                context.setFont(Utils.loadFont("font/BlackPearl.ttf", 18));
                context.setFill(Color.WHITE);

                // Inicializa los sistemas
                gameSystem.initialize();

            } catch (Exception e) {
                System.err.println("Error configuring Game: " + e.getMessage());
            }
        });


        // TODO: inicializa el GameController cuando se migre
        // if (gameController != null) gameController.initialize(this);

        running = true;
        System.out.println("Game configured correctly!");
    }

    public void update(double deltaTime) {
        if (!paused && running) gameSystem.updater.update(); // Actualiza todos los sistemas a traves del updater
    }

    public void render() {
        if (!running) return;

        // PASO 1 y 2: Renderiza y captura el Canvas (ambos en JavaFX Thread)
        JavaFXHelper.runAndWait(() -> {
            // 1a. Prepara el canvas (limpia el frame anterior)
            prepare();

            // 1b. Renderiza todo a traves del renderer
            gameSystem.renderer.render(context);

            // 2. Captura los pixeles del Canvas
            canvasToTexture.captureCanvas();
        });

        // PASO 3: Actualiza la textura OpenGL (en el thread actual = OpenGL Thread)
        canvasToTexture.updateTexture();

        // PASO 4: Renderiza la textura en la ventana LWJGL
        canvasToTexture.render();

        // PASO 5: Renderiza la UI sobre el canvas del juego
        gameUI.render(window.getWidth(), window.getHeight());

        // Actualiza los FPS
        updateFPS();

    }

    public void pause() {
        if (!paused) paused = true;
    }

    public void play() {
        if (paused) paused = false;
    }

    public void reset(boolean fullReset) {
        if (gameSystem != null) gameSystem.reset(fullReset);
    }

    public void stop() {
        running = false;
        paused = false;
        if (gameSystem != null && gameSystem.audio != null) gameSystem.audio.shutdown();
    }

    /**
     * Limpia todos los recursos.
     */
    public void cleanup() {
        stop(); // TODO Hace falta?

        System.out.println("Cleaning game resources...");

        if (gameUI != null) gameUI.cleanup();

        // if (gameSystem != null) gameSystem.audio.stopAll();

        // Limpia el puente Canvas -> OpenGL
        if (canvasToTexture != null) canvasToTexture.cleanup();

        // Aqui puedes agregar limpieza de otros recursos si es necesario
        // Por ejemplo: gameSystem.cleanup() si lo implementas en el futuro

        running = false;
        System.out.println("Game cleaned");

    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public GameSystem getGameSystem() {
        return gameSystem;
    }

    @Override
    public GraphicsContext getContext() {
        return context;
    }

    @Override
    public Scene getScene() {
        return null; // GameLWJGL no usa JavaFX Scene
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isRunning() {
        return running;
    }

    public int getFPS() {
        return fps;
    }

    public GameUI getGameUI() {
        return gameUI;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Actualiza el contador de FPS.
     */
    private void updateFPS() {
        frames++;
        long now = System.nanoTime();
        if (now - lastTime >= 1_000_000_000) {
            fps = frames;
            frames = 0;
            lastTime = now;
            window.setTitle("2D-RPG - FPS: " + fps);
        }
    }

    /**
     * Limpia el canvas antes de renderizar.
     */
    private void prepare() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // TODO Uso este?
    }

}