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
            game.systems.ui.command--;
            if (game.systems.ui.command < 0) game.systems.ui.command = 1;
        }
        if (key == Key.DOWN) {
            game.playSound(Assets.getAudio(AudioAssets.HOVER));
            game.systems.ui.command++;
            if (game.systems.ui.command > 1) game.systems.ui.command = 0;
        }
        if (key == Key.ENTER) {
            if (game.systems.ui.command == 0) {
                State.setState(State.PLAY);
                game.reset(false);
            } else if (game.systems.ui.command == 1) {
                State.setState(State.MAIN);
                game.reset(true);
            }
        }
    }

}
