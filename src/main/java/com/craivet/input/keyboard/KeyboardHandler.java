package com.craivet.input.keyboard;

import com.craivet.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.BitSet;
import java.util.EnumSet;

/**
 * Maneja los eventos del teclado.
 */

public class KeyboardHandler extends KeyAdapter {

    private final Game game;
    private final KeyboardStateHandler stateHandler;
    private final EnumSet<Key> keys;
    public final BitSet toggledKeys;  // Para teclas que alternan su estado
    private int lastKey = -1;

    public KeyboardHandler(Game game) {
        this.game = game;
        stateHandler = new KeyboardStateHandler();
        keys = EnumSet.noneOf(Key.class);
        toggledKeys = new BitSet(256);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Key key = Key.get(e.getKeyCode());
        if (key != null && lastKey != key.ordinal() && !keys.contains(key)) {
            lastKey = key.ordinal();
            keys.add(key);
            stateHandler.handleKeyPress(key, game);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        lastKey = -1;
        Key key = Key.get(e.getKeyCode());
        if (key != null) keys.remove(key);
    }

    /**
     * Verifica si se presiono la tecla.
     *
     * @param key key.
     * @return true si se presiono la tecla.
     */
    public boolean isKeyPressed(Key key) {
        return keys.contains(key);
    }

    /**
     * Libera la tecla.
     *
     * @param key key.
     */
    public void releaseKey(Key key) {
        keys.remove(key); // Elimina una unica instancia del elemento especificado de esta coleccion, si esta presente (operacion opcional)
    }

    /**
     * Alterna el estado de la tecla.
     *
     * @param key key.
     */
    public void toggleKey(Key key) {
        toggledKeys.flip(key.ordinal());
    }

    /**
     * Verifica si la tecla alternada esta "activada".
     *
     * @param key key.
     * @return true si la tecla aternada esta activada o false.
     */
    public boolean isKeyToggled(Key key) {
        return toggledKeys.get(key.ordinal());
    }

    public boolean checkKeys() {
        return checkMovementKeys() || checkActionKeys();
    }

    public boolean checkMovementKeys() {
        return Key.MOVEMENT_KEYS.stream().anyMatch(this::isKeyPressed);
    }

    public boolean checkActionKeys() {
        return Key.ACTION_KEYS.stream().anyMatch(this::isKeyPressed);
    }

    public void resetActionKeys() {
        Key.ACTION_KEYS.forEach(keys::remove);
    }

}
