package com.craivet.input;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;
import com.craivet.states.State;

import javax.swing.*;
import java.awt.event.*;

import static com.craivet.utils.Global.*;

/**
 * <ul>
 * <li>Confirm/Attack: Enter</li>
 * <li>Options/Back: ESC</li>
 * <li>Move: WASD</li>
 * <li>Shoot: F</li>
 * <li>Pickup Item: P</li>
 * <li>Minimap: M</li>
 * <li>Inventory: I</li>
 * <li>Stats Window: C</li>
 * <li>Debug: Q</li>
 * <li>Test Mode: T</li>
 * Ignores collisions with solid tiles, does not lose life and can see in the dark.
 * <li>Hitbox: H</li>
 * </ul>
 */

public class Keyboard extends KeyAdapter {

    private final Game game;

    public boolean enter, esc, w, a, s, d, shoot, pickup, minimap, debug, test, hitbox;
    private int lastKey = -1;
    private final int maxKeycode = 255;

    public Keyboard(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < 0 || e.getKeyCode() > maxKeycode) {
            JOptionPane.showMessageDialog(null, "Keycode cannot be greater than 255 or negative, keycode: " + e.getKeyCode(), "Input error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (lastKey != e.getKeyCode()) {
            lastKey = e.getKeyCode();
            if (State.isState(State.MAIN)) mainState(lastKey);
            else if (State.isState(State.PLAY)) playState(lastKey);
            else if (State.isState(State.DIALOGUE) || State.isState(State.CUTSCENE)) dialogueState(lastKey);
            else if (State.isState(State.STATS)) statsState(lastKey);
            else if (State.isState(State.INVENTORY)) inventoryState(lastKey);
            else if (State.isState(State.OPTION)) optionState(lastKey);
            else if (State.isState(State.GAME_OVER)) gameOverState(lastKey);
            else if (State.isState(State.TRADE)) tradeState(lastKey);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < 0 || e.getKeyCode() > maxKeycode) {
            JOptionPane.showMessageDialog(null, "Keycode cannot be greater than 255 or negative, keycode: " + e.getKeyCode(), "Input error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lastKey = -1;
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) w = false;
        if (code == KeyEvent.VK_A) a = false;
        if (code == KeyEvent.VK_S) s = false;
        if (code == KeyEvent.VK_D) d = false;
        if (code == KeyEvent.VK_F) shoot = false;
    }

    public boolean checkKeys() {
        return checkMovementKeys() || checkAccionKeys();
    }

    public boolean checkMovementKeys() {
        return s || w || a || d;
    }

    public boolean checkAccionKeys() {
        return enter || pickup;
    }

    public void resetAccionKeys() {
        enter = false;
        pickup = false;
    }

    private void mainState(int code) {
        if (game.ui.mainWindowState == MAIN_WINDOW) {
            if (code == KeyEvent.VK_W) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 2;
            }
            if (code == KeyEvent.VK_S) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command++;
                if (game.ui.command > 2) game.ui.command = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
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
            if (code == KeyEvent.VK_W) {
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 3;
            }
            if (code == KeyEvent.VK_S) {
                game.ui.command++;
                if (game.ui.command > 3) game.ui.command = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (game.ui.command == 0 || game.ui.command == 1 || game.ui.command == 2)
                    State.setState(State.PLAY);
                if (game.ui.command == 3) {
                    game.ui.command = 0;
                    game.ui.mainWindowState = MAIN_WINDOW;
                }
            }
        }
    }

    private void playState(int code) {
        if (code == KeyEvent.VK_W) w = true;
        if (code == KeyEvent.VK_A) a = true;
        if (code == KeyEvent.VK_S) s = true;
        if (code == KeyEvent.VK_D) d = true;
        if (code == KeyEvent.VK_ENTER) enter = true;
        if (code == KeyEvent.VK_F) shoot = true;
        if (code == KeyEvent.VK_P) pickup = true;
        if (code == KeyEvent.VK_Q) debug = !debug;
        if (code == KeyEvent.VK_H) hitbox = !hitbox;
        if (code == KeyEvent.VK_T) test = !test;
        if (code == KeyEvent.VK_C) State.setState(State.STATS);
        if (code == KeyEvent.VK_I) State.setState(State.INVENTORY);
        if (code == KeyEvent.VK_ESCAPE) State.setState(State.OPTION);
        if (code == KeyEvent.VK_M) minimap = !minimap;
        /* You need to save the edited text file by pressing Ctrl + F9 or by selecting Build > Build Project. Which will
         * rebuild the project and you can apply the change by pressing the R key. */
        if (code == KeyEvent.VK_R) {
            switch (game.world.map.num) {
                case 0 -> game.file.loadMap("maps/abandoned_island.txt", ABANDONED_ISLAND, "Abandoned Island");
                case 1 ->
                        game.file.loadMap("maps/abandoned_island_market.txt", ABANDONED_ISLAND_MARKET, "Abandoned Island Market");
            }
        }
    }

