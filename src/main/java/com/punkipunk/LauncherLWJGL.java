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
            e.printStackTrace();
            System.exit(1);
        }
    }
}