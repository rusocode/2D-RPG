package com.craivet.input.keyboard;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;

public class TradeState implements GameState {

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (game.ui.subState == 0) {
            if (key == Key.LEFT) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 1;
            }
            if (key == Key.RIGHT) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command++;
                if (game.ui.command > 1) game.ui.command = 0;
            }
            if (key == Key.ESCAPE) game.ui.command = 0;
        }
        if (game.ui.subState == 1) {
            npcInventoryState(key, game);
            if (key == Key.ESCAPE) {
                game.keyboard.releaseKey(Key.ESCAPE);
                game.ui.subState = 0;
            }
        }
        if (game.ui.subState == 2) {
            playerInventoryState(key, game);
            if (key == Key.ESCAPE) {
                game.keyboard.releaseKey(Key.ESCAPE);
                game.ui.subState = 0;
            }
        }
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

    private static void npcInventoryState(Key key, Game game) {
        if (key == Key.UP) {
            if (game.world.entities.player.inventory.npcSlotRow > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.npcSlotRow--;
            }
        }
        if (key == Key.LEFT) {
            if (game.world.entities.player.inventory.npcSlotCol > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.npcSlotCol--;
            }
        }
        if (key == Key.DOWN) {
            if (game.world.entities.player.inventory.npcSlotRow < 3) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.npcSlotRow++;
            }
        }
        if (key == Key.RIGHT) {
            if (game.world.entities.player.inventory.npcSlotCol < 4) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.npcSlotCol++;
            }
        }
    }

}
