package com.punkipunk.core;

/**
 * Clase principal del juego usando LWJGL. Reemplaza la estructura de JavaFX Application.
 */

public class MainGameLWJGL {

    /** Configuracion de la ventana. */
    private static final int WINDOW_WIDTH = 672;
    private static final int WINDOW_HEIGHT = 482;
    private static final String WINDOW_TITLE = "2D-RPG";
    private Window window;
    private GameLoop gameLoop;
    private boolean isInitialized = false;

    public void start() {
        init();
        gameLoop.run();
        cleanup();
    }

    public Window getWindow() {
        return window;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Inicializa todos los sistemas del juego.
     */
    private void init() {
        System.out.println("Starting game with LWJGL");

        // Crea y configura la ventana
        window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE);
        window.init();

        // Crea el game loop
        gameLoop = new GameLoop(window);
        gameLoop.init();

        isInitialized = true;
        System.out.println("Game started successfully!");
    }

    private void cleanup() {
        if (gameLoop != null) gameLoop.cleanup();
        if (window != null)  window.cleanup();
        isInitialized = false;
    }

}