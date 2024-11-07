package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import com.punkipunk.controllers.GameController;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;

/**
 * Maneja los eventos del teclado.
 */

public class KeyboardHandler {

    public final BitSet toggledKeys; // Para teclas que alternan su estado
    private final Game game;
    private final KeyboardStateHandler stateHandler;
    private final Set<Key> keys;
    private int lastKey = -1;

    public KeyboardHandler(Game game) {
        this.game = game;
        stateHandler = new KeyboardStateHandler();
        keys = EnumSet.noneOf(Key.class); // TODO Creo que se podria reemplazar por la clase de JavaFX que maneja todas las teclas KeyCode
        toggledKeys = new BitSet(256);
        setupKeyHandler();
    }

    public void handleKeyPressed(KeyEvent e) {
        Key key = Key.get(e.getCode().getCode());
        if (key != null && lastKey != key.ordinal() && !keys.contains(key)) {
            lastKey = key.ordinal();
            keys.add(key);
            stateHandler.handleKeyPress(key, game);
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        lastKey = -1;
        Key key = Key.get(e.getCode().getCode());
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

    /**
     * Resetea las teclas alternadas.
     */
    public void resetToggledKeys() {
        // Convierte las teclas alternadas en un flujo de datos (stream) para filtrar (filter) las activadas y alternarlas (forEach)
        Key.TOGGLE_KEYS.stream().filter(this::isKeyToggled).forEach(this::toggleKey);
    }

    /**
     * Configura el administrador de teclas utilizando la escena del juego actual.
     */
    private void setupKeyHandler() {
        game.getScene().setOnKeyPressed(this::handleKeyPressed);
        game.getScene().setOnKeyReleased(this::handleKeyReleased);
    }

}
