package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;

public class TradeState implements GameState {

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

    private static void npcInventoryState(Key key, Game game) {
        if (key == Key.UP) {
            if (game.systems.world.entities.player.inventory.npcSlotRow > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.world.entities.player.inventory.npcSlotRow--;
            }
        }
        if (key == Key.LEFT) {
            if (game.systems.world.entities.player.inventory.npcSlotCol > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.world.entities.player.inventory.npcSlotCol--;
            }
        }
        if (key == Key.DOWN) {
            if (game.systems.world.entities.player.inventory.npcSlotRow < 3) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.world.entities.player.inventory.npcSlotRow++;
            }
        }
        if (key == Key.RIGHT) {
            if (game.systems.world.entities.player.inventory.npcSlotCol < 4) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.world.entities.player.inventory.npcSlotCol++;
            }
        }
    }

    @Override
    public void handleKeyPress(Key key, Game game) {
        if (game.systems.ui.subState == 0) {
            if (key == Key.LEFT) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.ui.command--;
                if (game.systems.ui.command < 0) game.systems.ui.command = 1;
            }
            if (key == Key.RIGHT) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.systems.ui.command++;
                if (game.systems.ui.command > 1) game.systems.ui.command = 0;
            }
            if (key == Key.ESCAPE) game.systems.ui.command = 0;
        }
        if (game.systems.ui.subState == 1) {
            npcInventoryState(key, game);
            if (key == Key.ESCAPE) {
                game.systems.keyboard.releaseKey(Key.ESCAPE);
                game.systems.ui.subState = 0;
            }
        }
        if (game.systems.ui.subState == 2) {
            playerInventoryState(key, game);
            if (key == Key.ESCAPE) {
                game.systems.keyboard.releaseKey(Key.ESCAPE);
                game.systems.ui.subState = 0;
            }
        }
    }

}
