package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;

public class OptionState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.ESCAPE) {
            game.getGameController().toggleOptionsView();
            game.system.ui.command = 0;
            game.system.ui.subState = 0;
        }

        int maxCommand = switch (game.system.ui.subState) {
            case 0 -> 5;
            case 1, 2 -> 1;
            default -> 0;
        };

        /* If the command selection is in the fullScreen control substates, then it does not execute the following
         * instructions since the selection is only kept in the back. */
        if (game.system.ui.subState == 0 || game.system.ui.subState == 2) {
            if (key == Key.UP) {
                game.playSound(Assets.getAudio(AudioAssets.HOVER));
                game.system.ui.command--;
                if (game.system.ui.command < 0) game.system.ui.command = maxCommand;
            }
            if (key == Key.DOWN) {
                game.playSound(Assets.getAudio(AudioAssets.HOVER));
                game.system.ui.command++;
                if (game.system.ui.command > maxCommand) game.system.ui.command = 0;
            }
        }

        // Down the volume
        if (key == Key.LEFT) {
            if (game.system.ui.subState == 0) {
                if (game.system.ui.command == 0 && game.system.music.volumeScale > 0) { // Music
                    game.system.music.volumeScale--;
                    // TODO Is this necessary here?
                    game.system.music.checkVolume(); // Change the volume of the music when it is already playing
                    game.playSound(Assets.getAudio(AudioAssets.HOVER));
                }
                if (game.system.ui.command == 1 && game.system.sound.volumeScale > 0) { // Sound
                    game.system.sound.volumeScale--;
                    game.playSound(Assets.getAudio(AudioAssets.HOVER));
                }
            }
        }
        // Turn up the volume
        if (key == Key.RIGHT) {
            if (game.system.ui.subState == 0) {
                if (game.system.ui.command == 0 && game.system.music.volumeScale < 5) {
                    game.system.music.volumeScale++;
                    game.system.music.checkVolume();
                    game.playSound(Assets.getAudio(AudioAssets.HOVER));
                }
                if (game.system.ui.command == 1 && game.system.sound.volumeScale < 5) { // Sonido
                    game.system.sound.volumeScale++;
                    game.playSound(Assets.getAudio(AudioAssets.HOVER));
                }
            }
        }
    }

}
