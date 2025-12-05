package com.punkipunk.gfx.opengl;

import org.joml.Matrix4f;

/**
 * Camara 2D con proyeccion ortografica.
 * <p>
 * Gestiona la matriz de proyeccion para convertir coordenadas del mundo a coordenadas de pantalla.
 */

public class Camera2D {

    private final Matrix4f projectionMatrix;
    private float width;
    private float height;

    /**
     * Crea una nueva camara 2D.
     *
     * @param width  ancho de la ventana/viewport
     * @param height alto de la ventana/viewport
     */
    public Camera2D(float width, float height) {
        this.width = width;
        this.height = height;
        this.projectionMatrix = new Matrix4f();
        updateProjection();
    }

    /**
     * Actualiza las dimensiones de la camara (llamar cuando cambie el tamaño de la ventana).
     */
    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
        updateProjection();
    }

    /**
     * Obtiene la matriz de proyeccion actual.
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /**
     * Actualiza la matriz de proyeccion ortografica.
     * <p>
     * Crea una proyeccion donde:
     * <ul>
     *  <li>(0, 0) esta en la esquina superior izquierda
     *  <li>X positivo va hacia la derecha
     *  <li>Y positivo va hacia abajo
     * </ul>
     * Esto coincide con el sistema de coordenadas de JavaFX Canvas. TODO: hace falta que coincida con el sistema de coordenadas dce JavaFX?
     */
    private void updateProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0, width, height, 0, -1, 1);
    }

}
