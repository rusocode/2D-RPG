package com.punkipunk.loaders;

import com.punkipunk.controllers.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Encapsula la carga de la vista del juego.
 */

public class GameViewLoader {

    private final FXMLLoader loader;

    public GameViewLoader() {
        loader = loadFXML();
    }

    /**
     * Carga el archivo FXML.
     *
     * @return el archivo FXML.
     */
    private FXMLLoader loadFXML() {
        return new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
    }

    /**
     * Obtiene el nodo padre.
     *
     * @return el nodo padre.
     */
    public Parent getParent() {
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load game view", e);
        }
    }

    /**
     * Obtiene el controlador de la vista del juego.
     *
     * @return el controlador de la vista del juego.
     */
    public GameController getController() {
        return loader.getController();
    }

}
