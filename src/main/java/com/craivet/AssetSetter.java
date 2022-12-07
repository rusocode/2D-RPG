package com.craivet;

import com.craivet.entity.OldMan;

public class AssetSetter {

	Game game;

	public AssetSetter(Game game) {
		this.game = game;
	}

	public void setObject() {

	}

	public void setNPC() {
		game.npcs[0] = new OldMan(game);
		game.npcs[0].worldX = game.tileSize * 21;
		game.npcs[0].worldY = game.tileSize * 21;
	}

}
