package com.punkipunk.gfx.opengl;

/**
 * Clase simple de color RGBA que reemplaza javafx.scene.paint.Color. Los valores van de 0.0f a 1.0f.
 */

public record Color(float r, float g, float b, float a) {

    /** Colores predefinidos. */
    public static final Color WHITE = new Color(1, 1, 1, 1);
    public static final Color BLACK = new Color(0, 0, 0, 1);
    public static final Color RED = new Color(1, 0, 0, 1);
    public static final Color GREEN = new Color(0, 1, 0, 1);
    public static final Color BLUE = new Color(0, 0, 1, 1);
    public static final Color YELLOW = new Color(1, 1, 0, 1);
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public Color(float r, float g, float b, float a) {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
        this.a = clamp(a);
    }

    /**
     * Crea un color RGB opaco.
     */
    public Color(float r, float g, float b) {
        this(r, g, b, 1.0f);
    }

    /**
     * Crea un color desde valores enteros (0-255).
     */
    public static Color fromRGB(int r, int g, int b) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
    }

    /**
     * Crea un color desde valores enteros (0-255) con alpha.
     */
    public static Color fromRGBA(int r, int g, int b, int a) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    /**
     * Convierte un color de JavaFX a este formato. Util durante la transicion.
     */
    public static Color fromJavaFX(javafx.scene.paint.Color fxColor) {
        return new Color((float) fxColor.getRed(), (float) fxColor.getGreen(), (float) fxColor.getBlue(), (float) fxColor.getOpacity());
    }

    @Override
    public String toString() {
        return String.format("Color(%.2f, %.2f, %.2f, %.2f)", r, g, b, a);
    }

    private float clamp(float value) {
        return Math.max(0.0f, Math.min(1.0f, value));
    }

}