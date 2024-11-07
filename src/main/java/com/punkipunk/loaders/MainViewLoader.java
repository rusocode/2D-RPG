package com.punkipunk.loaders;

import com.punkipunk.controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Encapsula la carga de la vista principal.
 */

public class MainViewLoader {

    private final Parent parent;
    private final MainController controller;

    public MainViewLoader() {
        FXMLLoader loader = loadFXML();
        parent = loadParent(loader);
        controller = loader.getController();
    }

    /**
     * Carga el archivo FXML.
     *
     * @return el archivo FXML.
     */
    private FXMLLoader loadFXML() {
        return new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
    }


    /**
     * Carga el nodo padre.
     *
     * @param loader cargador FXML.
     * @return el nodo padre.
     */
    private Parent loadParent(FXMLLoader loader) {
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Parent getParent() {
        return parent;
    }

    public MainController getController() {
        return controller;
    }

}
