package com.craivet;

import java.awt.*;

public class EventHandler {

	Game game;
	Rectangle eventRect;
	int eventRectDefaultX, eventRectDefaultY;

	public EventHandler(Game game) {
		this.game = game;

		eventRect = new Rectangle();
		eventRect.x = 23;
		eventRect.y = 23;
		eventRect.width = 2;
		eventRect.height = 2;
		eventRectDefaultX = eventRect.x;
		eventRectDefaultY = eventRect.y;
	}

	public void checkEvent() {
		// if (hit(27, 16, "right")) damagePit(game.dialogueState);
		if (hit(27, 16, "right")) teleport(game.dialogueState);
		if (hit(23, 12, "up")) healingPool(game.dialogueState);
	}

	public boolean hit(int eventCol, int eventRow, String reqDirection) {
		boolean hit = false;

		game.player.solidArea.x = game.player.worldX + game.player.solidArea.x;
		game.player.solidArea.y = game.player.worldY + game.player.solidArea.y;
		eventRect.x = eventCol * game.tileSize + eventRect.x;
		eventRect.y = eventRow * game.tileSize + eventRect.y;

		if (game.player.solidArea.intersects(eventRect)) {
			if (game.player.direction.contentEquals(reqDirection) || game.player.direction.contentEquals("any")) {
				hit = true;
			}
		}

		game.player.solidArea.x = game.player.solidAreaDefaultX;
		game.player.solidArea.y = game.player.solidAreaDefaultY;
		eventRect.x = eventRectDefaultX;
		eventRect.y = eventRectDefaultY;

		return hit;

	}

	private void damagePit(int gameState) {
		game.gameState = gameState;
		game.ui.currentDialogue = "You fall into a pit!";
		game.player.life--;
	}

	public void teleport(int gameState) {
		game.gameState = gameState;
		game.player.worldX = game.tileSize * 37;
		game.player.worldY = game.tileSize * 10;
		game.ui.currentDialogue = "You teleported to position\n x=" + game.player.worldX / game.tileSize + " y=" + game.player.worldY / game.tileSize;
	}

	public void healingPool(int gameState) {
		if (game.keyHandler.enter) {
			game.gameState = gameState;
			game.ui.currentDialogue = "You drink the water.\nYour life has been recovered.";
			game.player.life = game.player.maxLife;
		}
	}

}
