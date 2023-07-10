package com.craivet.world.management;

import com.craivet.world.World;
import com.craivet.world.environment.Lighting;
import com.craivet.states.State;

import java.awt.*;

public class EnvironmentManager implements State {

    public final Lighting lighting;

    public EnvironmentManager(World world) {
        lighting = new Lighting(world);
    }

    @Override
    public void update() {
        lighting.update();
    }

    @Override
    public void render(Graphics2D g2) {
        lighting.render(g2);
    }
}
