package com.punkipunk.gfx.opengl;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

/**
 * Gestiona programas shader de OpenGL (vertex + fragment).
 */

public class Shader {

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    /**
     * Crea y compila un shader desde codigo fuente.
     *
     * @param vertexSource   codigo fuente del vertex shader
     * @param fragmentSource codigo fuente del fragment shader
     */
    public Shader(String vertexSource, String fragmentSource) {
        // Compila shaders
        vertexShaderID = compileShader(GL_VERTEX_SHADER, vertexSource);
        fragmentShaderID = compileShader(GL_FRAGMENT_SHADER, fragmentSource);

        // Crea el programa y enlaza los shaders
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);

        // Verifica errores de enlace
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            String error = glGetProgramInfoLog(programID);
            throw new RuntimeException("Failed to link shader program: " + error);
        }

        // Valida el programa
        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE)
            System.err.println("Warning: Shader validation failed: " + glGetProgramInfoLog(programID));


        System.out.println("Shader program created successfully (ID: " + programID + ")");
    }

    /**
     * Desactiva cualquier shader.
     */
    public static void unbind() {
        glUseProgram(0);
    }

    /**
     * Activa este shader para uso.
     */
    public void bind() {
        glUseProgram(programID);
    }

    /**
     * Libera recursos.
     */
    public void cleanup() {
        unbind();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }

    /**
     * Obtiene la ubicacion de un uniform.
     */
    public int getUniformLocation(String name) {
        int location = glGetUniformLocation(programID, name);
        if (location == -1) System.err.println("Warning: Uniform '" + name + "' not found in shader");
        return location;
    }

    /**
     * Establece un uniform int.
     */
    public void setUniform(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    /**
     * Establece un uniform float.
     */
    public void setUniform(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }

    /**
     * Establece un uniform vec4 (color).
     */
    public void setUniform(String name, float v0, float v1, float v2, float v3) {
        glUniform4f(getUniformLocation(name), v0, v1, v2, v3);
    }

    /**
     * Establece un uniform mat4 (matriz 4x4).
     */
    public void setUniform(String name, Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(getUniformLocation(name), false, buffer);
    }

    public int getProgramID() {
        return programID;
    }

    /**
     * Compila un shader individual.
     */
    private int compileShader(int type, String source) {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);

        // Verifica errores de compilacion
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            String error = glGetShaderInfoLog(shaderID);
            String typeName = (type == GL_VERTEX_SHADER) ? "VERTEX" : "FRAGMENT";
            throw new RuntimeException("Failed to compile " + typeName + " shader:\n" + error);
        }

        return shaderID;
    }

}
