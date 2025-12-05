package com.punkipunk.core;

import com.punkipunk.gfx.Renderer2D;
import javafx.scene.Scene;

/**
 * Interfaz que define el contrato comun para implementaciones de juego.
 * <p>
 * Permite que tanto Game (JavaFX) como GameLWJGL (LWJGL) trabajen con GameSystem.
 */

public interface IGame {

    /**
     * Obtiene el sistema de juego.
     */
    GameSystem getGameSystem();

    /**
     * Obtiene el renderer 2D para renderizado.
     *
     * @return Renderer2D para operaciones de dibujo
     */
    Renderer2D getRenderer();

    /**
     * Obtiene la escena (JavaFX Scene).
     * <p>
     * NOTA: Para GameLWJGL esto retornara null ya que no usa JavaFX Scene.
     * <p>
     * Usado principalmente para configuracion de input en la version JavaFX.
     *
     * @return Scene de JavaFX o null si no aplica
     */
    Scene getScene();

}
