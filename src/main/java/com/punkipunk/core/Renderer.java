package com.punkipunk.core;

import com.punkipunk.Minimap;
import com.punkipunk.UI;
import com.punkipunk.core.api.Renderable;
import com.punkipunk.gfx.Renderer2D;
import com.punkipunk.world.World;

/**
 * TODO Se puede unir con Renderer2D?
 */

public record Renderer(World world, UI ui, Minimap minimap) implements Renderable {

    @Override
    public void render(Renderer2D renderer) {
        world.render(renderer);
        minimap.render(renderer);
        ui.render(renderer);
    }

}
