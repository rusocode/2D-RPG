package com.punkipunk.core;

import com.punkipunk.core.api.GameLoopCallback;
import javafx.animation.AnimationTimer;

/**
 * <p>
 * Clase que implementa el bucle principal del juego extendiendo de AnimationTimer. Maneja la ejecucion continua del juego
 * llamando al callback en cada frame.
 */

public class GameLoop extends AnimationTimer {

    /** Callback que sera ejecutado en cada frame del juego */
    private final GameLoopCallback callback;

    /**
     * Constructor que inicializa el bucle del juego con un callback especifico.
     *
     * @param callback el objeto que implementa GameLoopCallback y que sera ejecutado en cada frame
     */
    public GameLoop(GameLoopCallback callback) {
        this.callback = callback;
    }

    /**
     * Metodo que se ejecuta en cada frame de la animacion.
     * <p>
     * Delega la actualizacion del estado del juego al callback.
     *
     * @param now el timestamp actual en nanosegundos
     */
    @Override
    public void handle(long now) {
        callback.tick();
    }

}
