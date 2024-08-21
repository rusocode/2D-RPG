package com.craivet.input.keyboard;

import com.craivet.Game;
import com.craivet.assets.*;
import com.craivet.states.State;

import java.util.Map;
import java.util.HashMap;

import static com.craivet.utils.Global.*;

/**
 * Maneja la logica de los estados del juego en respuesta a los eventos de teclado.
 */

public class KeyboardStateHandler {

    private final Map<State, GameState> stateHandlers;

    public KeyboardStateHandler() {
        stateHandlers = new HashMap<>();
        stateHandlers.put(State.MAIN, new MainState());
        stateHandlers.put(State.PLAY, new PlayState());
        stateHandlers.put(State.STATS, new StatsState());
        stateHandlers.put(State.INVENTORY, new InventoryState());
        stateHandlers.put(State.OPTION, new OptionState());
        stateHandlers.put(State.GAME_OVER, new GameOverState());
        stateHandlers.put(State.TRADE, new TradeState());
    }

    public void handleKeyPress(Key key, Game game) {
        GameState stateHandler = stateHandlers.get(State.getState());
        if (stateHandler != null) stateHandler.handleKeyPress(key, game);
    }

}
