package com.punkipunk.world.management;

import com.punkipunk.input.keyboard.Key;
import com.punkipunk.world.World;
import com.punkipunk.world.environment.Lighting;
import javafx.scene.canvas.GraphicsContext;

public class EnvironmentManager {

    public final Lighting lighting;
    private final World world;

    public EnvironmentManager(World world) {
        this.world = world;
        lighting = new Lighting(world);
    }

    public void update() {
        lighting.update();
    }

    public void render(GraphicsContext g2) {
        if (!world.entities.player.game.system.keyboard.isKeyToggled(Key.TEST)) lighting.render(g2);
    }

}
