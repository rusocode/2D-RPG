package com.punkipunk.world.system;

import com.punkipunk.input.keyboard.Key;
import com.punkipunk.world.World;
import com.punkipunk.world.environment.Lighting;
import javafx.scene.canvas.GraphicsContext;

public class EnvironmentSystem {

    public final Lighting lighting;
    private final World world;

    public EnvironmentSystem(World world) {
        this.world = world;
        lighting = new Lighting(world);
    }

    public void update() {
        lighting.update();
    }

    public void render(GraphicsContext context) {
        if (!world.entitySystem.player.game.getGameSystem().keyboard.isKeyToggled(Key.TEST)) lighting.render(context);
    }

}
