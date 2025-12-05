package com.punkipunk.gfx.opengl;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * SpriteBatch para renderizado eficiente de sprites 2D por lotes.
 * <p>
 * Agrupa multiples sprites con la misma textura en un solo draw call, mejorando significativamente el rendimiento.
 * <p>
 * Estructura de un vertice (9 floats):
 * <ul>
 *  <li>Position (x, y): 2 floats
 *  <li>TexCoord (u, v): 2 floats
 *  <li>Color (r, g, b, a): 4 floats
 * </ul>
 */

public class SpriteBatch {

    private static final int MAX_BATCH_SIZE = 1000; // Maximo de sprites por batch
    private static final int VERTICES_PER_SPRITE = 4; // Quad = 4 vertices
    private static final int INDICES_PER_SPRITE = 6; // 2 triangulos = 6 indices
    private static final int FLOATS_PER_VERTEX = 9; // pos(2) + texCoord(2) + color(4) + texID(1)

    private final int vaoID, vboID, eboID;

    private final FloatBuffer vertexBuffer;
    private final Shader shader;
    private final Camera2D camera;

    private int spriteCount;
    private Texture currentTexture;
    private boolean drawing;

    /** Color actual para tinting. */
    private Color currentColor = Color.WHITE;

    /**
     * Crea un nuevo SpriteBatch.
     *
     * @param shader shader a usar para renderizado
     * @param camera camara 2D para la proyeccion
     */
    public SpriteBatch(Shader shader, Camera2D camera) {
        this.shader = shader;
        this.camera = camera;

        // Crea buffer de vertices
        vertexBuffer = BufferUtils.createFloatBuffer(MAX_BATCH_SIZE * VERTICES_PER_SPRITE * FLOATS_PER_VERTEX);

        // Crea el VAO (Vertex Array Object)
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Crea el VBO (Vertex Buffer Object)
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexBuffer.capacity() * Float.BYTES, GL_DYNAMIC_DRAW);

        // Configura atributos de vertices
        // Posicion (location = 0)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // TexCoord (location = 1)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Color (location = 2)
        glVertexAttribPointer(2, 4, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 4 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // Texture ID (location = 3) - por ahora no se usa pero esta preparado
        glVertexAttribPointer(3, 1, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 8 * Float.BYTES);
        glEnableVertexAttribArray(3);

        // Crea EBO (Element Buffer Object) para indices
        eboID = glGenBuffers();
        IntBuffer indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glBindVertexArray(0);

        System.out.println("SpriteBatch created (max sprites: " + MAX_BATCH_SIZE + ")");
    }

    /**
     * Inicia el batch de renderizado.
     */
    public void begin() {
        if (drawing) throw new IllegalStateException("SpriteBatch.end() must be called before begin()");
        drawing = true;
        spriteCount = 0;
        vertexBuffer.clear();

        // Activa el shader y configura la proyeccion
        shader.bind();
        shader.setUniform("uProjection", camera.getProjectionMatrix());
        shader.setUniform("uTexture", 0); // Texture unit 0
    }

    /**
     * Finaliza el batch y renderiza todo.
     */
    public void end() {
        if (!drawing) throw new IllegalStateException("SpriteBatch.begin() must be called before end()");
        drawing = false;
        flush();
        Shader.unbind();
    }

    /**
     * Establece el color de tint para los siguientes sprites.
     */
    public void setColor(Color color) {
        this.currentColor = color;
    }

    /**
     * Restablece el color a blanco (sin tint).
     */
    public void resetColor() {
        this.currentColor = Color.WHITE;
    }

    /**
     * Dibuja una textura completa.
     *
     * @param texture textura a dibujar
     * @param x       posicion X
     * @param y       posicion Y
     */
    public void draw(Texture texture, float x, float y) {
        draw(texture, x, y, texture.getWidth(), texture.getHeight());
    }

