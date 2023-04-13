package com.craivet.states;

import com.craivet.UI;
import com.craivet.World;

import java.awt.*;

public class GameState implements State {

	private final World world;
	private final UI ui;

	public GameState(World world, UI ui) {
		this.world = world;
		this.ui = ui;
	}

	public void update() {
		world.update();
	}

	public void render(Graphics2D g2) {
		world.render(g2);
		ui.render(g2);
	}

}
