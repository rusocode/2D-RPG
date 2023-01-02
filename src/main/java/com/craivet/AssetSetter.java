package com.craivet;

import com.craivet.entity.OldMan;
import com.craivet.object.OBJ_Door;
import com.craivet.object.OBJ_Key;

public class AssetSetter {

	private final Game game;

	public AssetSetter(Game game) {
		this.game = game;
	}

	public void setObject() {
		game.objs[0] = new OBJ_Door(game);
		game.objs[0].worldX = game.tileSize * 21;
		game.objs[0].worldY = game.tileSize * 22;

		game.objs[1] = new OBJ_Door(game);
		game.objs[1].worldX = game.tileSize * 23;
		game.objs[1].worldY = game.tileSize * 25;

		game.objs[2] = new OBJ_Key(game);
		game.objs[2].worldX = game.tileSize * 25;
		game.objs[2].worldY = game.tileSize * 21;

	}

	public void setNPC() {
		game.npcs[0] = new OldMan(game);
		game.npcs[0].worldX = game.tileSize * 21;
		game.npcs[0].worldY = game.tileSize * 21;

		game.npcs[1] = new OldMan(game);
		game.npcs[1].worldX = game.tileSize * 11;
		game.npcs[1].worldY = game.tileSize * 21;

		game.npcs[2] = new OldMan(game);
		game.npcs[2].worldX = game.tileSize * 31;
		game.npcs[2].worldY = game.tileSize * 21;

		game.npcs[3] = new OldMan(game);
		game.npcs[3].worldX = game.tileSize * 26;
		game.npcs[3].worldY = game.tileSize * 21;
	}

}
