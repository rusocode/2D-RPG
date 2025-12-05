package com.punkipunk.core;

import com.punkipunk.audio.AudioID;
import com.punkipunk.controllers.GameController;
import com.punkipunk.gfx.Renderer2D;
import com.punkipunk.ui.GameUI;
import javafx.scene.Scene;

/**
 * Clase Game adaptada para LWJGL. Mantiene toda la logica original pero sin heredar de AnimationTimer.
 * <p>
 * ACTUALIZADO: Eliminada toda dependencia de JavaFX (Canvas, GraphicsContext, JavaFXHelper, CanvasToTexture). Ahora usa
 * Renderer2D puro de OpenGL.
 */

public class GameLWJGL implements IGame {

    private final Window window;
    private final Renderer2D renderer;
    private final GameUI gameUI;

    public GameSystem gameSystem;
    public GameController gameController;

    private boolean paused, running;
    private int frames, fps;
    private long lastTime = System.nanoTime();

    public GameLWJGL(Window window) {
        this.window = window;

        // Crea el renderer OpenGL 2D nativo
        this.renderer = new Renderer2D(window.getWidth(), window.getHeight());

        gameUI = new GameUI();
        gameUI.init(window.getWindowHandle(), this);

        System.out.println("GameLWJGL created (using native OpenGL rendering)");
    }

    /**
     * Inicializa todos los sistemas del juego. Equivalente al metodo setup() original.
     */
    public void setup() {
        try {
            // Crea el GameSystem
            gameSystem = GameSystem.createDefault(this);

            // Reproduce la musica inicial
            gameSystem.audio.playMusic(AudioID.Music.MAIN);

            // Inicializa los sistemas
            gameSystem.initialize();

            // TODO: Configurar fuente cuando se implemente el sistema de fuentes
            // Por ahora, el renderizado de texto no esta disponible

        } catch (Exception e) {
            System.err.println("Error configuring Game: " + e.getMessage());
        }

        running = true;
        System.out.println("Game configured correctly!");
    }

    public void update(double deltaTime) {
        if (!paused && running) gameSystem.updater.update();
    }

    public void render() {
        if (!running) return;

        // 1. Inicia el frame de renderizado
        renderer.begin();

        // 2. Limpia la pantalla
        prepare();

        // 3. Renderiza el juego a traves del renderer
        gameSystem.renderer.render(renderer);

        // 4. Finaliza el renderizado (frame y flush)
        renderer.end();

        // 5. Renderiza la UI (ImGui) sobre el juego
        gameUI.render(window.getWidth(), window.getHeight());

        // Actualiza FPS
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
        stop();

        System.out.println("Cleaning game resources...");

        if (gameUI != null) gameUI.cleanup();
        if (renderer != null) renderer.cleanup();

        running = false;
        System.out.println("Game cleaned");

    }

    @Override
    public GameSystem getGameSystem() {
        return gameSystem;
    }

    @Override
    public Renderer2D getRenderer() {
        return renderer;
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
     * Maneja cambios de tamaño de ventana.
     */
    public void onResize(int width, int height) {
        renderer.resize(width, height);
    }

    private void prepare() {
        // Limpia la pantalla con color negro
        renderer.clear(com.punkipunk.gfx.opengl.Color.BLACK);
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

}