    private void dialogueState(int code) {
        if (code == KeyEvent.VK_ENTER) enter = true;
    }

    private void statsState(int code) {
        if (code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) State.setState(State.PLAY);
    }

    private void inventoryState(int code) {
        if (code == KeyEvent.VK_I || code == KeyEvent.VK_ESCAPE) State.setState(State.PLAY);
        playerInventoryState(code);
        if (code == KeyEvent.VK_ENTER) game.world.entities.player.inventory.select();
    }

    private void optionState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            State.setState(State.PLAY);
            game.ui.command = 0;
            game.ui.subState = 0;
        }

        if (code == KeyEvent.VK_ENTER) enter = true;

        int maxCommand = switch (game.ui.subState) {
            case 0 -> 5;
            case 1, 2 -> 1;
            default -> 0;
        };

        /* If the command selection is in the fullScreen control substates, then it does not execute the following
         * instructions since the selection is only kept in the back. */
        if (game.ui.subState == 0 || game.ui.subState == 2) {
            if (code == KeyEvent.VK_W) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = maxCommand;
            }
            if (code == KeyEvent.VK_S) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command++;
                if (game.ui.command > maxCommand) game.ui.command = 0;
            }
        }

        // Down the volume
        if (code == KeyEvent.VK_A) {
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
        if (code == KeyEvent.VK_D) {
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

    private void gameOverState(int code) {
        if (code == KeyEvent.VK_W) {
            game.playSound(Assets.getAudio(AudioAssets.SLOT));
            game.ui.command--;
            if (game.ui.command < 0) game.ui.command = 1;
        }
        if (code == KeyEvent.VK_S) {
            game.playSound(Assets.getAudio(AudioAssets.SLOT));
            game.ui.command++;
            if (game.ui.command > 1) game.ui.command = 0;
        }
        if (code == KeyEvent.VK_ENTER) {
            if (game.ui.command == 0) {
                State.setState(State.PLAY);
                game.reset(false);
            } else if (game.ui.command == 1) {
                State.setState(State.MAIN);
                game.reset(true);
            }
        }
    }

    private void tradeState(int code) {
        if (code == KeyEvent.VK_ESCAPE) esc = true;
        if (code == KeyEvent.VK_ENTER) enter = true;
        if (game.ui.subState == 0) {
            if (code == KeyEvent.VK_A) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 1;
            }
            if (code == KeyEvent.VK_D) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command++;
                if (game.ui.command > 1) game.ui.command = 0;
            }
            if (code == KeyEvent.VK_ESCAPE) game.ui.command = 0;
        }
        if (game.ui.subState == 1) {
            npcInventoryState(code);
            if (code == KeyEvent.VK_ESCAPE) {
                esc = false;
                game.ui.subState = 0;
            }
        }
        if (game.ui.subState == 2) {
            playerInventoryState(code);
            if (code == KeyEvent.VK_ESCAPE) {
                esc = false;
                game.ui.subState = 0;
            }
        }
    }

    private void playerInventoryState(int code) {
        if (code == KeyEvent.VK_W) {
            if (game.world.entities.player.inventory.playerSlotRow > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.playerSlotRow--;
            }
        }
        if (code == KeyEvent.VK_A) {
            if (game.world.entities.player.inventory.playerSlotCol > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.playerSlotCol--;
            }
        }
        if (code == KeyEvent.VK_S) {
            if (game.world.entities.player.inventory.playerSlotRow < 3) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.playerSlotRow++;
            }
        }
        if (code == KeyEvent.VK_D) {
            if (game.world.entities.player.inventory.playerSlotCol < 4) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.playerSlotCol++;
            }
        }
    }

    private void npcInventoryState(int code) {
        if (code == KeyEvent.VK_W) {
            if (game.world.entities.player.inventory.npcSlotRow > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.npcSlotRow--;
            }
        }
        if (code == KeyEvent.VK_A) {
            if (game.world.entities.player.inventory.npcSlotCol > 0) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.npcSlotCol--;
            }
        }
        if (code == KeyEvent.VK_S) {
            if (game.world.entities.player.inventory.npcSlotRow < 3) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.npcSlotRow++;
            }
        }
        if (code == KeyEvent.VK_D) {
            if (game.world.entities.player.inventory.npcSlotCol < 4) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.world.entities.player.inventory.npcSlotCol++;
            }
        }
    }

}
