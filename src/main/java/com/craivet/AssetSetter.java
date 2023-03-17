package com.craivet;

import com.craivet.entity.Oldman;
import com.craivet.entity.Slime;
import com.craivet.object.*;
import com.craivet.tile.DryTree;

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
		int mapNum = 0, i = 0;
		game.items[mapNum][i++] = new Axe(game, 33, 7);
		game.items[mapNum][i++] = new Axe(game, 21, 23);
		game.items[mapNum][i++] = new Axe(game, 22, 24);
	}

	public void setNPC() {
		int mapNum = 0, i = 0;
		game.npcs[mapNum][i++] = new Oldman(game, 21, 21);
	}

	public void setMOB() {
		int mapNum = 0, i = 0;
		game.mobs[mapNum][i++] = new Slime(game, 23, 42);
		game.mobs[mapNum][i++] = new Slime(game, 24, 37);
		game.mobs[mapNum][i++] = new Slime(game, 34, 42);
		game.mobs[mapNum][i++] = new Slime(game, 38, 42);

		// Si quiere poner mobs en interior1.txt
		// mapNum = 1;
		// game.mobs[mapNum][i++] = new Slime(game, 23, 42);
	}

	public void setInteractiveTile() {
		int mapNum = 0, i = 0;
		game.iTile[mapNum][i++] = new DryTree(game, 28, 21);
		game.iTile[mapNum][i++] = new DryTree(game, 29, 21);
		game.iTile[mapNum][i++] = new DryTree(game, 30, 21);
		game.iTile[mapNum][i++] = new DryTree(game, 31, 21);
		game.iTile[mapNum][i++] = new DryTree(game, 32, 21);
		game.iTile[mapNum][i++] = new DryTree(game, 33, 21);
	}

}
