package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.states.State;

import static com.punkipunk.utils.Global.ABANDONED_ISLAND;
import static com.punkipunk.utils.Global.ABANDONED_ISLAND_MARKET;

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
        if (Key.TOGGLE_KEYS.contains(key)) game.system.keyboard.toggleKey(key);
        switch (key) {
            case STATS:
                // Apretar la tecla C es la causante del evento que produce la superposicion de la escena de estadisticas
                game.getGameController().toggleStatsView();
                break;
            case INVENTORY:
                State.setState(State.INVENTORY);
                break;
            case ESCAPE:
                game.getGameController().toggleOptionsView();
                // State.setState(State.OPTION);
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

    }
}
