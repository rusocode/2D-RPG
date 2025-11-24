package com.punkipunk.core;

import com.punkipunk.Minimap;
import com.punkipunk.UI;
import com.punkipunk.core.api.Renderable;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;

public record Renderer(World world, UI ui, Minimap minimap) implements Renderable {

    @Override
    public void render(GraphicsContext context) {
        world.render(context);
        minimap.render(context);
        ui.render(context);
    }

}
