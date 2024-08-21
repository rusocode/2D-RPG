package com.craivet.input.keyboard;

import com.craivet.Game;
import com.craivet.assets.*;
import com.craivet.states.State;

public class GameOverState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.UP) {
            game.playSound(Assets.getAudio(AudioAssets.SLOT));
            game.ui.command--;
            if (game.ui.command < 0) game.ui.command = 1;
        }
        if (key == Key.DOWN) {
            game.playSound(Assets.getAudio(AudioAssets.SLOT));
            game.ui.command++;
            if (game.ui.command > 1) game.ui.command = 0;
        }
        if (key == Key.ENTER) {
            if (game.ui.command == 0) {
                State.setState(State.PLAY);
                game.reset(false);
            } else if (game.ui.command == 1) {
                State.setState(State.MAIN);
                game.reset(true);
            }
        }
    }

}
