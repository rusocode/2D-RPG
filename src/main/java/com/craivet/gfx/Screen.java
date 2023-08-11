package com.craivet.gfx;

import com.craivet.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.craivet.utils.Global.*;

/**
 * <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/exclusivemode.html">Full-Screen Exclusive Mode</a>
 */

public class Screen extends JFrame {

    public Screen(final Game game, boolean fullScreenMode) {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (game.state == PLAY_STATE || game.state == OPTION_STATE || game.state == CHARACTER_STATE || game.state == PAUSE_STATE) {
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to save the changes?", "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (option) {
                        case JOptionPane.YES_OPTION -> {
                            game.file.saveData();
                            System.exit(0);
                        }
                        case JOptionPane.NO_OPTION -> {
                            game.file.saveConfig(); // TODO Hace falta?
                            System.exit(0);
                        }
                        case JOptionPane.CANCEL_OPTION -> setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
            }
        });
        if (fullScreenMode) setFullScreen();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Establece la resolucion a pantalla completa en caso de que el modo exclusivo de pantalla completa este disponible.
     */
    private void setFullScreen() {
        // Obtiene el dispositivo de pantalla local
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice(); // Obtiene la pantalla predeterminada (la unica pantalla en un sistema de un solo monitor

        if (gd.isFullScreenSupported()) {
            System.out.println("full-screen exclusive mode is available");
            gd.setFullScreenWindow(this);
        } else gd.setFullScreenWindow(null);
    }

}
