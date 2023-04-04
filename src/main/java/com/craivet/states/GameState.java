package com.craivet.states;

import com.craivet.Game;
import com.craivet.input.KeyManager;
import com.craivet.tile.World;

import java.awt.*;

public class GameState {

	public final World world;

	public GameState(Game game, KeyManager key) {
		this.world = new World(game, key);
	}

	public void update() {
		world.update();
	}


	public void render(Graphics2D g2) {
		world.render(g2);
	}

}
