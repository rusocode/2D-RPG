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
        // setIgnoreRepaint(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (game.state == PLAY_STATE || game.state == OPTION_STATE || game.state == CHARACTER_STATE || game.state == INVENTORY_STATE || game.state == PAUSE_STATE) {
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
        // Obtiene la pantalla predeterminada (la unica pantalla en un sistema de un solo monitor) a traves del entorno de graficos local
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode display = gd.getDisplayMode();
        System.out.println("width = " + display.getWidth());
        System.out.println("height = " + display.getHeight());
        System.out.println("BitDepth = " + display.getBitDepth());
        System.out.println("hz = " + display.getRefreshRate());
        // Si la pantalla principal admite el modo exclusivo de pantalla completa
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else gd.setFullScreenWindow(null);
    }

}
