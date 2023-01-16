package com.craivet;

import com.craivet.entity.Oldman;
import com.craivet.entity.Slime;
import com.craivet.object.Key;

/**
 * Establece las entidades en posiciones especificas del mundo. Esta es la primer clase que se carga antes de ejecutar
 * el Game Loop, para poder actualizar y dibujar las entidades.
 */

public class AssetSetter {

	private final Game game;

	public AssetSetter(Game game) {
		this.game = game;
	}

	public void setObject() {

	}

	public void setNPC() {
		game.npcs[0] = new Oldman(game);
		game.npcs[0].worldX = game.tileSize * 21;
		game.npcs[0].worldY = game.tileSize * 21;
	}

	public void setMOB() {
		int i = 0;

		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = game.tileSize * 21;
		game.mobs[i].worldY = game.tileSize * 38;
		i++;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = game.tileSize * 23;
		game.mobs[i].worldY = game.tileSize * 42;
		i++;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = game.tileSize * 24;
		game.mobs[i].worldY = game.tileSize * 37;
		i++;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = game.tileSize * 34;
		game.mobs[i].worldY = game.tileSize * 42;
		i++;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = game.tileSize * 38;
		game.mobs[i].worldY = game.tileSize * 42;
	}

}
