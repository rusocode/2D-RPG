package com.punkipunk;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class WindowManager {

    private final Stage stage;

    public WindowManager(Stage stage) {
        this.stage = stage;
    }

    public void configureWindow(String title) {
        stage.setTitle(title);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/textures/logo.png"))));
        stage.setResizable(false);
    }

    public void setOnCloseRequest() {
        stage.setOnCloseRequest(event -> {
            event.consume();
            showExitConfirmation();
        });
    }

    private void showExitConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) stage.close();
        });
    }

}
