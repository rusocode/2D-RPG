package com.craivet;

import com.craivet.entity.Entity;

import static com.craivet.utils.Constants.*;

public class EventHandler {

	private final Game game;
	private final EventRect[][][] eventRect;

	/* El evento no sucede de nuevo si el player no se encuentra a 1 tile de distancia. Esta mecanica evita que el
	 * evento se repita en el mismo lugar. */
	public int previousEventX, previousEventY;
	private boolean canTouchEvent = true;
	public int tempMap, tempCol, tempRow;

	public EventHandler(Game game) {
		this.game = game;

		eventRect = new EventRect[MAX_MAP][MAX_WORLD_ROW][MAX_WORLD_COL];

		// Crea un evento con area solida para cada tile
		for (int map = 0; map < MAX_MAP; map++) {
			for (int row = 0; row < MAX_WORLD_ROW; row++) {
				for (int col = 0; col < MAX_WORLD_COL; col++) {
					eventRect[map][row][col] = new EventRect();
					eventRect[map][row][col].x = 23;
					eventRect[map][row][col].y = 23;
					eventRect[map][row][col].width = 2;
					eventRect[map][row][col].height = 2;
					eventRect[map][row][col].eventRectDefaultX = eventRect[map][row][col].x;
					eventRect[map][row][col].eventRectDefaultY = eventRect[map][row][col].y;
				}
			}
		}

	}

	/**
	 * Verifica los eventos.
	 */
	public void checkEvent() {

		// Verifica si el player esta a mas de 1 tile de distancia del ultimo evento
		int xDistance = Math.abs(game.player.worldX - previousEventX);
		int yDistance = Math.abs(game.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if (distance > TILE_SIZE) canTouchEvent = true;

		if (canTouchEvent) {
			// El else if se utiliza para evitar que el siguiente if se llame inmediatamente en el caso de la teleport
			if (hit(0, 27, 16, DIR_RIGHT)) damagePit();
			else if (hit(0, 23, 12, DIR_UP)) healingPool();
			else if (hit(0, 10, 39, DIR_ANY)) teleport(1, 12, 13);
			else if (hit(1, 12, 13, DIR_ANY)) teleport(0, 10, 39);
			else if (hit(1, 12, 9, DIR_UP)) speak(game.npcs[1][0]);
		}

	}

	/**
	 * Verifica si el area solida del player colisiona con el evento de la posicion especificada.
	 */
	public boolean hit(int map, int col, int row, int reqDirection) {
		boolean hit = false;

		if (map == game.currentMap) {
			game.player.bodyArea.x += game.player.worldX;
			game.player.bodyArea.y += game.player.worldY;
			eventRect[map][row][col].x += col * TILE_SIZE;
			eventRect[map][row][col].y += row * TILE_SIZE;

			// Si el player colisiona con el evento
			if (game.player.bodyArea.intersects(eventRect[map][row][col])) {
				if (game.player.direction == reqDirection || reqDirection == DIR_ANY) {
					hit = true;
					// En base a esta informacion, podemos verificar la distancia entre el player y el ultimo evento
					previousEventX = game.player.worldX;
					previousEventY = game.player.worldY;
				}
			}

			game.player.bodyArea.x = game.player.bodyAreaDefaultX;
			game.player.bodyArea.y = game.player.bodyAreaDefaultY;
			eventRect[map][row][col].x = eventRect[map][row][col].eventRectDefaultX;
			eventRect[map][row][col].y = eventRect[map][row][col].eventRectDefaultY;
		}

		return hit;

	}

	/**
	 * Resta vida al player si callo en la foza.
	 */
	private void damagePit() {
		game.gameState = DIALOGUE_STATE;
		game.ui.currentDialogue = "You fall into a pit!";
		game.player.life--;
		canTouchEvent = false;
	}

	/**
	 * Regenera vida al player si toma agua.
	 */
	public void healingPool() {
		if (game.keyH.enter) {
			game.gameState = DIALOGUE_STATE;
			game.player.attackCanceled = true; // No puede atacar si regenera vida
			game.ui.currentDialogue = "You drink the water.\nYour life has been recovered.";
			game.player.life = game.player.maxLife;
			game.aSetter.setMOB();
		}
	}

	public void teleport(int map, int col, int row) {
		game.gameState = TRANSITION_STATE;
		tempMap = map;
		tempCol = col;
		tempRow = row;
		canTouchEvent = false;
	}

	public void speak(Entity entity) {
		if (game.keyH.enter) {
			game.gameState = DIALOGUE_STATE;
			game.player.attackCanceled = true;
			entity.speak();
		}
	}

}
