package com.craivet.states;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.input.KeyManager;
import com.craivet.tile.World;

import java.awt.*;

public class GameState implements State {

	public final World world;

	public GameState(Game game) {
		world = new World(game);
	}

	public void update() {
		world.update();
	}

	public void render(Graphics2D g2) {
		world.render(g2);
	}

}
