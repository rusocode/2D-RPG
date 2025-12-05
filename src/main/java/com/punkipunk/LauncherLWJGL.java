package com.punkipunk;

import com.punkipunk.core.MainGameLWJGL;

/**
 * Launcher principal del juego usando LWJGL. Reemplaza el launcher de JavaFX Application.
 */

public class LauncherLWJGL {

    public static void main(String[] args) {
        try {
            new MainGameLWJGL().start();
        } catch (Exception e) {
            System.err.println("Error starting the game: " + e.getMessage());
            System.exit(1);
        }
    }
}