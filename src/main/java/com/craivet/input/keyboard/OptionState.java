package com.craivet.input.keyboard;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;
import com.craivet.states.State;

public class OptionState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.ESCAPE) {
            State.setState(State.PLAY);
            game.ui.command = 0;
            game.ui.subState = 0;
        }

        int maxCommand = switch (game.ui.subState) {
            case 0 -> 5;
            case 1, 2 -> 1;
            default -> 0;
        };

        /* If the command selection is in the fullScreen control substates, then it does not execute the following
         * instructions since the selection is only kept in the back. */
        if (game.ui.subState == 0 || game.ui.subState == 2) {
            if (key == Key.UP) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = maxCommand;
            }
            if (key == Key.DOWN) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command++;
                if (game.ui.command > maxCommand) game.ui.command = 0;
            }
        }

        // Down the volume
        if (key == Key.LEFT) {
            if (game.ui.subState == 0) {
                if (game.ui.command == 0 && game.music.volumeScale > 0) { // Music
                    game.music.volumeScale--;
                    // TODO Is this necessary here?
                    game.music.checkVolume(); // Change the volume of the music when it is already playing
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                }
                if (game.ui.command == 1 && game.sound.volumeScale > 0) { // Sound
                    game.sound.volumeScale--;
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                }
            }
        }
        // Turn up the volume
        if (key == Key.RIGHT) {
            if (game.ui.subState == 0) {
                if (game.ui.command == 0 && game.music.volumeScale < 5) {
                    game.music.volumeScale++;
                    game.music.checkVolume();
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                }
                if (game.ui.command == 1 && game.sound.volumeScale < 5) { // Sonido
                    game.sound.volumeScale++;
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                }
            }
        }
    }

}
