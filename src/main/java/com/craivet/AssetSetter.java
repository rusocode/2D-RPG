package com.craivet;

import com.craivet.entity.Oldman;
import com.craivet.entity.Slime;
import com.craivet.object.*;

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
		game.objs[i] = new Gold(game);
		game.objs[i].worldX = TILE_SIZE * 25;
		game.objs[i].worldY = TILE_SIZE * 23;
		i++;
		game.objs[i] = new Gold(game);
		game.objs[i].worldX = TILE_SIZE * 21;
		game.objs[i].worldY = TILE_SIZE * 19;
		i++;
		game.objs[i] = new Gold(game);
		game.objs[i].worldX = TILE_SIZE * 26;
		game.objs[i].worldY = TILE_SIZE * 21;
		i++;
		game.objs[i] = new Axe(game);
		game.objs[i].worldX = TILE_SIZE * 33;
		game.objs[i].worldY = TILE_SIZE * 21;
		i++;
		game.objs[i] = new ShieldBlue(game);
		game.objs[i].worldX = TILE_SIZE * 35;
		game.objs[i].worldY = TILE_SIZE * 21;
		i++;
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
		game.mobs[i].worldY = TILE_SIZE * 38;
		i++;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 23;
		game.mobs[i].worldY = TILE_SIZE * 42;
		i++;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 24;
		game.mobs[i].worldY = TILE_SIZE * 37;
		i++;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 34;
		game.mobs[i].worldY = TILE_SIZE * 42;
		i++;
		game.mobs[i] = new Slime(game);
		game.mobs[i].worldX = TILE_SIZE * 38;
		game.mobs[i].worldY = TILE_SIZE * 42;
	}

}
