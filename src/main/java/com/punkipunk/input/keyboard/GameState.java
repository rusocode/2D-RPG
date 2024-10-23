package com.punkipunk.input.keyboard;

import com.punkipunk.Game;

public interface GameState {

    void handleKeyPress(Key key, Game game);

}
