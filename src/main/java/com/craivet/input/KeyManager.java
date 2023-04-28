package com.craivet.input;

import com.craivet.Game;

import java.awt.event.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class KeyManager extends KeyAdapter {

    private final Game game;
    public boolean w, a, s, d, enter, esc, f, l, t;

    private int lastKey = -1;
    public static final int MAX_KEYCODE = 255;

    public KeyManager(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < 0)
            throw new IllegalArgumentException("keycode cannot be negative, keycode: " + e.getKeyCode());
        if (e.getKeyCode() > MAX_KEYCODE)
            throw new IllegalArgumentException("keycode cannot be greater than 255, keycode: " + e.getKeyCode());
        if (lastKey != e.getKeyCode()) {
            lastKey = e.getKeyCode();
            if (game.state == TITLE_STATE) titleState(lastKey);
            else if (game.state == PLAY_STATE) playState(lastKey);
            else if (game.state == PAUSE_STATE) pauseState(lastKey);
            else if (game.state == DIALOGUE_STATE) dialogueState(lastKey);
            else if (game.state == CHARACTER_STATE) characterState(lastKey);
            else if (game.state == OPTION_STATE) optionState(lastKey);
            else if (game.state == GAME_OVER_STATE) gameOverState(lastKey);
            else if (game.state == TRADE_STATE) tradeState(lastKey);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < 0)
            throw new IllegalArgumentException("keycode cannot be negative, keycode: " + e.getKeyCode());
        if (e.getKeyCode() > MAX_KEYCODE)
            throw new IllegalArgumentException("keycode cannot be greater than 255, keycode: " + e.getKeyCode());
        lastKey = -1;
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) w = false;
        if (code == KeyEvent.VK_A) a = false;
        if (code == KeyEvent.VK_S) s = false;
        if (code == KeyEvent.VK_D) d = false;
        if (code == KeyEvent.VK_F) f = false;
    }

    private void titleState(int code) {
        if (game.ui.titleScreenState == MAIN_SCREEN) {
            if (code == KeyEvent.VK_W) {
                game.playSound(sound_cursor);
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 2;
            }
            if (code == KeyEvent.VK_S) {
                game.playSound(sound_cursor);
                game.ui.command++;
                if (game.ui.command > 2) game.ui.command = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (game.ui.command == 0) {
                    game.state = PLAY_STATE;
                    game.stopMusic();
                    game.playSound(sound_spawn);
                }
                if (game.ui.command == 1) {
                    game.saveLoad.load();
                    game.state = PLAY_STATE;
                    game.stopMusic();
                    game.playSound(sound_spawn);
                }
                if (game.ui.command == 2) System.exit(0);
            }
        } else if (game.ui.titleScreenState == SELECTION_SCREEN) {
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
                    game.state = PLAY_STATE;
                if (game.ui.command == 3) {
                    game.ui.command = 0;
                    game.ui.titleScreenState = MAIN_SCREEN;
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
        if (code == KeyEvent.VK_F) f = true;
        if (code == KeyEvent.VK_L) l = true;
        if (code == KeyEvent.VK_T) t = !t;
        if (code == KeyEvent.VK_P) game.state = PAUSE_STATE;
        if (code == KeyEvent.VK_C) game.state = CHARACTER_STATE;
        if (code == KeyEvent.VK_ESCAPE) game.state = OPTION_STATE;
        if (code == KeyEvent.VK_M) game.minimap.miniMapOn = !game.minimap.miniMapOn;
        /* Necesita guardar el archivo de texto editado presionando Ctrl + F9 o seleccionando Build > Build Project. Lo
         * que reconstruira el proyecto y puede aplicar el cambio presionando la tecla R. */
        if (code == KeyEvent.VK_R) {
            switch (game.getWorld().map) {
                case 0 -> game.getWorld().loadMap("maps/nix.txt", NIX, "Nix");
                case 1 -> game.getWorld().loadMap("maps/nix_indoor01.txt", NIX_INDOOR_01, "Nix Indoor 01");
            }
        }
    }

    private void pauseState(int code) {
        if (code == KeyEvent.VK_P) game.state = PLAY_STATE;
    }

    private void dialogueState(int code) {
        if (code == KeyEvent.VK_ENTER) enter = true;
    }

    private void characterState(int code) {
        if (code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) game.state = PLAY_STATE;
        playerInventory(code);
        if (code == KeyEvent.VK_ENTER) game.getWorld().player.selectItem();
    }

    private void optionState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            game.state = PLAY_STATE;
            game.ui.command = 0;
            game.ui.subState = 0;
        }

        if (code == KeyEvent.VK_ENTER) enter = true;

        int maxCommand = switch (game.ui.subState) {
            case 0 -> 5;
            case 1, 2 -> 1;
            default -> 0;
        };

        /* Si la seleccion de comandos esta en los subestados de fullScreen control, entonces no ejecuta las
         * siguientes instrucciones ya que la seleccion solo se mantiene en el back. */
        if (game.ui.subState == 0 || game.ui.subState == 2) {
            if (code == KeyEvent.VK_W) {
                game.playSound(sound_cursor);
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = maxCommand;
            }
            if (code == KeyEvent.VK_S) {
                game.playSound(sound_cursor);
                game.ui.command++;
                if (game.ui.command > maxCommand) game.ui.command = 0;
            }
        }

        // Para bajar el volumen
        if (code == KeyEvent.VK_A) {
            if (game.ui.subState == 0) {
                if (game.ui.command == 0 && game.music.volumeScale > 0) { // Musica
                    game.music.volumeScale--;
                    // TODO Hace falta esto aca?
                    game.music.checkVolume(); // Cambia el volumen de la musica cuando ya se esta reproduciendo
                    game.playSound(sound_cursor);
                }
                if (game.ui.command == 1 && game.sound.volumeScale > 0) { // Sonido
                    game.sound.volumeScale--;
                    game.playSound(sound_cursor);
                }
            }
        }
        // Para subir el volumen
        if (code == KeyEvent.VK_D) {
            if (game.ui.subState == 0) {
                if (game.ui.command == 0 && game.music.volumeScale < 5) {
                    game.music.volumeScale++;
                    game.music.checkVolume();
                    game.playSound(sound_cursor);
                }
                if (game.ui.command == 1 && game.sound.volumeScale < 5) { // Sonido
                    game.sound.volumeScale++;
                    game.playSound(sound_cursor);
                }
            }
        }

    }

    private void gameOverState(int code) {
        if (code == KeyEvent.VK_W) {
            game.playSound(sound_cursor);
            game.ui.command--;
            if (game.ui.command < 0) game.ui.command = 1;
        }
        if (code == KeyEvent.VK_S) {
            game.playSound(sound_cursor);
            game.ui.command++;
            if (game.ui.command > 1) game.ui.command = 0;
        }
        if (code == KeyEvent.VK_ENTER) {
            if (game.ui.command == 0) {
                game.state = PLAY_STATE;
                game.resetGame(false);
            } else if (game.ui.command == 1) {
                game.state = TITLE_STATE;
                game.resetGame(true);
            }
        }
    }

    private void tradeState(int code) {
        if (code == KeyEvent.VK_ENTER) enter = true;
        if (game.ui.subState == 0) {
            if (code == KeyEvent.VK_A) {
                game.playSound(sound_cursor);
                game.ui.command--;
                if (game.ui.command < 0) game.ui.command = 1;
            }
            if (code == KeyEvent.VK_D) {
                game.playSound(sound_cursor);
                game.ui.command++;
                if (game.ui.command > 1) game.ui.command = 0;
            }
            if (code == KeyEvent.VK_ESCAPE) {
                esc = true;
                game.ui.command = 0;
                game.state = PLAY_STATE;
            }
        }
        if (game.ui.subState == 1) {
            npcInventory(code);
            if (code == KeyEvent.VK_ESCAPE) game.ui.subState = 0;
        }
        if (game.ui.subState == 2) {
            playerInventory(code);
            if (code == KeyEvent.VK_ESCAPE) game.ui.subState = 0;
        }
    }

    private void playerInventory(int code) {
        if (code == KeyEvent.VK_W) {
            if (game.ui.playerSlotRow > 0) {
                game.playSound(sound_cursor);
                game.ui.playerSlotRow--;
            }
        }
        if (code == KeyEvent.VK_A) {
            if (game.ui.playerSlotCol > 0) {
                game.playSound(sound_cursor);
                game.ui.playerSlotCol--;
            }
        }
        if (code == KeyEvent.VK_S) {
            if (game.ui.playerSlotRow < 3) {
                game.playSound(sound_cursor);
                game.ui.playerSlotRow++;
            }
        }
        if (code == KeyEvent.VK_D) {
            if (game.ui.playerSlotCol < 4) {
                game.playSound(sound_cursor);
                game.ui.playerSlotCol++;
            }
        }
    }

    private void npcInventory(int code) {
        if (code == KeyEvent.VK_W) {
            if (game.ui.npcSlotRow > 0) {
                game.playSound(sound_cursor);
                game.ui.npcSlotRow--;
            }
        }
        if (code == KeyEvent.VK_A) {
            if (game.ui.npcSlotCol > 0) {
                game.playSound(sound_cursor);
                game.ui.npcSlotCol--;
            }
        }
        if (code == KeyEvent.VK_S) {
            if (game.ui.npcSlotRow < 3) {
                game.playSound(sound_cursor);
                game.ui.npcSlotRow++;
            }
        }
        if (code == KeyEvent.VK_D) {
            if (game.ui.npcSlotCol < 4) {
                game.playSound(sound_cursor);
                game.ui.npcSlotCol++;
            }
        }
    }

}
