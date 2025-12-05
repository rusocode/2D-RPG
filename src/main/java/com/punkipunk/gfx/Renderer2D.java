package com.punkipunk.gfx;

import com.punkipunk.gfx.opengl.*;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * API de alto nivel para renderizado 2D.
 * <p>
 * Proporciona una interfaz similar a GraphicsContext pero usando OpenGL internamente. Durante la transicion, mantiene
 * compatibilidad parcial con tipos de JavaFX (Image, Font).
 */

public class Renderer2D {

    private final SpriteBatch spriteBatch;
    private final Camera2D camera;
    private final Shader shader;

    /** Cache de texturas convertidas desde JavaFX Image. */
    private final Map<Image, Texture> textureCache = new HashMap<>();

    /** Estado actual. */
    private Color fillColor = Color.WHITE;
    private Color strokeColor = Color.BLACK;
    private Font currentFont;

    public Renderer2D(int width, int height) {
        camera = new Camera2D(width, height);

        // Crea el shader desde codigo fuente embebido
        shader = new Shader(getVertexShaderSource(), getFragmentShaderSource());

        spriteBatch = new SpriteBatch(shader, camera);

        setupOpenGL();

        System.out.println("Renderer2D initialized (" + width + "x" + height + ")");
    }

    /**
     * Inicia el frame de renderizado.
     * <p>
     * DEBE llamarse al principio de cada frame.
     */
    public void begin() {
        spriteBatch.begin();
    }

    /**
     * Finaliza el frame de renderizado.
     * <p>
     * DEBE llamarse al final de cada frame.
     */
    public void end() {
        spriteBatch.end();
    }

    /**
     * Limpia la pantalla con un color.
     */
    public void clear(Color color) {
        glClearColor(color.r(), color.g(), color.b(), color.a());
        glClear(GL_COLOR_BUFFER_BIT);
    }

    /**
     * Limpia un rectangulo (equivalente a clearRect de GraphicsContext).
     */
    public void clearRect(double x, double y, double width, double height) {
        /* En OpenGL, clearRect se puede simular dibujando un rectangulo negro o simplemente no hacer nada si el fondo ya esta
         * limpio. Por simplicidad, lo dejamos vacio ya que clear() limpia toda la pantalla. */
    }

    // ===== DIBUJO DE IMAGENES =====

    /**
     * Dibuja una textura completa.
     *
     * @param texture textura OpenGL
     * @param x       posicion X
     * @param y       posicion Y
     */
    public void drawImage(Texture texture, double x, double y) {
        spriteBatch.draw(texture, (float) x, (float) y);
    }

    /**
     * Dibuja una textura con tamaño especifico.
     *
     * @param texture textura OpenGL
     * @param x       posicion X
     * @param y       posicion Y
     * @param width   ancho
     * @param height  alto
     */
    public void drawImage(Texture texture, double x, double y, double width, double height) {
        spriteBatch.draw(texture, (float) x, (float) y, (float) width, (float) height);
    }

    /**
     * Dibuja una region de textura (para sprite sheets).
     *
     * @param texture textura OpenGL
     * @param sx      coordenada X de origen en la textura
     * @param sy      coordenada Y de origen en la textura
     * @param sWidth  ancho de la region
     * @param sHeight alto de la region
     * @param dx      posicion X de destino
     * @param dy      posicion Y de destino
     * @param dWidth  ancho de destino
     * @param dHeight alto de destino
     */
    public void drawImage(Texture texture,
                          double sx, double sy, double sWidth, double sHeight,
                          double dx, double dy, double dWidth, double dHeight) {
        // Calcula coordenadas UV normalizadas
        float u0 = (float) (sx / texture.getWidth());
        float v0 = (float) (sy / texture.getHeight());
        float u1 = (float) ((sx + sWidth) / texture.getWidth());
        float v1 = (float) ((sy + sHeight) / texture.getHeight());

        spriteBatch.draw(texture, (float) dx, (float) dy, (float) dWidth, (float) dHeight, u0, v0, u1, v1);
    }

