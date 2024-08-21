package com.craivet.input.keyboard;

import com.craivet.Game;

public interface GameState {

    void handleKeyPress(Key key, Game game);

}
