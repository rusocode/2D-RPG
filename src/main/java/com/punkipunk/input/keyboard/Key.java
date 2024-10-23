package com.punkipunk.input.keyboard;

import java.awt.event.KeyEvent;
import java.util.EnumSet;

/**
 * <p>
 * Representacion de las teclas.
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
    TEST(KeyEvent.VK_T),
    UP(KeyEvent.VK_W);


    public static final EnumSet<Key> ACTION_KEYS = EnumSet.of(ENTER, PICKUP, SHOOT);
    public static final EnumSet<Key> MOVEMENT_KEYS = EnumSet.of(DOWN, LEFT, RIGHT, UP);
    public static final EnumSet<Key> TOGGLE_KEYS = EnumSet.of(DEBUG, MINIMAP, RECTS, TEST);

    private final int keyCode;

    Key(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Obtiene la instancia de {@link Key} correspondiente al codigo de tecla proporcionado.
     *
     * @param keyCode codigo de tecla para devolver la instancia {@link Key} correspondiente.
     * @return la instancia de {@link Key} correspondiente al codigo de tecla proporcionado, o {@code null} si no existe dicha
     * tecla.
     */
    public static Key get(int keyCode) {
        for (Key key : values())
            if (key.keyCode == keyCode) return key;
        return null;
    }

}