    // ===== DIBUJO DE FORMAS =====

    /**
     * Establece el color de relleno.
     */
    public void setFill(Color color) {
        spriteBatch.setColor(color);
    }

    /**
     * Establece el color de trazo.
     */
    public void setStroke(Color color) {
        this.strokeColor = color;
    }

    /**
     * Dibuja un rectangulo relleno.
     */
    public void fillRect(double x, double y, double width, double height) {
        spriteBatch.setColor(fillColor);
        spriteBatch.fillRect((float) x, (float) y, (float) width, (float) height);
        spriteBatch.resetColor();
    }

    /**
     * Dibuja el contorno de un rectangulo.
     */
    public void strokeRect(double x, double y, double width, double height) {
        // TODO: Implementar strokeRect usando lineas
        // Por ahora, dibuja un rectangulo sin relleno (simplificado)
        // Necesitarias dibujar 4 lineas para el borde
        System.err.println("strokeRect() not fully implemented yet");
    }

    /**
     * Establece el ancho de linea para trazos.
     */
    public void setLineWidth(double width) {
        glLineWidth((float) width);
    }

    // ===== TEXTO =====

    /**
     * Establece la fuente actual.
     * <p>
     * NOTA: El renderizado de fuentes sera implementado mas adelante usando FreeType o STB.
     */
    public void setFont(Font font) {
        this.currentFont = font;
        System.err.println("Text rendering not implemented yet - font set but ignored");
    }

    /**
     * Dibuja texto.
     * <p>
     * NOTA: Por implementar usando FreeType o STB TrueType.
     */
    public void fillText(String text, double x, double y) {
        // TODO: Implementar renderizado de texto
        System.err.println("Text rendering not implemented yet: \"" + text + "\"");
    }

    /**
     * Dibuja texto con trazo.
     */
    public void strokeText(String text, double x, double y) {
        // TODO: Implementar renderizado de texto con trazo
        System.err.println("Text stroke rendering not implemented yet: \"" + text + "\"");
    }

    // ===== GESTION DE TEXTURAS =====

    /**
     * Actualiza las dimensiones cuando cambia el tamaño de la ventana.
     */
    public void resize(int width, int height) {
        camera.resize(width, height);
        glViewport(0, 0, width, height);
    }

    // ===== TRANSFORMACIONES =====

    /**
     * Libera recursos.
     */
    public void cleanup() {
        spriteBatch.cleanup();
        shader.cleanup();

        // Limpia texturas en cache
        for (Texture texture : textureCache.values())
            texture.cleanup();

        textureCache.clear();
    }

    // ===== CONFIGURACION INICIAL =====

    private void setupOpenGL() {
        // Habilita blending para transparencias
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Deshabilita depth test (es 2D)
        glDisable(GL_DEPTH_TEST);

        System.out.println("OpenGL 2D rendering configured");
    }

    // ===== SHADERS EMBEBIDOS =====

    private String getVertexShaderSource() {
        return """
                #version 330 core
                
                layout (location = 0) in vec2 aPosition;
                layout (location = 1) in vec2 aTexCoord;
                layout (location = 2) in vec4 aColor;
                
                out vec2 fTexCoord;
                out vec4 fColor;
                
                uniform mat4 uProjection;
                
                void main() {
                    fTexCoord = aTexCoord;
                    fColor = aColor;
                    gl_Position = uProjection * vec4(aPosition, 0.0, 1.0);
                }
                """;
    }

    private String getFragmentShaderSource() {
        return """
                #version 330 core
                
                in vec2 fTexCoord;
                in vec4 fColor;
                
                out vec4 FragColor;
                
                uniform sampler2D uTexture;
                
                void main() {
                    vec4 texColor = texture(uTexture, fTexCoord);
                    FragColor = texColor * fColor;
                }
                """;
    }

}