    /**
     * Dibuja una textura con tamaño especifico.
     *
     * @param texture textura a dibujar
     * @param x       posicion X
     * @param y       posicion Y
     * @param width   ancho del sprite
     * @param height  alto del sprite
     */
    public void draw(Texture texture, float x, float y, float width, float height) {
        draw(texture, x, y, width, height, 0, 0, 1, 1);
    }

    /**
     * Dibuja una region de textura (para sprite sheets).
     *
     * @param texture textura a dibujar
     * @param x       posicion X
     * @param y       posicion Y
     * @param width   ancho del sprite
     * @param height  alto del sprite
     * @param u0      coordenada U inicial (0-1)
     * @param v0      coordenada V inicial (0-1)
     * @param u1      coordenada U final (0-1)
     * @param v1      coordenada V final (0-1)
     */
    public void draw(Texture texture, float x, float y, float width, float height, float u0, float v0, float u1, float v1) {

        if (!drawing) throw new IllegalStateException("SpriteBatch.begin() must be called before draw()");

        // Si cambia la textura o el batch esta lleno, flush
        if (currentTexture != null && currentTexture != texture || spriteCount >= MAX_BATCH_SIZE) flush();

        currentTexture = texture;

        // Agrega vertices del quad (2 triangulos)

        // Vertice 0 (bottom-left)
        addVertex(x, y + height, u0, v1);

        // Vertice 1 (bottom-right)
        addVertex(x + width, y + height, u1, v1);

        // Vertice 2 (top-right)
        addVertex(x + width, y, u1, v0);

        // Vertice 3 (top-left)
        addVertex(x, y, u0, v0);

        spriteCount++;
    }

    /**
     * Dibuja un rectangulo relleno (sin textura).
     */
    public void fillRect(float x, float y, float width, float height) {
        // Flush si hay una textura activa
        if (currentTexture != null) flush();

        // Usa una textura blanca de 1x1 (deberias crear una textura dummy)
        // Por ahora, simplemente no dibujamos sin textura
        // TODO: Implementar rectangulo sin textura usando un shader diferente o una textura blanca dummy
    }

    /**
     * Libera recursos de OpenGL.
     */
    public void cleanup() {
        glDeleteBuffers(vboID);
        glDeleteBuffers(eboID);
        // glDeleteVertexArrays(vaoID); // TODO: que importacion deberia usar?
    }

    /**
     * Renderiza el batch actual.
     */
    private void flush() {
        if (spriteCount == 0) return;

        // Sube los vertices a la GPU
        vertexBuffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuffer);

        // Vincula la textura
        if (currentTexture != null) currentTexture.bind();

        // Renderiza
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, spriteCount * INDICES_PER_SPRITE, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        // Reset batch
        vertexBuffer.clear();
        spriteCount = 0;
        currentTexture = null;
    }

    /**
     * Agrega un vertice al buffer.
     */
    private void addVertex(float x, float y, float u, float v) {
        vertexBuffer.put(x);
        vertexBuffer.put(y);
        vertexBuffer.put(u);
        vertexBuffer.put(v);
        vertexBuffer.put(currentColor.r());
        vertexBuffer.put(currentColor.g());
        vertexBuffer.put(currentColor.b());
        vertexBuffer.put(currentColor.a());
        vertexBuffer.put(0); // Texture ID (no usado por ahora)
    }

    /**
     * Genera indices para los quads. Cada quad usa 6 indices (2 triangulos).
     */
    private IntBuffer generateIndices() {
        IntBuffer indices = BufferUtils.createIntBuffer(MAX_BATCH_SIZE * INDICES_PER_SPRITE);

        for (int i = 0; i < MAX_BATCH_SIZE; i++) {
            int offset = i * VERTICES_PER_SPRITE;

            // Triangulo 1
            indices.put(offset + 0);
            indices.put(offset + 1);
            indices.put(offset + 2);

            // Triangulo 2
            indices.put(offset + 2);
            indices.put(offset + 3);
            indices.put(offset + 0);
        }

        indices.flip();
        return indices;
    }

}
