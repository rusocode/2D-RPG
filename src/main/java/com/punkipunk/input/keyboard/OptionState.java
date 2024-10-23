package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.states.State;

public class OptionState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.ESCAPE) {
            State.setState(State.PLAY);
            game.systems.ui.command = 0;
            game.systems.ui.subState = 0;
        }

        int maxCommand = switch (game.systems.ui.subState) {
            case 0 -> 5;
            case 1, 2 -> 1;
            default -> 0;
        };

        /* If the command selection is in the fullScreen control substates, then it does not execute the following
         * instructions since the selection is only kept in the back. */
        if (game.systems.ui.subState == 0 || game.systems.ui.subState == 2) {
            if (key == Key.UP) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.ui.command--;
                if (game.systems.ui.command < 0) game.systems.ui.command = maxCommand;
            }
            if (key == Key.DOWN) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.ui.command++;
                if (game.systems.ui.command > maxCommand) game.systems.ui.command = 0;
            }
        }

        // Down the volume
        if (key == Key.LEFT) {
            if (game.systems.ui.subState == 0) {
                if (game.systems.ui.command == 0 && game.systems.music.volumeScale > 0) { // Music
                    game.systems.music.volumeScale--;
                    // TODO Is this necessary here?
                    game.systems.music.checkVolume(); // Change the volume of the music when it is already playing
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                }
                if (game.systems.ui.command == 1 && game.systems.sound.volumeScale > 0) { // Sound
                    game.systems.sound.volumeScale--;
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                }
            }
        }
        // Turn up the volume
        if (key == Key.RIGHT) {
            if (game.systems.ui.subState == 0) {
                if (game.systems.ui.command == 0 && game.systems.music.volumeScale < 5) {
                    game.systems.music.volumeScale++;
                    game.systems.music.checkVolume();
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                }
                if (game.systems.ui.command == 1 && game.systems.sound.volumeScale < 5) { // Sonido
                    game.systems.sound.volumeScale++;
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                }
            }
        }
    }

}
