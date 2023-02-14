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
		game.objs[i] = new Axe(game);
		game.objs[i].worldX = TILE_SIZE * 33;
		game.objs[i++].worldY = TILE_SIZE * 21;
		game.objs[i] = new ShieldBlue(game);
		game.objs[i].worldX = TILE_SIZE * 35;
		game.objs[i++].worldY = TILE_SIZE * 21;
		game.objs[i] = new PotionRed(game);
		game.objs[i].worldX = TILE_SIZE * 22;
		game.objs[i].worldY = TILE_SIZE * 27;
	}

	public void setNPC() {
		game.npcs[0] = new Oldman(game);
		game.npcs[0].worldX = TILE_SIZE * 21;
		game.npcs[0].worldY = TILE_SIZE * 21;
	}

	public void setMOB() {
		int i = 0;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 21;
		game.mobs[i++].worldY = TILE_SIZE * 19;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 23;
		game.mobs[i++].worldY = TILE_SIZE * 42;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 24;
		game.mobs[i++].worldY = TILE_SIZE * 37;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 34;
		game.mobs[i++].worldY = TILE_SIZE * 42;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 38;
		game.mobs[i].worldY = TILE_SIZE * 42;
	}

	public void setInteractiveTile() {
		int i = 0;
		game.iTile[i++] = new DryTree(game, 27, 12);
		game.iTile[i++] = new DryTree(game, 28, 12);
		game.iTile[i++] = new DryTree(game, 29, 12);
		game.iTile[i++] = new DryTree(game, 30, 12);
		game.iTile[i++] = new DryTree(game, 31, 12);
		game.iTile[i++] = new DryTree(game, 32, 12);
		game.iTile[i++] = new DryTree(game, 33, 12);

		game.iTile[i++] = new DryTree(game, 30, 20);
		game.iTile[i++] = new DryTree(game, 30, 21);
		game.iTile[i++] = new DryTree(game, 30, 22);
		game.iTile[i++] = new DryTree(game, 20, 20);
		game.iTile[i++] = new DryTree(game, 20, 21);
		game.iTile[i++] = new DryTree(game, 20, 22);
		game.iTile[i++] = new DryTree(game, 22, 24);
		game.iTile[i++] = new DryTree(game, 23, 24);
		game.iTile[i] = new DryTree(game, 24, 24);

	}

}
