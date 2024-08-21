package com.craivet.input.keyboard;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;
import com.craivet.states.State;

public class InventoryState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (key == Key.INVENTORY || key == Key.ESCAPE) State.setState(State.PLAY);
        playerInventoryState(key, game);
    }

    private static void playerInventoryState(Key key, Game game) {
        if (key == Key.ENTER) game.world.entities.player.inventory.select();
        if (key == Key.UP) {
            if (game.world.entities.player.inventory.playerSlotRow > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.playerSlotRow--;
            }
        }
        if (key == Key.LEFT) {
            if (game.world.entities.player.inventory.playerSlotCol > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.playerSlotCol--;
            }
        }
        if (key == Key.DOWN) {
            if (game.world.entities.player.inventory.playerSlotRow < 3) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.playerSlotRow++;
            }
        }
        if (key == Key.RIGHT) {
            if (game.world.entities.player.inventory.playerSlotCol < 4) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.playerSlotCol++;
            }
        }
    }

}
