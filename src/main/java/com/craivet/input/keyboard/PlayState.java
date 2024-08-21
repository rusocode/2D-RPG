package com.craivet.input.keyboard;

import com.craivet.Game;
import com.craivet.states.State;

import static com.craivet.utils.Global.*;

public class PlayState implements GameState {

    /**
     * <p>
     * Si usas if independientes, permite que multiples condiciones sean verdaderas simultaneamente. Por ejemplo, una tecla podria
     * ser tanto una tecla de alternancia como cambiar el estado del juego.
     * <p>
     * Si usas else if, solo se ejecutara una condicion, incluso si multiples condiciones son verdaderas.
     */
    @Override
    public void handleKeyPress(Key key, Game game) {
        if (Key.TOGGLE_KEYS.contains(key)) game.keyboard.toggleKey(key);
        else if (key == Key.STATS) State.setState(State.STATS);
        else if (key == Key.INVENTORY) State.setState(State.INVENTORY);
        else if (key == Key.ESCAPE) State.setState(State.OPTION);
            /* You need to save the edited text file by pressing Ctrl + F9 or by selecting Build > Build Project. Which will
             * rebuild the project and you can apply the change by pressing the R key. */
        else if (key == Key.LOAD_MAP) {
            switch (game.world.map.num) {
                case 0 -> game.file.loadMap("maps/abandoned_island.txt", ABANDONED_ISLAND, "Abandoned Island");
                case 1 ->
                        game.file.loadMap("maps/abandoned_island_market.txt", ABANDONED_ISLAND_MARKET, "Abandoned Island Market");
            }
        }
    }
}
