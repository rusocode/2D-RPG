package com.punkipunk.core;

import com.punkipunk.Minimap;
import com.punkipunk.UI;
import com.punkipunk.core.api.Renderable;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;

public class Renderer implements Renderable {

    public final World world;
    private final UI ui;
    private final Minimap minimap;

    public Renderer(World world, UI ui, Minimap minimap) {
        this.world = world;
        this.ui = ui;
        this.minimap = minimap;
    }

    @Override
    public void render(GraphicsContext context) {
        world.render(context);
        minimap.render(context);
        ui.render(context);
    }

}
