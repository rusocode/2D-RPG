package com.punkipunk.ui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

/**
 * Menu principal del juego usando ImGui. Reemplaza el MainView.fxml de JavaFX.
 */

public class MainMenuUI {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private boolean isInitialized = false;
    private MenuAction currentAction = MenuAction.NONE;

    public void init(long windowHandle) {

        // Crea el contexto de ImGui
        ImGui.createContext();

        // Obtiene el IO para configuracion
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.setIniFilename(null); // Desactiva el archivo imgui.ini

        // Inicializa el backend de GLFW primero
        imGuiGlfw.init(windowHandle, true);
        /* Inicializa el backend de GL3. Esto NO construye automaticamente el atlas de fuentes, se necesita llamar explicitamente
         * al metodo que construye y carga las texturas, que en este caso es imGuiGl3.newFrame(). */
        imGuiGl3.init("#version 330 core");

        /* Metodo que construye el atlas de fuentes y sube las texturas a la GPU. Debe llamarse al menos una vez durante la
         * inicializacion y en cada frame de renderizado, debe llamarse ANTES que imGuiGlfw.newFrame(). */
        imGuiGl3.newFrame();

        // Configura el estilo
        setupStyle();

        isInitialized = true;
        System.out.println("ImGui initialized correctly");
    }

    /**
     * Renderiza el menu principal.
     */
    public void render(int width, int height) {
        if (!isInitialized) return;

        // Actualiza el tamaño del display antes de cada frame
        ImGui.getIO().setDisplaySize(width, height); // TODO Hace falta actualizar?

        // Inicia un nuevo frame de ImGui
        imGuiGl3.newFrame(); // Primero GL3
        imGuiGlfw.newFrame(); // Luego GLFW
        ImGui.newFrame(); // Finalmente ImGui

        // Resetea la accion actual
        currentAction = MenuAction.NONE;

        // Crea la ventana del menu principal
        renderMainMenu();

        // Renderiza
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    /**
     * Limpia los recursos de ImGui.
     */
    public void cleanup() {
        if (isInitialized) {
            imGuiGl3.shutdown();
            imGuiGlfw.shutdown();
            ImGui.destroyContext();
            isInitialized = false;
            System.out.println("ImGui cleanup completed");
        }
    }

    /**
     * Obtiene la accion actual del menu.
     */
    public MenuAction getCurrentAction() {
        return currentAction;
    }

    /**
     * Configura el estilo visual del menu.
     */
    private void setupStyle() {
        ImGui.styleColorsDark(); // Tema oscuro por defecto
        // Ejemplo de personalizacion adicional:
        // ImGuiStyle style = ImGui.getStyle();
        // style.setWindowRounding(5.0f);
        // style.setFrameRounding(3.0f);
    }

    /**
     * Renderiza la ventana del menu principal.
     */
    private void renderMainMenu() {
        // Configura posicion y tamaño de la ventana del menu
        ImGui.setNextWindowPos(400, 250);
        ImGui.setNextWindowSize(480, 300);

        // Crea ventana con flags
        int windowFlags = ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse;

        ImGui.begin("2D-RPG", windowFlags);

        // Titulo
        ImGui.text("");
        ImGui.text("MAIN MENU");
        ImGui.text("");
        ImGui.separator();
        ImGui.text("");

        // Botones del menu (centrados)
        float buttonWidth = 200;
        float centerX = (ImGui.getWindowWidth() - buttonWidth) * 0.5f;
        ImGui.setCursorPosX(centerX);

        if (ImGui.button("NEW GAME", buttonWidth, 40)) {
            currentAction = MenuAction.NEW_GAME;
            System.out.println("NEW GAME clicked");
        }

        ImGui.text("");
        ImGui.setCursorPosX(centerX);

        if (ImGui.button("LOAD GAME", buttonWidth, 40)) {
            currentAction = MenuAction.LOAD_GAME;
            System.out.println("LOAD GAME clicked");
        }

        ImGui.text("");
        ImGui.setCursorPosX(centerX);

        if (ImGui.button("QUIT", buttonWidth, 40)) {
            currentAction = MenuAction.QUIT;
            System.out.println("QUIT clicked");
        }

        ImGui.end();
    }

    public enum MenuAction {
        NONE,
        NEW_GAME,
        LOAD_GAME,
        QUIT
    }

}