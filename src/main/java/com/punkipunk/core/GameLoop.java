package com.punkipunk.core;

import com.punkipunk.states.State;
import com.punkipunk.ui.MainMenuUI;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Game Loop principal que maneja la actualizacion y renderizado del juego. Reemplaza el AnimationTimer de JavaFX.
 */

public class GameLoop {

    /** Target FPS (0 = ilimitado, controlado por v-sync). */
    private static final double TARGET_FPS = 60.0;
    private static final double TARGET_FRAME_TIME = 1.0 / TARGET_FPS;
    private final Window window;
    private final MainMenuUI mainMenu;
    /** Control de tiempo. */
    private double lastTime, deltaTime;
    /** FPS y estadisticas. */
    private int fps, fpsCounter;
    private double fpsTimer;
    /** Estado del juego. */
    private GameState currentState = GameState.MAIN_MENU;

    private GameLWJGL game;

    public GameLoop(Window window) {
        this.window = window;
        this.mainMenu = new MainMenuUI();
    }

    /**
     * Inicializa el game loop.
     */
    public void init() {
        lastTime = glfwGetTime();
        // Inicializa UI
        mainMenu.init(window.getWindowHandle());
        System.out.println("GameLoop inicializado - Target FPS: " + TARGET_FPS);
    }

    /**
     * Loop principal del juego.
     */
    public void run() {
        System.out.println("Starting Game Loop...");

        while (!window.shouldClose()) {
            double currentTime = glfwGetTime();
            deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            // Actualiza estadisticas de FPS
            updateFPS(deltaTime);

            // Procesa acciones del menu
            processMenuActions();

            // Actualiza logica del juego
            update(deltaTime);

            // Renderiza
            render();

            // Actualiza la ventana (swap buffers y poll events)
            window.update();
        }

        System.out.println("Game Loop completed!");
    }

    /**
     * Limpia los recursos del game loop.
     */
    public void cleanup() {
        if (game != null) game.cleanup();
        mainMenu.cleanup();
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public int getFPS() {
        return fps;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Procesa las acciones del menu principal.
     */
    private void processMenuActions() {
        if (currentState == GameState.MAIN_MENU) {
            MainMenuUI.MenuAction action = mainMenu.getCurrentAction();
            switch (action) {
                case NEW_GAME:
                    startNewGame();
                    break;
                case LOAD_GAME:
                    loadGame();
                    break;
                case QUIT:
                    glfwSetWindowShouldClose(window.getWindowHandle(), true);
                    break;
                case NONE:
                    break;
            }
        }
    }

    /**
     * Inicia el juego.
     */
    private void startNewGame() {
        System.out.println("Starting a new game");

        game = new GameLWJGL(window);
        game.setup();

        window.initInput(game);

        currentState = GameState.PLAYING;

        State.setState(State.PLAY);

        // TODO: Inicializar el juego
    }

    /**
     * Carga el juego guardado.
     */
    private void loadGame() {
        System.out.println("Loading game");
        // TODO: Implementar carga de juego
    }

    /**
     * Actualiza la logica del juego.
     */
    private void update(double delta) {
        switch (currentState) {
            case MAIN_MENU:
                // El menu no necesita actualizacion de logica
                break;
            case PLAYING:
                if (game != null) game.update(delta);
                // TODO: Aqui ira la logica de actualizacion del juego
                // - Input handling
                // - Physics
                // - AI
                // - Animations
                // etc.
                break;
            case PAUSED:
                break;
        }
    }

    /**
     * Renderiza el juego.
     */
    private void render() {
        // Limpia el framebuffer
        window.clear();
        switch (currentState) {
            case MAIN_MENU:
                mainMenu.render(window.getWidth(), window.getHeight());
                break;
            case PLAYING:
                if (game != null) game.render();
                // TODO: Aqui ira el codigo de renderizado del juego
                // - Renderizar mundo
                // - Renderizar entidades
                // - Renderizar UI
                // etc.
                break;
            case PAUSED:
                if (game != null) game.render();
                break;
        }
    }

    /**
     * Actualiza el contador de FPS.
     */
    private void updateFPS(double delta) {
        fpsCounter++;
        fpsTimer += delta;
        if (fpsTimer >= 1.0) {
            fps = fpsCounter;
            fpsCounter = 0;
            fpsTimer = 0;
            window.setTitle("2D-RPG - FPS: " + fps);
        }
    }

    public enum GameState {
        MAIN_MENU,
        PLAYING,
        PAUSED
    }

}