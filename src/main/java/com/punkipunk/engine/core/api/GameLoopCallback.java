package com.punkipunk.engine.core.api;

public interface GameLoopCallback {

    /**
     * Actualiza el estado del juego para el frame actual.
     * <p>
     * Este metodo es llamado en cada frame de la animacion.
     */
    void tick();

}
