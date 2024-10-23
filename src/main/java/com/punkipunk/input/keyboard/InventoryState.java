package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.states.State;

public class InventoryState implements GameState {

    private static void playerInventoryState(Key key, Game game) {
        if (key == Key.ENTER) game.systems.world.entities.player.inventory.select();
        if (key == Key.UP) {
            if (game.systems.world.entities.player.inventory.playerSlotRow > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.world.entities.player.inventory.playerSlotRow--;
            }
        }
        if (key == Key.LEFT) {
            if (game.systems.world.entities.player.inventory.playerSlotCol > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.world.entities.player.inventory.playerSlotCol--;
            }
        }
        if (key == Key.DOWN) {
            if (game.systems.world.entities.player.inventory.playerSlotRow < 3) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.world.entities.player.inventory.playerSlotRow++;
            }
        }
        if (key == Key.RIGHT) {
            if (game.systems.world.entities.player.inventory.playerSlotCol < 4) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.world.entities.player.inventory.playerSlotCol++;
            }
        }
    }

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.INVENTORY || key == Key.ESCAPE) State.setState(State.PLAY);
        playerInventoryState(key, game);
    }

}
