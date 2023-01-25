package com.craivet;

import com.craivet.entity.Oldman;
import com.craivet.entity.Slime;
import com.craivet.object.Axe;
import com.craivet.object.Key;
import com.craivet.object.PotionRed;
import com.craivet.object.ShieldBlue;

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
		game.objs[i] = new Key(game);
		game.objs[i].worldX = game.tileSize * 25;
		game.objs[i].worldY = game.tileSize * 23;
		i++;
		game.objs[i] = new Key(game);
		game.objs[i].worldX = game.tileSize * 21;
		game.objs[i].worldY = game.tileSize * 19;
		i++;
		game.objs[i] = new Key(game);
		game.objs[i].worldX = game.tileSize * 26;
		game.objs[i].worldY = game.tileSize * 21;
		i++;
		game.objs[i] = new Axe(game);
		game.objs[i].worldX = game.tileSize * 33;
		game.objs[i].worldY = game.tileSize * 21;
		i++;
		game.objs[i] = new ShieldBlue(game);
		game.objs[i].worldX = game.tileSize * 35;
		game.objs[i].worldY = game.tileSize * 21;
		i++;
		game.objs[i] = new PotionRed(game);
		game.objs[i].worldX = game.tileSize * 22;
		game.objs[i].worldY = game.tileSize * 27;
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
