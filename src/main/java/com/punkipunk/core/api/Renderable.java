package com.punkipunk.core.api;

import com.punkipunk.gfx.Renderer2D;

/**
 * Interfaz funcional para objetos que pueden renderizarse.
 */

@FunctionalInterface
public interface Renderable {

    void render(Renderer2D renderer);

}
