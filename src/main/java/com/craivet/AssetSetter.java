package com.craivet;

import com.craivet.object.OBJ_Boots;
import com.craivet.object.OBJ_Chest;
import com.craivet.object.OBJ_Door;
import com.craivet.object.OBJ_Key;

public class AssetSetter {

	Game game;

	public AssetSetter(Game game) {
		this.game = game;
	}

	public void setObject() {
		game.objs[0] = new OBJ_Key(game);
		game.objs[0].worldX = 23 * game.tileSize;
		game.objs[0].worldY = 7 * game.tileSize;

		game.objs[1] = new OBJ_Key(game);
		game.objs[1].worldX = 23 * game.tileSize;
		game.objs[1].worldY = 40 * game.tileSize;

		game.objs[2] = new OBJ_Key(game);
		game.objs[2].worldX = 37 * game.tileSize;
		game.objs[2].worldY = 7 * game.tileSize;

		game.objs[3] = new OBJ_Door(game);
		game.objs[3].worldX = 10 * game.tileSize;
		game.objs[3].worldY = 11 * game.tileSize;

		game.objs[4] = new OBJ_Door(game);
		game.objs[4].worldX = 8 * game.tileSize;
		game.objs[4].worldY = 28 * game.tileSize;

		game.objs[5] = new OBJ_Door(game);
		game.objs[5].worldX = 12 * game.tileSize;
		game.objs[5].worldY = 22 * game.tileSize;

		game.objs[6] = new OBJ_Chest(game);
		game.objs[6].worldX = 10 * game.tileSize;
		game.objs[6].worldY = 7 * game.tileSize;

		game.objs[7] = new OBJ_Boots(game);
		game.objs[7].worldX = 25 * game.tileSize;
		game.objs[7].worldY = 19 * game.tileSize;

	}

}
