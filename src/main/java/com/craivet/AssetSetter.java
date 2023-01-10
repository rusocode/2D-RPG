package com.craivet;

import com.craivet.entity.Oldman;
import com.craivet.entity.Slime;

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
		game.mobs[0] = new Slime(game);
		game.mobs[0].worldX = game.tileSize * 23;
		game.mobs[0].worldY = game.tileSize * 32;

		game.mobs[1] = new Slime(game);
		game.mobs[1].worldX = game.tileSize * 23;
		game.mobs[1].worldY = game.tileSize * 37;

	}

}
