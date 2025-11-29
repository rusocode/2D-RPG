package com.punkipunk.core;

import com.punkipunk.audio.AudioID;
import com.punkipunk.controllers.GameController;
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
 * El Canvas renderiza en memoria y luego se copia a una textura OpenGL para visualización.
 */

public class GameLWJGL implements IGame {

    private final Window window;
    private final Canvas canvas;
    private final GraphicsContext context;

    // Sistema de transferencia Canvas -> OpenGL
    private final CanvasToTexture canvasToTexture;

    public GameSystem gameSystem;
    public GameController gameController; // Por ahora null, lo migraremos despues

    private boolean paused, running;

    private int frames, fps;
    private long lastTime = System.nanoTime();

    public GameLWJGL(Window window) {
        this.window = window;

        // Crear Canvas temporal de JavaFX
        this.canvas = new Canvas(window.getWidth(), window.getHeight());
        this.context = canvas.getGraphicsContext2D();

        // Crear el puente Canvas -> OpenGL
        this.canvasToTexture = new CanvasToTexture(canvas);

        System.out.println("GameLWJGL creado (usando Canvas temporal de JavaFX con puente a OpenGL)");
    }

    /**
     * Inicializa todos los sistemas del juego. Equivalente al metodo setup() original.
     */
    public void setup() {
        System.out.println("=== Configurando Game ===");

        // IMPORTANTE: Ejecuta la inicializacion en el FX Thread porque algunos sistemas (como Lighting) usan operaciones JavaFX
        JavaFXHelper.runAndWait(() -> {
            try {
                // Crea el GameSystem exactamente como en tu codigo original
                gameSystem = GameSystem.createDefault(this);

                // Reproducir música inicial
                gameSystem.audio.playMusic(AudioID.Music.MAIN);

                // Configurar fuente (como en tu código original)
                context.setFont(Utils.loadFont("font/BlackPearl.ttf", 18));
                context.setFill(Color.WHITE);

                // Inicializar sistemas
                gameSystem.initialize();

                System.out.println("=== Game configurado correctamente ===");

            } catch (Exception e) {
                System.err.println("Error configurando Game: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        });


        // TODO: Inicializa el GameController cuando lo migremos
        // if (gameController != null) gameController.initialize(this);

        running = true;
        System.out.println("=== Game configurado correctamente ===");
    }

    /**
     * Actualiza la logica del juego. Se llama desde el GameLoop.
     */
    public void update(double deltaTime) {
        if (!paused && running) gameSystem.updater.update(); // Actualiza todos los sistemas a traves del updater
    }

    /**
     * Renderiza el juego. Se llama desde el GameLoop.
     */
    public void render() {
        if (!running) return;

        // PASO 1 y 2: Renderizar y capturar Canvas (ambos en JavaFX Thread)
        JavaFXHelper.runAndWait(() -> {
            // 1a. Prepara el canvas (limpia el frame anterior)
            prepare();

            // 1b. Renderiza todo a través del renderer
            gameSystem.renderer.render(context);

            // 2. Captura los píxeles del Canvas
            canvasToTexture.captureCanvas();
        });

        // PASO 3: Actualizar la textura OpenGL (en el thread actual = OpenGL Thread)
        canvasToTexture.updateTexture();

        // PASO 4: Renderizar la textura en la ventana LWJGL
        canvasToTexture.render();

        // Actualiza los FPS
        updateFPS();

    }

    /**
     * Pausa el juego.
     */
    public void pause() {
        if (!paused) {
            paused = true;
            System.out.println("Juego pausado");
        }
    }

    /**
     * Reanuda el juego.
     */
    public void play() {
        if (paused) {
            paused = false;
            System.out.println("Juego reanudado");
        }
    }

    /**
     * Reinicia el juego.
     */
    public void reset(boolean fullReset) {
        if (gameSystem != null) {
            gameSystem.reset(fullReset);
            System.out.println("Juego reiniciado (fullReset: " + fullReset + ")");
        }
    }

    /**
     * Detiene el juego y libera recursos.
     */
    public void stop() {
        System.out.println("Deteniendo Game...");
        running = false;
        paused = false;
        if (gameSystem != null && gameSystem.audio != null) gameSystem.audio.shutdown();
        System.out.println("Game detenido");
    }

    /**
     * Limpia todos los recursos.
     */
    public void cleanup() {
        stop();

        System.out.println("Limpiando recursos del juego...");

        // Limpia el puente Canvas -> OpenGL
        canvasToTexture.cleanup();

        // Aquí puedes agregar limpieza de otros recursos si es necesario
        // Por ejemplo: gameSystem.cleanup() si lo implementas en el futuro

        running = false;
        System.out.println("Recursos del juego liberados");

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
            // Muestra los FPS en el título de la ventana
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