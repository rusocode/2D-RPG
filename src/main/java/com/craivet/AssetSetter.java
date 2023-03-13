package com.craivet;

import com.craivet.entity.Oldman;
import com.craivet.entity.Slime;
import com.craivet.object.*;
import com.craivet.tile.DryTree;

import static com.craivet.utils.Constants.*;

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
		int i = 0;
		game.objs[i++] = new Axe(game, 33, 7);
		game.objs[i++] = new Axe(game, 21, 23);
		game.objs[i++] = new Axe(game, 22, 24);
	}

	public void setNPC() {
		int i = 0;
		game.npcs[i++] = new Oldman(game, 21, 21);
	}

	public void setMOB() {
		int i = 0;
		game.mobs[i++] = new Slime(game, 23, 42);
		game.mobs[i++] = new Slime(game, 24, 37);
		game.mobs[i++] = new Slime(game, 34, 42);
		game.mobs[i++] = new Slime(game, 38, 42);
	}

	public void setInteractiveTile() {
		int i = 0;
		game.iTile[i++] = new DryTree(game, 28, 21);
		game.iTile[i++] = new DryTree(game, 29, 21);
		game.iTile[i++] = new DryTree(game, 30, 21);
		game.iTile[i++] = new DryTree(game, 31, 21);
		game.iTile[i++] = new DryTree(game, 32, 21);
		game.iTile[i++] = new DryTree(game, 33, 21);
	}

}
