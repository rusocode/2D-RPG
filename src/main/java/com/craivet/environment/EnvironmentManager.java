package com.craivet.environment;

import com.craivet.World;

import java.awt.*;

/**
 * Administrador del ambiente.
 */

public class EnvironmentManager {

    public final Lighting lighting;

    public EnvironmentManager(World world) {
        lighting = new Lighting(world);
    }

    public void update() {
        lighting.update();
    }

    public void render(Graphics2D g2) {
        lighting.render(g2);
    }
}
