package com.craivet.environment;

import com.craivet.Game;

import java.awt.*;

public class EnvironmentManager {

    private final Game game;
    private Lighting lighting;

    public EnvironmentManager(Game game) {
        this.game = game;
    }

    public void setup() {
        lighting = new Lighting(game);
    }

    public void update() {
        lighting.update();
    }

    public void draw(Graphics2D g2) {
        lighting.draw(g2);
    }
}
