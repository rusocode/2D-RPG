package com.craivet.input.keyboard;

import com.craivet.Game;
import com.craivet.assets.*;
import com.craivet.states.State;

import static com.craivet.utils.Global.*;

public class MainState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (game.ui.mainWindowState == MAIN_WINDOW) {
            if (key == Key.UP) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 2;
            }
            if (key == Key.DOWN) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command++;
                if (game.ui.command > 2) game.ui.command = 0;
            }
            if (key == Key.ENTER) {
                if (game.ui.command == 0) {
                    State.setState(State.PLAY);
                    game.playSound(Assets.getAudio(AudioAssets.SPAWN));
                    switch (game.world.map.zone) {
                        case OVERWORLD -> game.playMusic(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
                        case DUNGEON -> game.playMusic(Assets.getAudio(AudioAssets.AMBIENT_DUNGEON));
                        case BOSS -> game.playMusic(Assets.getAudio(AudioAssets.MUSIC_BOSS));
                    }
                }
                if (game.ui.command == 1) {
                    game.file.loadData();
                    State.setState(State.PLAY);
                    game.playSound(Assets.getAudio(AudioAssets.SPAWN));
                    game.playMusic(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
                }
                if (game.ui.command == 2) System.exit(0);
            }
        } else if (game.ui.mainWindowState == SELECTION_WINDOW) {
            if (key == Key.UP) {
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 3;
            }
            if (key == Key.DOWN) {
                game.ui.command++;
                if (game.ui.command > 3) game.ui.command = 0;
            }
            if (key == Key.ENTER) {
                if (game.ui.command == 0 || game.ui.command == 1 || game.ui.command == 2)
                    State.setState(State.PLAY);
                if (game.ui.command == 3) {
                    game.ui.command = 0;
                    game.ui.mainWindowState = MAIN_WINDOW;
                }
            }
        }
    }

}
