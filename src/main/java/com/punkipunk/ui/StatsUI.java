package com.punkipunk.ui;

import com.punkipunk.entity.components.Stats;
import imgui.ImFont;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

/**
 * Componente de UI para mostrar las estadisticas del jugador usando ImGui.
 * <p>
 * Migracion de StatsController.java (JavaFX) a ImGui.
 */

public class StatsUI {

    /** Referencia al sistema de UI principal para poder cerrarse a si mismo. */
    private final Runnable onBackPressed;
    /** Fuente personalizada para el titulo. */
    private final ImFont titleFont;
    private boolean isVisible;
    private Stats currentStats; // TODO Renombro a stats?

    /**
     * Constructor del componente de estadisticas.
     *
     * @param onBackPressed callback que se ejecuta cuando se presiona el boton "Back"
     */
    public StatsUI(Runnable onBackPressed, ImFont titleFont) {
        this.onBackPressed = onBackPressed;
        this.titleFont = titleFont;
    }

    /**
     * Renderiza la ventana de estadisticas. Este metodo debe ser llamado en cada frame del game loop.
     */
    public void render() {
        if (!isVisible || currentStats == null) return;

        // Configura el estilo de la ventana
        ImGui.setNextWindowSize(250, 330, 0); // Equivalente a max-width: 250px, max-height: 330px
        ImGui.setNextWindowBgAlpha(0.8f); // Equivalente a rgba(30, 30, 30, 0.8)

        // Flags de la ventana: sin redimensionar, sin mover, sin colapsar
        int windowFlags = ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar;

        // Centra la ventana en la pantalla
        ImGui.setNextWindowPos(ImGui.getIO().getDisplaySizeX() / 2 - 125, ImGui.getIO().getDisplaySizeY() / 2 - 165);

        if (ImGui.begin("Stats", windowFlags)) {

            // Titulo
            if (titleFont != null) ImGui.pushFont(titleFont);

            float titleWidth = ImGui.calcTextSize("STATS").x;
            ImGui.setCursorPosX((ImGui.getWindowWidth() - titleWidth) / 2);
            ImGui.text("STATS");

            if (titleFont != null) ImGui.popFont();

            ImGui.spacing();
            ImGui.spacing();

            // Estadisticas, equivalente a los HBox de JavaFX
            renderStatRow("Level: ", String.valueOf(currentStats.lvl));
            renderStatRow("Exp: ", String.valueOf(currentStats.exp));
            renderStatRow("Next Exp: ", String.valueOf(currentStats.nextExp));
            renderStatRow("Health: ", currentStats.hp + "/" + currentStats.maxHp);
            renderStatRow("Mana: ", currentStats.mana + "/" + currentStats.maxMana);
            renderStatRow("Strength: ", String.valueOf(currentStats.strength));
            renderStatRow("Dexterity: ", String.valueOf(currentStats.dexterity));
            renderStatRow("Attack: ", String.valueOf(currentStats.attack));
            renderStatRow("Defense: ", String.valueOf(currentStats.defense));
            renderStatRow("Gold: ", String.valueOf(currentStats.gold));

            // TODO: Agregar las imagenes de espada y escudo cuando implementemos el sistema de texturas
            // renderEquipmentIcons();

            ImGui.spacing();

            // Boton "Back", equivalente al Label con onMouseClicked
            renderBackButton();

            ImGui.end();
        }
    }

    /**
     * Actualiza las estadisticas mostradas. Equivalente al metodo update(Stats stats) en StatsController.
     *
     * @param stats estadisticas del jugador
     */
    public void update(Stats stats) {
        this.currentStats = stats;
    }

    public void show() {
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }

    public void toggle() {
        isVisible = !isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Renderiza una fila de estadistica (etiqueta + valor). Equivalente a los HBox en el FXML original.
     *
     * @param label etiqueta de la estadistica
     * @param value valor de la estadistica
     */
    private void renderStatRow(String label, String value) {
        ImGui.text(label);
        ImGui.sameLine();
        ImGui.textColored(200, 200, 200, 255, value); // Color ligeramente diferente para el valor
    }

    /**
     * Renderiza el boton "Back". Cambia de color al hacer hover (equivalente al CSS :hover).
     */
    private void renderBackButton() {
        // Centra el boton
        float buttonWidth = ImGui.calcTextSize("Back").x + 20;
        ImGui.setCursorPosX((ImGui.getWindowWidth() - buttonWidth) / 2);

        // Detecta hover para cambiar el color
        boolean isHovered = ImGui.isMouseHoveringRect(
                ImGui.getCursorScreenPosX(),
                ImGui.getCursorScreenPosY(),
                ImGui.getCursorScreenPosX() + buttonWidth,
                ImGui.getCursorScreenPosY() + 20
        );

        if (isHovered) {
            ImGui.textColored(160, 160, 160, 255, "Back"); // Color #a0a0a0 en hover
            if (ImGui.isMouseClicked(0)) handleBackPressed(); // Click izquierdo
        } else ImGui.text("Back");

    }

    /**
     * Maneja el evento de presionar el boton "Back". Equivalente a handleBackButtonClicked() en StatsController.
     */
    private void handleBackPressed() {
        hide();
        if (onBackPressed != null) onBackPressed.run();
    }

}
