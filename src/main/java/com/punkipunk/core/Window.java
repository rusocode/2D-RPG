package com.punkipunk.core;

import com.punkipunk.input.keyboard.InputHandler;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Clase que maneja la ventana principal del juego usando LWJGL/GLFW. Reemplaza el sistema de Stage/Scene de JavaFX.
 */

public class Window {

    private long windowHandle;
    private int width, height;
    private String title;

    private boolean isRunning, isResized;

    private InputHandler inputHandler;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    /**
     * Inicializa GLFW y crea la ventana.
     */
    public void init() {

        // Configura el callback de errores
        GLFWErrorCallback.createPrint(System.err).set();

        // Inicializa GLFW
        if (!glfwInit()) throw new IllegalStateException("GLFW could not be initialized");

        // Configura GLFW para OpenGL 3.3 Core Profile
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // La ventana estara oculta despues de la creacion
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE); // Para macOS

        // Crea la ventana
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) throw new RuntimeException("Failed to create GLFW window");

        // Configura callbacks
        setupCallbacks();

        // Centra la ventana
        centerWindow();

        // Hace el contexto OpenGL actual
        glfwMakeContextCurrent(windowHandle);

        // Habilita v-sync
        glfwSwapInterval(1);

        // Hace visible la ventana
        glfwShowWindow(windowHandle);

        // Esta linea es critica para que LWJGL pueda usar el contexto OpenGL
        GL.createCapabilities();

        isRunning = true;

        System.out.println("Initialized LWJGL Window: " + width + "x" + height);
        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        // System.out.println("GLSL Version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
    }

    /**
     * Inicializa el sistema de entrada con referencia al juego.
     * <p>
     * Debe llamarse despues de que el juego este creado.
     *
     * @param game referencia al juego
     */
    public void initInput(IGame game) {
        if (inputHandler != null) inputHandler.cleanup();
        inputHandler = new InputHandler(windowHandle, game);
        System.out.println("Input system initialized");
    }

    /**
     * Actualiza la ventana (poll events y swap buffers).
     */
    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
        if (isResized) isResized = false;
    }

    /**
     * Verifica si la ventana debe cerrarse.
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    /**
     * Solicita el cierre de la ventana.
     */
    public void close() {
        glfwSetWindowShouldClose(windowHandle, true);
    }

    /**
     * Limpia el framebuffer.
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Libera los recursos de la ventana.
     */
    public void cleanup() {
        if (inputHandler != null) inputHandler.cleanup();

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) callback.free();

        isRunning = false;
        System.out.println("Window cleaned up");
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(windowHandle, title);
    }

    /**
     * Configura los callbacks de GLFW.
     */
    private void setupCallbacks() {
        // Callback para el tamaño de la ventana
        glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> {
            this.width = w;
            this.height = h;
            this.isResized = true;
            glViewport(0, 0, w, h); // TODO Hace falta?
        });

        /*
         // Callback para la tecla ESC (cerrar ventana)
         glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);});
        */

        // Callback para cierre de ventana
        glfwSetWindowCloseCallback(windowHandle, (window) -> {
            isRunning = false;
        });

    }

    /**
     * Centra la ventana en la pantalla.
     */
    private void centerWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if (vidmode != null) {
                glfwSetWindowPos(
                        windowHandle,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );
            }
        }
    }
}