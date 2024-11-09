package com.punkipunk;

import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import javafx.scene.text.Font;

/**
 * Configura el juego.
 */

public class GameSetup {

    private final Game game;

    public GameSetup(Game game) {
        this.game = game;
    }

    public void setup() {
        initializeSystems();
    }

    private void initializeSystems() {
        game.playMusic(Assets.getAudio(AudioAssets.MUSIC_MAIN));
    }


}
