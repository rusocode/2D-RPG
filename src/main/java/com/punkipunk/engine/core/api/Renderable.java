package com.punkipunk.engine.core.api;

import javafx.scene.canvas.GraphicsContext;

@FunctionalInterface
public interface Renderable {

    void render(GraphicsContext context);

}
