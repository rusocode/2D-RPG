package com.punkipunk.core;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

/**
 * Interfaz que define el contrato comun para implementaciones de juego. Permite que tanto Game (JavaFX) como GameLWJGL (LWJGL)
 * trabajen con GameSystem.
 */

public interface IGame {

    /**
     * Obtiene el sistema de juego.
     */
    GameSystem getGameSystem();

    /**
     * Obtiene el contexto grafico para renderizado.
     * <p>
     * NOTA: Por ahora retorna GraphicsContext de JavaFX. En el futuro esto sera reemplazado por un contexto OpenGL.
     */
    GraphicsContext getContext();

    /**
     * Obtiene la escena (JavaFX Scene).
     * <p>
     * NOTA: Para GameLWJGL esto retornara null ya que no usa JavaFX Scene. Usado principalmente para configuracion de input en la
     * version JavaFX.
     *
     * @return Scene de JavaFX o null si no aplica
     */
    Scene getScene();

}
