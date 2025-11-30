package com.punkipunk.ui;

import com.punkipunk.core.IGame;
import imgui.ImFont;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Manager principal de UI del juego usando ImGui. Maneja todos los componentes de UI durante el gameplay (Stats, Options,
 * Inventory, HUD, etc.). Equivalente a la combinacion de GameView.fxml + game.css + GameController.java.
 */

public class GameUI {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private boolean isInitialized;

    private ImFont blackPearlFont, defaultFont;

    /** Componentes de UI. */
    private StatsUI statsUI;

    private IGame game;

    /**
     * Inicializa el sistema de UI del juego.
     *
     * @param windowHandle handle de la ventana GLFW
     * @param game         referencia al juego
     */
    public void init(long windowHandle, IGame game) {
        this.game = game;

        // Crea el contexto de ImGui
        ImGui.createContext();

        // Configura ImGui IO
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.setIniFilename(null); // Desactiva el archivo imgui.ini

        // Carga la fuente ANTES de inicializar los backends
        loadFonts(io);

        // Inicializa backends
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 330 core");
        imGuiGl3.newFrame(); // Construye el atlas de fuentes

        // Configura el estilo
        setupStyle();

        // Inicializa componentes de UI
        initializeComponents();

        isInitialized = true;
        System.out.println("GameUI initialized correctly!");
    }

    /**
     * Renderiza toda la UI del juego. Debe llamarse en cada frame del game loop.
     */
    public void render(int width, int height) {
        if (!isInitialized) return;

        // Actualiza el tamaño del display
        ImGui.getIO().setDisplaySize(width, height);

        // Inicia nuevo frame de ImGui
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        // Actualiza y renderiza componentes visibles
        updateComponents();
        renderComponents();

        // Renderiza ImGui
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
            System.out.println("GameUI cleanup completed!");
        }
    }

    /**
     * Alterna la visibilidad de la ventana de estadisticas. Equivalente a toggleStatsView() en GameController.
     */
    public void toggleStatsView() {
        statsUI.toggle();
        System.out.println("Stats view toggled: " + statsUI.isVisible());
    }

    public void showStatsView() {
        statsUI.show();
    }

    public void hideStatsView() {
        statsUI.hide();
    }

    /**
     * Verifica si hay alguna ventana de UI abierta. Util para pausar el juego o deshabilitar controles.
     *
     * @return true si hay alguna ventana abierta
     */
    public boolean isAnyViewOpen() {
        return statsUI.isVisible() /* || optionsUI.isVisible() */;
    }

    public void closeAllViews() {
    }

    private void initializeComponents() {
        // Inicializa StatsUI con callback para el boton "Back" y la fuente personalizada
        statsUI = new StatsUI(this::toggleStatsView, blackPearlFont);
    }

    /**
     * Actualiza los datos de los componentes desde el juego.
     */
    private void updateComponents() {
        if (game == null || game.getGameSystem() == null) return;

        // Actualiza las estadisticas si la ventana esta visible
        if (statsUI.isVisible()) {
            var player = game.getGameSystem().world.entitySystem.player;
            statsUI.update(player.stats);
        }

        // TODO: Actualizar otros componentes
        // if (hudUI != null) hudUI.update(player.getStats());
    }

    // TODO: Agregar metodos para otros componentes
    // public void toggleOptionsView() { ... }
    // public void toggleInventoryView() { ... }
    // public void toggleControlsView() { ... }

    private void renderComponents() {
        statsUI.render();
    }

    /**
     * Carga las fuentes personalizadas para ImGui.
     *
     * @param io ImGuiIO para acceder al sistema de fuentes
     */
    private void loadFonts(ImGuiIO io) {
        try {
            // Carga fuente por defecto
            defaultFont = io.getFonts().addFontDefault();

            // Carga BlackPearl.ttf desde resources
            String fontPath = "/font/BlackPearl.ttf";
            InputStream fontStream = getClass().getResourceAsStream(fontPath);

            if (fontStream == null) {
                System.err.println("The " + fontPath + " font could not be found. Using default font.");
                blackPearlFont = defaultFont;
                return;
            }

            // Crea archivo temporal para la fuente
            // ImGui necesita una ruta de archivo, no puede cargar directamente desde InputStream
            Path tempFontFile = Files.createTempFile("BlackPearl", ".ttf");
            Files.copy(fontStream, tempFontFile, StandardCopyOption.REPLACE_EXISTING);
            tempFontFile.toFile().deleteOnExit(); // Elimina al cerrar la aplicacion
            fontStream.close();

            // Configuracion de la fuente
            ImFontConfig fontConfig = new ImFontConfig();
            fontConfig.setOversampleH(2); // Mejora la calidad horizontal
            fontConfig.setOversampleV(2); // Mejora la calidad vertical
            fontConfig.setPixelSnapH(true); // Alinea los pixeles para mejor nitidez

            // Carga la fuente con tamaño 22px (mismo que en el CSS original)
            blackPearlFont = io.getFonts().addFontFromFileTTF(tempFontFile.toString(), 22.0f, fontConfig);

            fontConfig.destroy(); // Libera memoria de la configuracion

            // Construye el atlas de fuentes
            io.getFonts().build();

            System.out.println("BlackPearl font loaded successfully!");

        } catch (Exception e) {
            System.err.println("Error loading BlackPearl font: " + e.getMessage());
            blackPearlFont = defaultFont;
        }
    }

    /**
     * Configura el estilo visual de ImGui.
     */
    private void setupStyle() {
        ImGui.styleColorsDark();
        // TODO: Personalizar estilo para que se parezca al CSS original
        // ImGuiStyle style = ImGui.getStyle();
        // style.setWindowRounding(10.0f); // Equivalente a -fx-background-radius: 10px
    }

}