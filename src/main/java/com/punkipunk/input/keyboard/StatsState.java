package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.controllers.GameController;

public class StatsState implements GameState {

    private final GameController gameController;

    public StatsState(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.STATS || key == Key.ESCAPE) gameController.toggleStatsView();
    }

}
