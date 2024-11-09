package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.states.State;

public class GameOverState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.UP) {
            game.playSound(Assets.getAudio(AudioAssets.HOVER));
            game.system.ui.command--;
            if (game.system.ui.command < 0) game.system.ui.command = 1;
        }
        if (key == Key.DOWN) {
            game.playSound(Assets.getAudio(AudioAssets.HOVER));
            game.system.ui.command++;
            if (game.system.ui.command > 1) game.system.ui.command = 0;
        }
        if (key == Key.ENTER) {
            if (game.system.ui.command == 0) {
                State.setState(State.PLAY);
                game.reset(false);
            } else if (game.system.ui.command == 1) {
                State.setState(State.MAIN);
                game.reset(true);
            }
        }
    }

}
