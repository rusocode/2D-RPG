package com.punkipunk.input.keyboard;

import com.punkipunk.Game;

/**
 * <p>
 * Listener para procesar eventos de teclas presionadas en diferentes estados del juego. Cada implementacion define el
 * comportamiento especifico para un estado del juego cuando se presiona una tecla.
 */

@FunctionalInterface
public interface KeyPressListener {

    /**
     * Invocado cuando se presiona una tecla en el estado actual del juego.
     * <p>
     * El prefijo {@code on} es mas apropiado para metodos que responden a eventos.
     *
     * @param key  la tecla presionada
     * @param game referencia al juego actual
     */
    void onKeyPress(Key key, Game game);

}
