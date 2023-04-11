package com.craivet.environment;

import com.craivet.Game;

import java.awt.*;

public class EnvironmentManager {

    Game game;
    Lighting lighting;

    public EnvironmentManager(Game game) {
        this.game = game;
    }

    public void setup() {
        lighting = new Lighting(game, 500);
    }

    public void draw(Graphics2D g2) {
        lighting.draw(g2);
    }
}
