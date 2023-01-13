package com.craivet;

public class EventHandler {

	Game game;
	EventRect[][] eventRect;

	/* El evento no sucede de nuevo si el player no se encuentra a 1 tile de distancia. Esta mecanica evita que el
	 * evento se repita en el mismo lugar. */
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;

	public EventHandler(Game game) {
		this.game = game;

		eventRect = new EventRect[game.maxWorldCol][game.maxWorldRow];

		// Crea un evento para cada tile del mapa
		for (int row = 0; row < game.maxWorldRow; row++) {
			for (int col = 0; col < game.maxWorldCol; col++) {
				// Ahora cada evento tiene un area solida
				eventRect[col][row] = new EventRect();
				eventRect[col][row].x = 23;
				eventRect[col][row].y = 23;
				eventRect[col][row].width = 2;
				eventRect[col][row].height = 2;
				eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
				eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
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
		if (distance > game.tileSize) canTouchEvent = true;

		if (canTouchEvent) {
			if (hit(27, 16, "right")) damagePit(27, 16, game.dialogueState);
			// if (hit(27, 16, "right")) teleport(game.dialogueState);
			if (hit(23, 12, "up")) healingPool(23, 12, game.dialogueState);
		}

	}

	/**
	 * Verifica si el area solida del player colisiona con el evento de la posicion especificada.
	 */
	public boolean hit(int col, int row, String reqDirection) {
		boolean hit = false;

		game.player.solidArea.x = game.player.worldX + game.player.solidArea.x;
		game.player.solidArea.y = game.player.worldY + game.player.solidArea.y;
		eventRect[col][row].x = col * game.tileSize + eventRect[col][row].x;
		eventRect[col][row].y = row * game.tileSize + eventRect[col][row].y;

		// Si el player colisiona con el evento
		if (game.player.solidArea.intersects(eventRect[col][row])) {
			if (game.player.direction.contentEquals(reqDirection) || game.player.direction.contentEquals("any")) {
				hit = true;
				// En base a esta informacion, podemos verificar la distancia entre el player y el ultimo evento
				previousEventX = game.player.worldX;
				previousEventY = game.player.worldY;
			}
		}

		game.player.solidArea.x = game.player.solidAreaDefaultX;
		game.player.solidArea.y = game.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

		return hit;

	}

	/**
	 * Resta vida al player si callo en la foza.
	 */
	private void damagePit(int col, int row, int gameState) {
		game.gameState = gameState;
		game.ui.currentDialogue = "You fall into a pit!";
		game.player.life--;
		canTouchEvent = false;
	}

	/**
	 * Regenera vida al player si toma agua.
	 */
	public void healingPool(int col, int row, int gameState) {
		if (game.keyHandler.enter) {
			game.player.attackCanceled = true; // No puede atacar si regenera vida
			game.gameState = gameState;
			game.ui.currentDialogue = "You drink the water.\nYour life has been recovered.";
			game.player.life = game.player.maxLife;
			game.aSetter.setMOB();
		}
	}

	public void teleport(int gameState) {
		game.gameState = gameState;
		game.player.worldX = game.tileSize * 37;
		game.player.worldY = game.tileSize * 10;
		game.ui.currentDialogue = "You teleported to position\n x=" + game.player.worldX / game.tileSize + " y=" + game.player.worldY / game.tileSize;
	}

}
