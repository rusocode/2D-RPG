package com.craivet;

import javax.swing.*;

/**
 * <h3>Notes</h3>
 * # This program depends on the CPU for rendering, so the graphical performance will be weaker than that of the games
 * that use GPU. To use the GPU, we need to take a step forward and access OpenGL.
 * <p>
 * # Rendering with Canvas seems to be more powerful unlike JPanel with respect to the amount of FPS.
 */

public class Launcher {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Game().start();
    }

}
