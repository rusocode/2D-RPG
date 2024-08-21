package com.craivet.input.keyboard;

import com.craivet.Game;
import com.craivet.states.State;

public class StatsState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.STATS || key == Key.ESCAPE) State.setState(State.PLAY);
    }

}
