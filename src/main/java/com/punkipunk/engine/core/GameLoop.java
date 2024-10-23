package com.punkipunk.engine.core;

import com.punkipunk.engine.core.api.GameLoopCallback;
import javafx.animation.AnimationTimer;

/**
 * Core functionality of the game loop.
 */

public class GameLoop extends AnimationTimer {

    private final GameLoopCallback callback;

    public GameLoop(GameLoopCallback callback) {
        this.callback = callback;
    }

    @Override
    public void handle(long now) {
        callback.tick();
    }

}
