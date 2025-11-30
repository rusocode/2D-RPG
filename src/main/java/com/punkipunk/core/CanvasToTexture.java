package com.punkipunk.core;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * Clase que maneja la transferencia de pixeles desde un Canvas de JavaFX hacia una textura de OpenGL.
 * <p>
 * Este puente temporal permite mantener toda la logica de renderizado del juego (que usa GraphicsContext) mientras se visualiza
 * el resultado en una ventana LWJGL.
 * <p>
 * IMPORTANTE: La captura del Canvas (snapshot) debe ejecutarse en el JavaFX Thread, mientras que la actualizacion de la textura
 * OpenGL debe ejecutarse en el OpenGL Thread.
 */

public class CanvasToTexture {

    private final Canvas canvas;
    private final int textureID;
    private final int width, height;

    /** Buffers reutilizables para evitar crear objetos en cada frame. */
    private final WritableImage snapshot;
    private final ByteBuffer pixelBuffer;
    private final byte[] pixelArray;

    /**
     * Crea un nuevo CanvasToTexture.
     *
     * @param canvas Canvas de JavaFX del que se extraeran los pixeles
     */
    public CanvasToTexture(Canvas canvas) {
        this.canvas = canvas;
        this.width = (int) canvas.getWidth();
        this.height = (int) canvas.getHeight();

        // Crea la textura OpenGL
        this.textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Configura parametros de la textura
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // Filtrado nearest para pixel art
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // Inicializa la textura con datos vacios
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glBindTexture(GL_TEXTURE_2D, 0);

        // Prepara buffers reutilizables
        this.snapshot = new WritableImage(width, height);
        this.pixelBuffer = BufferUtils.createByteBuffer(width * height * 4); // RGBA = 4 bytes por pixel
        this.pixelArray = new byte[width * height * 4];

        System.out.println("CanvasToTexture created - Size: " + width + "x" + height + ", TextureID: " + textureID);
    }

    /**
     * Captura el Canvas como imagen que DEBE ejecutarse en el JavaFX Thread. Este metodo extrae los pixeles y los prepara para
     * ser transferidos a OpenGL.
     */
    public void captureCanvas() {
        // PASO 1: Captura el Canvas como imagen (DEBE estar en JavaFX Thread)
        canvas.snapshot(null, snapshot);

        // PASO 2: Extrae los pixeles de la imagen
        var pixelReader = snapshot.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), pixelArray, 0, width * 4);

        // PASO 3: Convierte BGRA a RGBA (OpenGL usa RGBA)
        convertBGRAtoRGBA(pixelArray);
    }

    /**
     * Actualiza la textura OpenGL con el contenido actual del Canvas. Este metodo debe llamarse despues de renderizar en el
     * Canvas y antes de dibujar en OpenGL.
     */
    public void updateTexture() {
        // PASO 1: Copia al ByteBuffer
        pixelBuffer.clear();
        pixelBuffer.put(pixelArray);
        pixelBuffer.flip();

        // PASO 2: Actualiza la textura OpenGL
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Renderiza la textura en un quad que cubre toda la pantalla.
     */
    public void render() {
        // Activa la textura
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Dibuja un quad en modo inmediato (simple para empezar)
        // TODO: migrar a VAO/VBO para mejor rendimiento
        glEnable(GL_TEXTURE_2D);

        glBegin(GL_QUADS);

        // Esquina inferior izquierda
        glTexCoord2f(0.0f, 1.0f); // UV invertido en Y (OpenGL vs JavaFX)
        glVertex2f(-1.0f, -1.0f);

        // Esquina inferior derecha
        glTexCoord2f(1.0f, 1.0f);
        glVertex2f(1.0f, -1.0f);

        // Esquina superior derecha
        glTexCoord2f(1.0f, 0.0f);
        glVertex2f(1.0f, 1.0f);

        // Esquina superior izquierda
        glTexCoord2f(0.0f, 0.0f);
        glVertex2f(-1.0f, 1.0f);

        glEnd();

        glDisable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Libera los recursos de OpenGL.
     */
    public void cleanup() {
        if (textureID != 0) glDeleteTextures(textureID);
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
     * Convierte los pixeles de formato BGRA (JavaFX) a RGBA (OpenGL). Se modifica el array in-place para eficiencia.
     */
    private void convertBGRAtoRGBA(byte[] pixels) {
        for (int i = 0; i < pixels.length; i += 4) {
            // Intercambia B y R
            byte temp = pixels[i];
            pixels[i] = pixels[i + 2];
            pixels[i + 2] = temp;
        }
    }

}
