package com.punkipunk.input.keyboard;

import com.punkipunk.Game;

@FunctionalInterface
public interface KeyPressHandler {

    void handle(Key key, Game game);

}
