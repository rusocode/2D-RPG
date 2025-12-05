package com.punkipunk.input.keyboard;

import com.punkipunk.core.IGame;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import static org.lwjgl.glfw.GLFW.*;


/**
 * Maneja la entrada de teclado y mouse para LWJGL/GLFW.
 * <p>
 * Esta clase configura y gestiona los callbacks de GLFW, actuando como puente entre GLFW y el sistema de entrada existente del
 * juego (Keyboard y Mouse).
 */

public class InputHandler {

    private final long windowHandle;
    private final IGame game;
    private final Keyboard keyboard;

    // Callbacks de GLFW
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;

    // Estado del mouse
    private double mouseX;
    private double mouseY;
    private boolean[] mouseButtons = new boolean[8]; // GLFW soporta hasta 8 botones

    /**
     * Crea un nuevo manejador de entrada.
     *
     * @param windowHandle handle de la ventana GLFW
     * @param game         referencia al juego
     */
    public InputHandler(long windowHandle, IGame game) {
        this.windowHandle = windowHandle;
        this.game = game;
        this.keyboard = game.getGameSystem().keyboard;
        setupCallbacks();
        System.out.println("InputHandler inicializado para LWJGL");
    }

    /**
     * Obtiene la posicion X actual del mouse.
     */
    public double getMouseX() {
        return mouseX;
    }

    /**
     * Obtiene la posicion Y actual del mouse.
     */
    public double getMouseY() {
        return mouseY;
    }

    /**
     * Verifica si un boton del mouse esta presionado.
     *
     * @param button codigo del boton (GLFW_MOUSE_BUTTON_*)
     * @return true si el boton esta presionado
     */
    public boolean isMouseButtonPressed(int button) {
        if (button >= 0 && button < mouseButtons.length) return mouseButtons[button];
        return false;
    }

    /**
     * Verifica si el boton izquierdo del mouse esta presionado.
     */
    public boolean isLeftButtonPressed() {
        return isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT);
    }

    /**
     * Verifica si el boton derecho del mouse esta presionado.
     */
    public boolean isRightButtonPressed() {
        return isMouseButtonPressed(GLFW_MOUSE_BUTTON_RIGHT);
    }

    /**
     * Verifica si el boton medio del mouse esta presionado.
     */
    public boolean isMiddleButtonPressed() {
        return isMouseButtonPressed(GLFW_MOUSE_BUTTON_MIDDLE);
    }

    /**
     * Libera todos los callbacks de GLFW. Debe llamarse antes de destruir la ventana.
     */
    public void cleanup() {
        if (keyCallback != null) keyCallback.free();
        if (cursorPosCallback != null) cursorPosCallback.free();
        if (mouseButtonCallback != null) mouseButtonCallback.free();
        if (scrollCallback != null) scrollCallback.free();
        System.out.println("InputHandler limpiado");
    }

    /**
     * Configura todos los callbacks de entrada de GLFW.
     */
    private void setupCallbacks() {
        // Callback de teclado
        keyCallback = glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            handleKeyEvent(key, action);
        });

        // Callback de posicion del cursor
        cursorPosCallback = glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
        });

        // Callback de botones del mouse
        mouseButtonCallback = glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            if (button >= 0 && button < mouseButtons.length) mouseButtons[button] = (action == GLFW_PRESS);
        });

        // Callback de scroll del mouse
        scrollCallback = glfwSetScrollCallback(windowHandle, (window, xoffset, yoffset) -> {
            // Aqui puedes manejar el scroll si es necesario
            // Por ahora solo lo registramos para futura implementacion
        });
    }

    /**
     * Maneja eventos de teclado de GLFW.
     *
     * @param glfwKey codigo de tecla GLFW
     * @param action  accion (PRESS, RELEASE, REPEAT)
     */
    private void handleKeyEvent(int glfwKey, int action) {
        Key key = KeyMapper.glfwToKey(glfwKey);

        if (key == null) return; // Tecla no mapeada

        if (action == GLFW_PRESS) {
            keyboard.pressKey(key);
        }
        else if (action == GLFW_RELEASE) keyboard.releaseKey(key);

        // GLFW_REPEAT lo ignoramos para evitar repeticion automatica
    }

}
