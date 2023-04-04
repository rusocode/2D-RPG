package com.craivet;

import com.craivet.entity.EntityManager;
import com.craivet.entity.item.Axe;
import com.craivet.entity.mob.Slime;
import com.craivet.entity.npc.Oldman;
import com.craivet.tile.World;

/**
 * Establece las entidades en el mundo.
 */

public class AssetSetter {

	private final Game game;
	private final World world;
	private final EntityManager entityManager;

	public AssetSetter(Game game, World world, EntityManager entityManager) {
		this.game = game;
		this.world = world;
		this.entityManager = entityManager;
	}

	public void setObject() {
		int map = 0, i = 0;
		entityManager.items[map][i++] = new Axe(game, world, entityManager, 33, 7);
		// game.items[map][i++] = new Axe(game, 21, 23);
		// game.items[map][i++] = new Axe(game, 22, 24);
	}

	public void setNPC() {
		int map = 0, i = 0;
		entityManager.npcs[map][i++] = new Oldman(game ,world, entityManager, 29, 20);

		map = 1;
		i = 0;
		// game.npcs[map][i++] = new Merchant(game, 12, 7);

	}

	public void setMOB() {
		int map = 0, i = 0;
		entityManager.mobs[map][i++] = new Slime(game, world, entityManager, 23, 41);
		// game.mobs[map][i++] = new Slime(game, 24, 37);
		// game.mobs[map][i++] = new Slime(game, 34, 42);
		// game.mobs[map][i++] = new Slime(game, 38, 42);

		// Si quiere poner mobs en interior1.txt
		// mapNum = 1;
		// game.mobs[mapNum][i++] = new Slime(game, 23, 42);
	}

	public void setInteractiveTile() {
		int map = 0, i = 0;
		// game.iTile[map][i++] = new DryTree(game, 28, 21);
		// game.iTile[map][i++] = new DryTree(game, 29, 21);
		// game.iTile[map][i++] = new DryTree(game, 30, 21);
		// game.iTile[map][i++] = new DryTree(game, 31, 21);
		// game.iTile[map][i++] = new DryTree(game, 32, 21);
		//game.iTile[map][i++] = new DryTree(game, 33, 21);
	}

}
