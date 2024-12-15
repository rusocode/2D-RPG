package com.punkipunk.input.keyboard;

import com.punkipunk.Game;
import javafx.scene.input.KeyEvent;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;

/**
 * <p>
 * Gestiona las entradas del teclado y el estado de las teclas en el juego. Procesa eventos de teclas presionadas y liberadas,
 * manteniendo registro del estado de las teclas y delegando al controlador de estados.
 */

public class Keyboard {

    /** Almacena el estado de las teclas que pueden alternarse (activado/desactivado) */
    public final BitSet toggledKeys = new BitSet(256);
    private final Game game;
    private final KeyStateController keyStateController = new KeyStateController();
    /** Conjunto de teclas actualmente presionadas */
    private final Set<Key> keys = EnumSet.noneOf(Key.class);
    /** Ultima tecla presionada, -1 si ninguna */
    private int lastKey = -1;

    /**
     * Crea un nuevo gestor de teclado.
     *
     * @param game el juego al que pertenece este teclado
     */
    public Keyboard(Game game) {
        this.game = game;
        setupKeyListeners();
    }

    /**
     * Procesa el evento de tecla presionada.
     * <p>
     * Evita la repeticion de teclas mantenidas y notifica al controlador de estados.
     *
     * @param e el evento de tecla presionada
     */
    public void onKeyPressed(KeyEvent e) {
        Key key = Key.get(e.getCode().getCode());
        if (key != null && lastKey != key.ordinal() && !keys.contains(key)) {
            lastKey = key.ordinal();
            keys.add(key);
            keyStateController.notifyKeyPress(key, game);
        }
    }

    /**
     * Procesa el evento de tecla liberada.
     * <p>
     * Actualiza el registro de teclas presionadas.
     *
     * @param e el evento de tecla liberada
     */
    public void onKeyReleased(KeyEvent e) {
        lastKey = -1;
        Key key = Key.get(e.getCode().getCode());
        if (key != null) keys.remove(key);
    }

    /**
     * Verifica si una tecla especifica esta presionada.
     *
     * @param key la tecla a verificar
     * @return true si la tecla esta presionada
     */
    public boolean isKeyPressed(Key key) {
        return keys.contains(key);
    }

    /**
     * Libera una tecla especifica del registro de teclas presionadas.
     *
     * @param key la tecla a liberar
     */
    public void releaseKey(Key key) {
        keys.remove(key);
    }

    /**
     * Alterna el estado de una tecla entre activado y desactivado.
     *
     * @param key la tecla a alternar
     */
    public void toggleKey(Key key) {
        toggledKeys.flip(key.ordinal());
    }

    /**
     * Verifica si una tecla alternada esta en estado activado.
     *
     * @param key la tecla a verificar
     * @return true si la tecla esta activada
     */
    public boolean isKeyToggled(Key key) {
        return toggledKeys.get(key.ordinal());
    }

    /**
     * Verifica si hay alguna tecla de movimiento o accion presionada.
     *
     * @return true si hay alguna tecla de movimiento o accion presionada
     */
    public boolean checkKeys() {
        return checkMovementKeys() || checkActionKeys();
    }

    /**
     * Verifica si hay alguna tecla de movimiento presionada.
     *
     * @return true si hay alguna tecla de movimiento presionada
     */
    public boolean checkMovementKeys() {
        return Key.MOVEMENT_KEYS.stream().anyMatch(this::isKeyPressed);
    }

    /**
     * Verifica si hay alguna tecla de accion presionada.
     *
     * @return true si hay alguna tecla de accion presionada
     */
    public boolean checkActionKeys() {
        return Key.ACTION_KEYS.stream().anyMatch(this::isKeyPressed);
    }

    /**
     * Libera todas las teclas de accion del registro de teclas presionadas.
     */
    public void resetActionKeys() {
        Key.ACTION_KEYS.forEach(keys::remove);
    }

    /**
     * Desactiva todas las teclas alternadas que esten activas.
     */
    public void resetToggledKeys() {
        // Convierte las teclas alternadas en un flujo de datos (stream) para filtrar (filter) las activadas y alternarlas (forEach)
        Key.TOGGLE_KEYS.stream().filter(this::isKeyToggled).forEach(this::toggleKey);
    }

    /**
     * Configura los listeners de eventos de teclado en la escena del juego.
     */
    private void setupKeyListeners() {
        game.getScene().setOnKeyPressed(this::onKeyPressed);
        game.getScene().setOnKeyReleased(this::onKeyReleased);
    }

}
