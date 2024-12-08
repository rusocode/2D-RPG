package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.states.State;

import java.util.EnumMap;
import java.util.Map;

import static com.punkipunk.utils.Global.ABANDONED_ISLAND;
import static com.punkipunk.utils.Global.ABANDONED_ISLAND_MARKET;

/**
 * Maneja la logica de los estados del juego en respuesta a los eventos de teclado.
 */

public class KeyboardState {

    private final Map<State, KeyPressHandler> stateHandlers;

    public KeyboardState() {
        stateHandlers = new EnumMap<>(State.class);
        initializeStateHandlers();
    }

    private void initializeStateHandlers() {
        stateHandlers.put(State.PLAY, (key, game) -> {
            if (Key.TOGGLE_KEYS.contains(key)) game.system.keyboard.toggleKey(key);
            /* Si usas if independientes, permite que multiples condiciones sean verdaderas simultaneamente. Por ejemplo, una
             * tecla podria ser tanto una tecla de alternancia como cambiar el estado del juego. Si usas else if, solo se
             * ejecutara una condicion, incluso si multiples condiciones son verdaderas. */
            switch (key) {
                case ESCAPE:
                    game.getGameController().toggleOptionsView();
                    // State.setState(State.OPTION);
                    break;
                case INVENTORY:
                    game.getGameController().toggleInventoryView();
                    break;
                case LOAD_MAP:
                    /* You need to save the edited text file by pressing Ctrl + F9 or by selecting Build > Build Project. Which will
                     * rebuild the project and you can apply the change by pressing the R key. */
                    switch (game.system.world.map.num) {
                        case 0 -> game.system.file.loadMap("maps/abandoned_island.txt", ABANDONED_ISLAND, "Abandoned Island");
                        case 1 ->
                                game.system.file.loadMap("maps/abandoned_island_market.txt", ABANDONED_ISLAND_MARKET, "Abandoned Island Market");
                    }
            }
        });

        stateHandlers.put(State.TRADE, (key, game) -> {
            // if (key == Key.ENTER) game.system.world.entities.player.inventory.select();
        });

    }

    public void handleKeyPress(Key key, Game game) {
        KeyPressHandler handler = stateHandlers.get(State.getState());
        if (handler != null) handler.handle(key, game);
    }

}
