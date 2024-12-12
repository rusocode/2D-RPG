package com.punkipunk.gui.container;

/**
 * Patron Observer para notificar los cambios en el contenedor.
 */

@FunctionalInterface
public interface ContainerListener {

    /**
     * Se invoca cuando el contenido del contenedor cambia (items agregados, removidos o modificados).
     */
    void onContainerChanged();

}
