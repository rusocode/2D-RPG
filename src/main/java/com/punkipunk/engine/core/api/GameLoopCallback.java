package com.punkipunk.engine.core.api;

/**
 * Interfaz funcional que define el comportamiento de actualizacion para el bucle principal del juego.
 */

@FunctionalInterface
public interface GameLoopCallback {

    /**
     * Actualiza el estado del juego para el frame actual.
     * <p>
     * Este metodo es llamado en cada frame de la animacion.
     */
    void tick();

}
