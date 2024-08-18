package com.craivet.input;

import com.craivet.Game;
import com.craivet.assets.*;
import com.craivet.states.State;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.BitSet;
import java.util.Set;

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
    private final BitSet keys;
    private final BitSet toggledKeys; // Para teclas que alternan su estado
    private int lastKey = -1;

    private static final Set<Integer> MOVEMENT_KEYS = Set.of(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D);
    private static final Set<Integer> TOGGLE_KEYS = Set.of(KeyEvent.VK_Q, KeyEvent.VK_H, KeyEvent.VK_T, KeyEvent.VK_M);
    private static final Set<Integer> ACTION_KEYS = Set.of(KeyEvent.VK_ENTER, KeyEvent.VK_P);

    public Keyboard(Game game) {
        this.game = game;
        this.keys = new BitSet(256);
        this.toggledKeys = new BitSet(256);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (isValidKey(key) && lastKey != key) {
            lastKey = key;
            keys.set(key);
            StateHandler.handleKeyPress(this, State.getState(), key, game);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        lastKey = -1;
        int key = e.getKeyCode();
        if (isValidKey(key)) keys.clear(key);
    }

    /**
     * Check if the key is valid.
     *
     * @param key key.
     * @return true if key is valid or false otherwise.
     */
    private boolean isValidKey(int key) {
        int maxKeyCode = 255;
        return key >= 0 && key <= maxKeyCode;
    }

    /**
     * Verifica si se presiono la tecla.
     *
     * @param key tecla.
     * @return el valor del bit de la tecla.
     */
    public boolean isKeyPressed(int key) {
        return keys.get(key);
    }

    /**
     * Libera la tecla.
     *
     * @param key key.
     */
    public void releaseKey(int key) {
        keys.clear(key); // Establece el bit de la tecla a false
    }

    /**
     * Alterna el estado de la tecla.
     *
     * @param key tecla.
     */
    public void toggleKey(int key) {
        toggledKeys.flip(key);
    }

    /**
     * Verifica si la tecla alternada esta "activada".
     *
     * @param key tecla.
     * @return true si la tecla aternada esta activada o false.
     */
    public boolean isKeyToggled(int key) {
        return toggledKeys.get(key);
    }

    public boolean checkKeys() {
        return checkMovementKeys() || checkActionKeys();
    }

    public boolean checkMovementKeys() {
        return MOVEMENT_KEYS.stream().anyMatch(this::isKeyPressed);
    }

    public boolean checkActionKeys() {
        return ACTION_KEYS.stream().anyMatch(this::isKeyPressed);
    }

    public void resetActionKeys() {
        ACTION_KEYS.forEach(keys::clear);
    }

    private static class StateHandler {

        public static void handleKeyPress(Keyboard keyboard, State state, int key, Game game) {
            switch (state) {
                case MAIN -> handleMainState(key, game);
                case PLAY -> handlePlayState(keyboard, key, game);
                case STATS -> handleStatsState(key);
                case INVENTORY -> handleInventoryState(key, game);
                case OPTION -> handleOptionState(key, game);
                case GAME_OVER -> handleGameOverState(key, game);
                case TRADE -> handleTradeState(keyboard, key, game);
            }
        }

        private static void handleMainState(int key, Game game) {
            if (game.ui.mainWindowState == MAIN_WINDOW) {
                if (key == KeyEvent.VK_W) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.ui.command--;
                    if (game.ui.command < 0) game.ui.command = 2;
                }
                if (key == KeyEvent.VK_S) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.ui.command++;
                    if (game.ui.command > 2) game.ui.command = 0;
                }
                if (key == KeyEvent.VK_ENTER) {
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
                if (key == KeyEvent.VK_W) {
                    game.ui.command--;
                    if (game.ui.command < 0) game.ui.command = 3;
                }
                if (key == KeyEvent.VK_S) {
                    game.ui.command++;
                    if (game.ui.command > 3) game.ui.command = 0;
                }
                if (key == KeyEvent.VK_ENTER) {
                    if (game.ui.command == 0 || game.ui.command == 1 || game.ui.command == 2)
                        State.setState(State.PLAY);
                    if (game.ui.command == 3) {
                        game.ui.command = 0;
                        game.ui.mainWindowState = MAIN_WINDOW;
                    }
                }
            }
        }

        /**
         * <p>
         * Si usas if independientes, permite que multiples condiciones sean verdaderas simultaneamente. Por ejemplo, una tecla
         * podria ser tanto una tecla de alternancia como cambiar el estado del juego.
         * <p>
         * Si usas else if, solo se ejecutara una condicion, incluso si multiples condiciones son verdaderas.
         */
        private static void handlePlayState(Keyboard keyboard, int key, Game game) {
            if (TOGGLE_KEYS.contains(key)) keyboard.toggleKey(key);
            else if (key == KeyEvent.VK_C) State.setState(State.STATS);
            else if (key == KeyEvent.VK_I) State.setState(State.INVENTORY);
            else if (key == KeyEvent.VK_ESCAPE) State.setState(State.OPTION);
                /* You need to save the edited text file by pressing Ctrl + F9 or by selecting Build > Build Project. Which will
                 * rebuild the project and you can apply the change by pressing the R key. */
            else if (key == KeyEvent.VK_R) {
                switch (game.world.map.num) {
                    case 0 -> game.file.loadMap("maps/abandoned_island.txt", ABANDONED_ISLAND, "Abandoned Island");
                    case 1 ->
                            game.file.loadMap("maps/abandoned_island_market.txt", ABANDONED_ISLAND_MARKET, "Abandoned Island Market");
                }
            }
        }

        private static void handleStatsState(int key) {
            if (key == KeyEvent.VK_C || key == KeyEvent.VK_ESCAPE) State.setState(State.PLAY);
        }

        private static void handleInventoryState(int key, Game game) {
            if (key == KeyEvent.VK_I || key == KeyEvent.VK_ESCAPE) State.setState(State.PLAY);
            playerInventoryState(key, game);
        }

        private static void handleOptionState(int key, Game game) {
            if (key == KeyEvent.VK_ESCAPE) {
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
                if (key == KeyEvent.VK_W) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.ui.command--;
                    if (game.ui.command < 0) game.ui.command = maxCommand;
                }
                if (key == KeyEvent.VK_S) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.ui.command++;
                    if (game.ui.command > maxCommand) game.ui.command = 0;
                }
            }

            // Down the volume
            if (key == KeyEvent.VK_A) {
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
            if (key == KeyEvent.VK_D) {
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

        private static void handleGameOverState(int key, Game game) {
            if (key == KeyEvent.VK_W) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 1;
            }
            if (key == KeyEvent.VK_S) {
                game.playSound(Assets.getAudio(AudioAssets.SLOT));
                game.ui.command++;
                if (game.ui.command > 1) game.ui.command = 0;
            }
            if (key == KeyEvent.VK_ENTER) {
                if (game.ui.command == 0) {
                    State.setState(State.PLAY);
                    game.reset(false);
                } else if (game.ui.command == 1) {
                    State.setState(State.MAIN);
                    game.reset(true);
                }
            }
        }

        private static void handleTradeState(Keyboard keyboard, int key, Game game) {
            if (game.ui.subState == 0) {
                if (key == KeyEvent.VK_A) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.ui.command--;
                    if (game.ui.command < 0) game.ui.command = 1;
                }
                if (key == KeyEvent.VK_D) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.ui.command++;
                    if (game.ui.command > 1) game.ui.command = 0;
                }
                if (key == KeyEvent.VK_ESCAPE) game.ui.command = 0;
            }
            if (game.ui.subState == 1) {
                npcInventoryState(key, game);
                if (key == KeyEvent.VK_ESCAPE) {
                    keyboard.releaseKey(KeyEvent.VK_ESCAPE);
                    game.ui.subState = 0;
                }
            }
            if (game.ui.subState == 2) {
                playerInventoryState(key, game);
                if (key == KeyEvent.VK_ESCAPE) {
                    keyboard.releaseKey(KeyEvent.VK_ESCAPE);
                    game.ui.subState = 0;
                }
            }
        }

        private static void playerInventoryState(int key, Game game) {
            if (key == KeyEvent.VK_ENTER) game.world.entities.player.inventory.select();
            if (key == KeyEvent.VK_W) {
                if (game.world.entities.player.inventory.playerSlotRow > 0) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.world.entities.player.inventory.playerSlotRow--;
                }
            }
            if (key == KeyEvent.VK_A) {
                if (game.world.entities.player.inventory.playerSlotCol > 0) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.world.entities.player.inventory.playerSlotCol--;
                }
            }
            if (key == KeyEvent.VK_S) {
                if (game.world.entities.player.inventory.playerSlotRow < 3) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.world.entities.player.inventory.playerSlotRow++;
                }
            }
            if (key == KeyEvent.VK_D) {
                if (game.world.entities.player.inventory.playerSlotCol < 4) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.world.entities.player.inventory.playerSlotCol++;
                }
            }
        }

        private static void npcInventoryState(int key, Game game) {
            if (key == KeyEvent.VK_W) {
                if (game.world.entities.player.inventory.npcSlotRow > 0) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.world.entities.player.inventory.npcSlotRow--;
                }
            }
            if (key == KeyEvent.VK_A) {
                if (game.world.entities.player.inventory.npcSlotCol > 0) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.world.entities.player.inventory.npcSlotCol--;
                }
            }
            if (key == KeyEvent.VK_S) {
                if (game.world.entities.player.inventory.npcSlotRow < 3) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.world.entities.player.inventory.npcSlotRow++;
                }
            }
            if (key == KeyEvent.VK_D) {
                if (game.world.entities.player.inventory.npcSlotCol < 4) {
                    game.playSound(Assets.getAudio(AudioAssets.SLOT));
                    game.world.entities.player.inventory.npcSlotCol++;
                }
            }
        }

    }

}


