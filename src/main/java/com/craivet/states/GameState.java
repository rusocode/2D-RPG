package com.craivet.states;

import com.craivet.World;

import java.awt.*;

public class GameState implements State {

	private final World world;

	public GameState(World world) {
		this.world = world;
	}

	public void update() {
		world.update();
	}

	public void render(Graphics2D g2) {
		world.render(g2);
	}

}
