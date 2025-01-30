package com.punkipunk.input.keyboard;

import com.punkipunk.core.Game;
import com.punkipunk.states.State;
import com.punkipunk.world.MapID;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 * Controla el comportamiento de las teclas segun el estado actual del juego. Cada estado del juego tiene su propio listener que
 * define como responder a las diferentes teclas presionadas.
 */

public class KeyStateController {

    private final Map<State, KeyPressListener> stateListeners = new EnumMap<>(State.class);

    public KeyStateController() {
        initializeListeners();
    }

    /**
     * Notifica la pulsacion de una tecla al listener correspondiente al estado actual.
     *
     * @param key  la tecla presionada
     * @param game referencia al juego actual
     */
    public void notifyKeyPress(Key key, Game game) {
        KeyPressListener listener = stateListeners.get(State.getState());
        if (listener != null) listener.onKeyPress(key, game);
    }

    /**
     * Inicializa los listeners para cada estado del juego.
     */
    private void initializeListeners() {
        stateListeners.put(State.PLAY, this::processPlayState);
        stateListeners.put(State.TRADE, this::processTradeState);
    }

    /**
     * Procesa las teclas presionadas durante el estado de juego (PLAY).
     * <p>
     * Maneja teclas de alternancia y acciones especificas como abrir inventario u opciones.
     *
     * @param key  la tecla presionada
     * @param game referencia al juego actual
     */
    private void processPlayState(Key key, Game game) {
        if (Key.TOGGLE_KEYS.contains(key)) game.gameSystem.keyboard.toggleKey(key);
        switch (key) {
            case ESCAPE -> game.getGameController().toggleOptionsView();
            case INVENTORY -> game.getGameController().toggleInventoryView();
            case LOAD_MAP -> processMapLoading(game);
            case TEST -> game.getGameController().toggleTestMode();
        }
    }

    private void processTradeState(Key key, Game game) {
        // if (key == Key.ENTER) game.system.world.entities.player.inventory.select();
    }

    /**
     * Procesa la carga de mapas segun el numero de mapa actual.
     * <p>
     * Debes guardar el archivo de texto editado presionando Ctrl + F9 o seleccionando Generar > Generar proyecto. Esto
     * reconstruira el proyecto y podras aplicar el cambio presionando la tecla R.
     *
     * @param game referencia al juego actual
     */
    private void processMapLoading(Game game) {
        /* if (game.gameSystem.world.map.id.equals(MapID.ABANDONED_ISLAND))
            game.gameSystem.file.loadMap("maps/abandoned_island.txt", MapID.ABANDONED_ISLAND);
        if (game.gameSystem.world.map.id.equals(MapID.ABANDONED_ISLAND_MARKET))
            game.gameSystem.file.loadMap("maps/abandoned_island_market.txt", MapID.ABANDONED_ISLAND); */
    }

}

