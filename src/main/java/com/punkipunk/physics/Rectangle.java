package com.punkipunk.physics;

/**
 * Rectangulo simple para deteccion de colisiones.
 * <p>
 * Reemplaza javafx.scene.shape.Rectangle para compatibilidad con LWJGL.
 */

public class Rectangle {

    private double x, y, width, height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Verifica si este rectangulo intersecta con otro.
     */
    public boolean intersects(Rectangle other) {
        return !(x + width < other.x || other.x + other.width < x || y + height < other.y || other.y + other.height < y);
    }

    // Getters y setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("Rectangle[x=%.1f, y=%.1f, w=%.1f, h=%.1f]", x, y, width, height);
    }

}