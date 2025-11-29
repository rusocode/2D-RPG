package com.punkipunk.input.keyboard;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mapea los codigos de teclas de GLFW al enum Key.
 * <p>
 * Esta clase actua como puente entre los codigos de tecla de GLFW y el sistema de teclas del juego, permitiendo que el codigo
 * existente funcione sin cambios.
 */

public class KeyMapper {

    /**
     * Convierte un codigo de tecla GLFW a un objeto Key.
     *
     * @param glfwKeyCode el codigo de tecla de GLFW
     * @return la tecla correspondiente, o null si no hay mapeo
     */
    public static Key glfwToKey(int glfwKeyCode) {
        return switch (glfwKeyCode) {
            case GLFW_KEY_Q -> Key.DEBUG;
            case GLFW_KEY_S -> Key.DOWN;
            case GLFW_KEY_ENTER -> Key.ENTER;
            case GLFW_KEY_ESCAPE -> Key.ESCAPE;
            case GLFW_KEY_I -> Key.INVENTORY;
            case GLFW_KEY_A -> Key.LEFT;
            case GLFW_KEY_R -> Key.LOAD_MAP;
            case GLFW_KEY_M -> Key.MINIMAP;
            case GLFW_KEY_P -> Key.PICKUP;
            case GLFW_KEY_H -> Key.RECTS;
            case GLFW_KEY_D -> Key.RIGHT;
            case GLFW_KEY_F -> Key.SHOOT;
            case GLFW_KEY_C -> Key.STATS;
            case GLFW_KEY_T -> Key.TEST;
            case GLFW_KEY_W -> Key.UP;
            default -> null;
        };
    }

    /**
     * Convierte un objeto Key a un codigo de tecla GLFW.
     *
     * @param key la tecla a convertir
     * @return el codigo de tecla GLFW correspondiente, o -1 si no hay mapeo
     */
    public static int keyToGlfw(Key key) {
        return switch (key) {
            case DEBUG -> GLFW_KEY_Q;
            case DOWN -> GLFW_KEY_S;
            case ENTER -> GLFW_KEY_ENTER;
            case ESCAPE -> GLFW_KEY_ESCAPE;
            case INVENTORY -> GLFW_KEY_I;
            case LEFT -> GLFW_KEY_A;
            case LOAD_MAP -> GLFW_KEY_R;
            case MINIMAP -> GLFW_KEY_M;
            case PICKUP -> GLFW_KEY_P;
            case RECTS -> GLFW_KEY_H;
            case RIGHT -> GLFW_KEY_D;
            case SHOOT -> GLFW_KEY_F;
            case STATS -> GLFW_KEY_C;
            case TEST -> GLFW_KEY_T;
            case UP -> GLFW_KEY_W;
        };
    }

}
