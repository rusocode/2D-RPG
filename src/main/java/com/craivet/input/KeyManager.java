package com.craivet.input;

import com.craivet.Game;
import com.craivet.entity.EntityManager;

import java.awt.event.*;

import static com.craivet.utils.Constants.*;

public class KeyManager extends KeyAdapter {

	private final Game game;
	private final EntityManager entityManager;

	public boolean w, a, s, d, enter, f, l, t;

	private int lastKey = -1;
	public static final int MAX_KEYCODE = 255;

	public KeyManager(Game game, EntityManager entityManager) {
		this.game = game;
		this.entityManager = entityManager;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < 0)
			throw new IllegalArgumentException("keycode cannot be negative, keycode: " + e.getKeyCode());
		if (e.getKeyCode() > MAX_KEYCODE)
			throw new IllegalArgumentException("keycode cannot be greater than 255, keycode: " + e.getKeyCode());
		if (lastKey != e.getKeyCode()) {
			lastKey = e.getKeyCode();
			if (game.gameState == TITLE_STATE) titleState(lastKey);
			else if (game.gameState == PLAY_STATE) playState(lastKey);
			else if (game.gameState == PAUSE_STATE) pauseState(lastKey);
			else if (game.gameState == DIALOGUE_STATE) dialogueState(lastKey);
			else if (game.gameState == CHARACTER_STATE) characterState(lastKey);
			else if (game.gameState == OPTION_STATE) optionState(lastKey);
			else if (game.gameState == GAME_OVER_STATE) gameOverState(lastKey);
			else if (game.gameState == TRADE_STATE) tradeState(lastKey);
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
				game.ui.commandNum--;
				if (game.ui.commandNum < 0) game.ui.commandNum = 2;
			}
			if (code == KeyEvent.VK_S) {
				game.ui.commandNum++;
				if (game.ui.commandNum > 2) game.ui.commandNum = 0;
			}
			if (code == KeyEvent.VK_ENTER) {
				if (game.ui.commandNum == 0) {
					// entityManager.playSound(sound_spawn);
					game.gameState = PLAY_STATE; // entityManager.ui.titleScreenState = SELECTION_SCREEN;
					// entityManager.restart();
					// entityManager.playMusic(blue_boy_adventure);
				}
				// if (entityManager.ui.commandNum == 1) {}
				if (game.ui.commandNum == 2) System.exit(0);
			}
		} else if (game.ui.titleScreenState == SELECTION_SCREEN) {
			if (code == KeyEvent.VK_W) {
				game.ui.commandNum--;
				if (game.ui.commandNum < 0) game.ui.commandNum = 3;
			}
			if (code == KeyEvent.VK_S) {
				game.ui.commandNum++;
				if (game.ui.commandNum > 3) game.ui.commandNum = 0;
			}
			if (code == KeyEvent.VK_ENTER) {
				if (game.ui.commandNum == 0 || game.ui.commandNum == 1 || game.ui.commandNum == 2)
					game.gameState = PLAY_STATE;
				if (game.ui.commandNum == 3) {
					game.ui.commandNum = 0;
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
		if (code == KeyEvent.VK_P) game.gameState = PAUSE_STATE;
		if (code == KeyEvent.VK_C) game.gameState = CHARACTER_STATE;
		if (code == KeyEvent.VK_ESCAPE) game.gameState = OPTION_STATE;
		/* Necesita guardar el archivo de texto editado presionando Ctrl + F9 o seleccionando Build > Build Project. Lo
		 * que reconstruira el proyecto y puede aplicar el cambio presionando la tecla R. */
		if (code == KeyEvent.VK_R) {
			switch (game.map) {
				case 0:
					entityManager.world.loadMap("maps/map1.txt", 0);
					break;
				case 1:
					entityManager.world.loadMap("maps/interior1.txt", 1);
					break;
			}
		}
	}

	private void pauseState(int code) {
		if (code == KeyEvent.VK_P) game.gameState = PLAY_STATE;
	}

	private void dialogueState(int code) {
		if (code == KeyEvent.VK_ENTER) game.gameState = PLAY_STATE;
	}

	private void characterState(int code) {
		if (code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) game.gameState = PLAY_STATE;
		playerInventory(code);
		if (code == KeyEvent.VK_ENTER) entityManager.player.selectItem();
	}

	private void optionState(int code) {
		if (code == KeyEvent.VK_ESCAPE) game.gameState = PLAY_STATE;
		if (code == KeyEvent.VK_ENTER) enter = true;

		int maxCommandNum = 0;
		switch (game.ui.subState) {
			case 0:
				maxCommandNum = 5;
				break;
			case 3:
				maxCommandNum = 1;
				break;
		}

		/* Si la seleccion de comandos esta en los subestados de fullScreen control, entonces no ejecuta las
		 * siguientes instrucciones ya que la seleccion solo se mantiene en el back. */
		if (game.ui.subState == 0 || game.ui.subState == 3) {
			if (code == KeyEvent.VK_W) {
				// entityManager.playSound(sound_cursor);
				game.ui.commandNum--;
				if (game.ui.commandNum < 0) game.ui.commandNum = maxCommandNum;
			}
			if (code == KeyEvent.VK_S) {
				// entityManager.playSound(sound_cursor);
				game.ui.commandNum++;
				if (game.ui.commandNum > maxCommandNum) game.ui.commandNum = 0;
			}
		}

		// Para bajar el volumen
		if (code == KeyEvent.VK_A) {
			if (game.ui.subState == 0) {
				if (game.ui.commandNum == 1 && game.music.volumeScale > 0) { // Musica
					game.music.volumeScale--;
					// TODO Hace falta esto aca?
					game.music.checkVolume(); // Cambia el volumen de la musica cuando ya se esta reproduciendo
					// game.playSound(sound_cursor);
				}
				if (game.ui.commandNum == 2 && game.sound.volumeScale > 0) { // Sonido
					game.sound.volumeScale--;
					// game.playSound(sound_cursor);
				}
			}
		}
		// Para subir el volumen
		if (code == KeyEvent.VK_D) {
			if (game.ui.subState == 0) {
				if (game.ui.commandNum == 1 && game.music.volumeScale < 5) {
					game.music.volumeScale++;
					game.music.checkVolume();
					// game.playSound(sound_cursor);
				}
				if (game.ui.commandNum == 2 && game.sound.volumeScale < 5) { // Sonido
					game.sound.volumeScale++;
					// game.playSound(sound_cursor);
				}
			}
		}

	}

	private void gameOverState(int code) {
		if (code == KeyEvent.VK_W) {
			// game.playSound(sound_cursor);
			game.ui.commandNum--;
			if (game.ui.commandNum < 0) game.ui.commandNum = 1;
		}
		if (code == KeyEvent.VK_S) {
			// entityManager.playSound(sound_cursor);
			game.ui.commandNum++;
			if (game.ui.commandNum > 1) game.ui.commandNum = 0;
		}
		if (code == KeyEvent.VK_ENTER) {
			if (game.ui.commandNum == 0) {
				game.gameState = PLAY_STATE;
				// game.retry();
				// game.playMusic(music_blue_boy_adventure);
			} else if (game.ui.commandNum == 1) {
				game.gameState = TITLE_STATE;
				// game.restart();
			}
		}
	}

	private void tradeState(int code) {
		if (code == KeyEvent.VK_ENTER) enter = true;
		if (game.ui.subState == 0) {
			if (code == KeyEvent.VK_W) {
				// entityManager.playSound(sound_cursor);
				game.ui.commandNum--;
				if (game.ui.commandNum < 0) game.ui.commandNum = 1;
			}
			if (code == KeyEvent.VK_S) {
				// entityManager.playSound(sound_cursor);
				game.ui.commandNum++;
				if (game.ui.commandNum > 1) game.ui.commandNum = 0;
			}
			if (code == KeyEvent.VK_ESCAPE) {
				game.ui.commandNum = 0;
				game.gameState = PLAY_STATE;
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
				// entityManager.playSound(sound_cursor);
				game.ui.playerSlotRow--;
			}
		}
		if (code == KeyEvent.VK_A) {
			if (game.ui.playerSlotCol > 0) {
				// entityManager.playSound(sound_cursor);
				game.ui.playerSlotCol--;
			}
		}
		if (code == KeyEvent.VK_S) {
			if (game.ui.playerSlotRow < 3) {
				// entityManager.playSound(sound_cursor);
				game.ui.playerSlotRow++;
			}
		}
		if (code == KeyEvent.VK_D) {
			if (game.ui.playerSlotCol < 4) {
				// entityManager.playSound(sound_cursor);
				game.ui.playerSlotCol++;
			}
		}
	}

	private void npcInventory(int code) {
		if (code == KeyEvent.VK_W) {
			if (game.ui.npcSlotRow > 0) {
				// entityManager.playSound(sound_cursor);
				game.ui.npcSlotRow--;
			}
		}
		if (code == KeyEvent.VK_A) {
			if (game.ui.npcSlotCol > 0) {
				// entityManager.playSound(sound_cursor);
				game.ui.npcSlotCol--;
			}
		}
		if (code == KeyEvent.VK_S) {
			if (game.ui.npcSlotRow < 3) {
				// entityManager.playSound(sound_cursor);
				game.ui.npcSlotRow++;
			}
		}
		if (code == KeyEvent.VK_D) {
			if (game.ui.npcSlotCol < 4) {
				// entityManager.playSound(sound_cursor);
				game.ui.npcSlotCol++;
			}
		}
	}

}
