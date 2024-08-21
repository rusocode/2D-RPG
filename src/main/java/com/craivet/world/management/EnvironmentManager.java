package com.craivet.world.management;

import com.craivet.input.keyboard.Key;
import com.craivet.world.World;
import com.craivet.world.environment.Lighting;
import com.craivet.states.Renderable;

import java.awt.*;

public class EnvironmentManager implements Renderable {

    private final World world;
    public final Lighting lighting;

    public EnvironmentManager(World world) {
        this.world = world;
        lighting = new Lighting(world);
    }

    @Override
    public void update() {
        lighting.update();
    }

    @Override
    public void render(Graphics2D g2) {
        if (!world.entities.player.game.keyboard.isKeyToggled(Key.TEST)) lighting.render(g2);
    }
}
