package com.craivet.input;

import java.awt.event.*;

import com.craivet.Game;
import com.craivet.gfx.Assets;

import static com.craivet.utils.Constants.*;

public class KeyHandler extends KeyAdapter {

	private final Game game;
	public boolean w, a, s, d, enter, f, l, t;

	private int lastKey = -1;

	public KeyHandler(Game game) {
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (lastKey != e.getKeyCode()) {
			lastKey = e.getKeyCode();
			if (game.gameState == TITLE_STATE) titleState(lastKey);
			else if (game.gameState == PLAY_STATE) playState(lastKey);
			else if (game.gameState == PAUSE_STATE) pauseState(lastKey);
			else if (game.gameState == DIALOGUE_STATE) dialogueState(lastKey);
			else if (game.gameState == CHARACTER_STATE) characterState(lastKey);
			else if (game.gameState == OPTION_STATE) optionState(lastKey);
			else if (game.gameState == GAME_OVER_STATE) gameOverState(lastKey);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
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
					game.playSound(Assets.spawn);
					game.gameState = PLAY_STATE; // game.ui.titleScreenState = SELECTION_SCREEN;
					game.restart();
					// game.playMusic(Assets.blue_boy_adventure);
				}
				// if (game.ui.commandNum == 1) {}
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
			switch (game.currentMap) {
				case 0:
					game.tileManager.loadMap("maps/map3.txt", 0);
					break;
				case 1:
					game.tileManager.loadMap("maps/interior1.txt", 1);
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
		if (code == KeyEvent.VK_C) game.gameState = PLAY_STATE;
		if (code == KeyEvent.VK_W) {
			if (game.ui.slotRow > 0) {
				game.playSound(Assets.cursor);
				game.ui.slotRow--;
			}
		}
		if (code == KeyEvent.VK_A) {
			if (game.ui.slotCol > 0) {
				game.playSound(Assets.cursor);
				game.ui.slotCol--;
			}
		}
		if (code == KeyEvent.VK_S) {
			if (game.ui.slotRow < 3) {
				game.playSound(Assets.cursor);
				game.ui.slotRow++;
			}
		}
		if (code == KeyEvent.VK_D) {
			if (game.ui.slotCol < 4) {
				game.playSound(Assets.cursor);
				game.ui.slotCol++;
			}
		}
		if (code == KeyEvent.VK_ENTER) game.player.selectItem();
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
				game.playSound(Assets.cursor);
				game.ui.commandNum--;
				if (game.ui.commandNum < 0) game.ui.commandNum = maxCommandNum;
			}
			if (code == KeyEvent.VK_S) {
				game.playSound(Assets.cursor);
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
					game.playSound(Assets.cursor);
				}
				if (game.ui.commandNum == 2 && game.sound.volumeScale > 0) { // Sonido
					game.sound.volumeScale--;
					game.playSound(Assets.cursor);
				}
			}
		}
		// Para subir el volumen
		if (code == KeyEvent.VK_D) {
			if (game.ui.subState == 0) {
				if (game.ui.commandNum == 1 && game.music.volumeScale < 5) {
					game.music.volumeScale++;
					game.music.checkVolume();
					game.playSound(Assets.cursor);
				}
				if (game.ui.commandNum == 2 && game.sound.volumeScale < 5) { // Sonido
					game.sound.volumeScale++;
					game.playSound(Assets.cursor);
				}
			}
		}

	}

	private void gameOverState(int code) {
		if (code == KeyEvent.VK_W) {
			game.playSound(Assets.cursor);
			game.ui.commandNum--;
			if (game.ui.commandNum < 0) game.ui.commandNum = 1;
		}
		if (code == KeyEvent.VK_S) {
			game.playSound(Assets.cursor);
			game.ui.commandNum++;
			if (game.ui.commandNum > 1) game.ui.commandNum = 0;
		}
		if (code == KeyEvent.VK_ENTER) {
			if (game.ui.commandNum == 0) {
				game.gameState = PLAY_STATE;
				game.retry();
				game.playMusic(Assets.blue_boy_adventure);
			} else if (game.ui.commandNum == 1) {
				game.gameState = TITLE_STATE;
				game.retry();
			}
		}
	}

}
