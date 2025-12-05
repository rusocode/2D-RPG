package com.punkipunk.gfx.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * Gestiona texturas OpenGL usando STB Image para carga de imagenes.
 */

public class Texture {

    private final int textureID;
    private final int width, height;

    /**
     * Carga una textura desde un recurso del classpath.
     */
    public Texture(String resourcePath) {
        // Carga la imagen usando STB
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        // Carga el archivo como ByteBuffer
        ByteBuffer imageBuffer;
        try {
            imageBuffer = loadResourceToByteBuffer(resourcePath);
        } catch (IOException e) {
            throw new RuntimeException("[Texture] Failed to load texture '" + resourcePath + "'");
        }

        // Voltea la imagen verticalmente (OpenGL espera el origen abajo-izquierda)
        STBImage.stbi_set_flip_vertically_on_load(true);

        // Decodifica la imagen
        ByteBuffer imageData = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 4); // RGBA

        if (imageData == null)
            throw new RuntimeException("[Texture] Failed to decode image '" + resourcePath + "'. Reason: " + STBImage.stbi_failure_reason());

        this.width = w.get(0);
        this.height = h.get(0);

        // Crea la textura OpenGL
        textureID = glGenTextures();
        // Vincula la textura
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Configura parametros de la textura
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // Filtrado NEAREST ideal para pixel art
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // Sube la textura a la GPU
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);

        // Libera la memoria de la imagen
        STBImage.stbi_image_free(imageData);

        // Desvincula la textura
        glBindTexture(GL_TEXTURE_2D, 0);

        // System.out.println("[Texture] Successfully loaded '" + resourcePath + "' (ID: " + textureID + ", " + width + "x" + height + ")");
    }

    /**
     * Constructor para texturas ya creadas (util para casos especiales).
     */
    public Texture(int textureID, int width, int height) {
        this.textureID = textureID;
        this.width = width;
        this.height = height;
    }

    /**
     * Desvincula cualquier textura.
     */
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Vincula esta textura para uso en renderizado.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    /**
     * Libera los recursos de la GPU.
     */
    public void cleanup() {
        glDeleteTextures(textureID);
    }

    public int getTextureID() {
        return textureID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Carga un recurso del classpath a un ByteBuffer.
     */
    private ByteBuffer loadResourceToByteBuffer(String resource) throws IOException {
        InputStream source = Texture.class.getResourceAsStream("/" + resource);
        if (source == null) throw new IOException("Resource not found: " + resource); // TODO Hace falta esto?

        ReadableByteChannel rbc = Channels.newChannel(source);
        ByteBuffer buffer = BufferUtils.createByteBuffer(8192);

        while (true) {
            int bytes = rbc.read(buffer);
            if (bytes == -1) break;
            if (buffer.remaining() == 0) {
                // Expande el buffer si es necesario
                ByteBuffer newBuffer = BufferUtils.createByteBuffer(buffer.capacity() * 2);
                buffer.flip();
                newBuffer.put(buffer);
                buffer = newBuffer;
            }
        }

        buffer.flip();
        return buffer;
    }

}
