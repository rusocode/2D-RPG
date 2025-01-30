package com.punkipunk.input.keyboard;

import java.awt.event.KeyEvent;
import java.util.EnumSet;

/**
 * <p>
 * Representa las teclas utilizadas en el juego.
 * <p>
 * Cada tecla esta asociada a un codigo de tecla de KeyEvent y puede pertenecer a diferentes categorias como accion, movimiento o
 * alternables.
 * <p>
 * Al usar una enumeracion, el codigo se vuelve mas tipo seguro, ya que solo se pueden usar los valores de la enumeracion
 * {@code Key} para interactuar con el {@code EnumSet}. Los entornos de desarrollo pueden brindar autocompletado y sugerencias de
 * manera mas efectiva cuando se trabaja con enumeraciones.
 * <p>
 * Si en el futuro se necesita agregar o modificar las teclas, sera mas sencillo hacerlo al tener una enumeracion centralizada que
 * representa las teclas.
 */

public enum Key {

    DEBUG(KeyEvent.VK_Q),
    DOWN(KeyEvent.VK_S),
    ENTER(KeyEvent.VK_ENTER),
    ESCAPE(KeyEvent.VK_ESCAPE),
    INVENTORY(KeyEvent.VK_I),
    LEFT(KeyEvent.VK_A),
    LOAD_MAP(KeyEvent.VK_R),
    MINIMAP(KeyEvent.VK_M),
    PICKUP(KeyEvent.VK_P),
    RECTS(KeyEvent.VK_H),
    RIGHT(KeyEvent.VK_D),
    SHOOT(KeyEvent.VK_F),
    STATS(KeyEvent.VK_C),
    TEST(KeyEvent.VK_T), // TODO Que se vean los rects, el pathfinding, y las otras cosas tipicas de test mode
    UP(KeyEvent.VK_W);

    public static final EnumSet<Key> ACTION_KEYS = EnumSet.of(ENTER, PICKUP, SHOOT);
    public static final EnumSet<Key> MOVEMENT_KEYS = EnumSet.of(DOWN, LEFT, RIGHT, UP);
    public static final EnumSet<Key> TOGGLE_KEYS = EnumSet.of(DEBUG, MINIMAP, RECTS, TEST);

    private final int keyCode;

    /**
     * Crea una nueva tecla con su codigo correspondiente.
     *
     * @param keyCode el codigo de tecla de KeyEvent
     */
    Key(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Obtiene la tecla correspondiente a un codigo especifico.
     *
     * @param keyCode el codigo de tecla a buscar
     * @return la tecla correspondiente al codigo, o null si no existe
     */
    public static Key get(int keyCode) {
        for (Key key : values())
            if (key.keyCode == keyCode) return key;
        return null;
    }

}