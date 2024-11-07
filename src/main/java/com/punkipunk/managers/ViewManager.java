package com.punkipunk.managers;

import com.punkipunk.utils.ViewState;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.Map;

/**
 * Maneja la visibilidad de las diferentes vistas.
 */

public class ViewManager {

    private final Map<ViewState, Node> views = new EnumMap<>(ViewState.class);
    private ViewState currentState = ViewState.NONE;

    public ViewManager(VBox statsView, VBox optionsView, VBox controlsView) {
        views.put(ViewState.STATS, statsView);
        views.put(ViewState.OPTIONS, optionsView);
        views.put(ViewState.CONTROLS, controlsView);
    }

    public void toggleView(ViewState newState) {
        if (currentState == newState) {
            hideCurrentView();
            currentState = ViewState.NONE;
            return;
        }

        hideCurrentView();
        showView(newState);
        currentState = newState;
    }

    private void hideCurrentView() {
        if (currentState != ViewState.NONE) setViewVisibility(views.get(currentState), false);
    }

    private void showView(ViewState state) {
        setViewVisibility(views.get(state), true);
    }

    private void setViewVisibility(Node view, boolean visible) {
        view.setVisible(visible);
        view.setManaged(visible);
    }

}
