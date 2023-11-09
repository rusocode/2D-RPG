package com.craivet.gfx;

import com.craivet.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.craivet.utils.Global.*;

/**
 * Screen resolution refers to both the number of pixels on the physical screen and the size of the window in which the
 * game is displayed.
 * <p>
 * Most used resolutions:
 * <ul>
 * <li>800x600 (544x416)
 * <li>1024x768 (768x584)
 * <li>1600x900
 * <li>1920x1080
 * </ul>
 * The AO presents various graphical resolutions that are based on the number of tools visible within the camera and the
 * graphical interfaces (console, inventory, etc.). At a resolution like 800x600, the portion of rendered tiles is
 * 544x416, called "Map View", while the remaining 256x184 correspond to the user interface (UI). Opting for a lower
 * resolution would affect the visual quality (in the case of Minecraft, the quality of the font and graphics remain
 * constant). It should be noted that the number of visible tiles does not vary, remaining constant regardless of the
 * resolution and being stretched in cases of higher resolutions.
 * <p>
 * Unlike World coordinates, these coordinates represent offset screen. The variables tempScreenX-tempScreenY are
 * used as temporary coordinates to represent the left and up attack frames of the entity, avoiding modifying the
 * original coordinates on the screen. Maybe I think they are not really necessary, it only differs in the rendering of
 * the rectangles but does not affect the collision.
 * <p>
 * <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/exclusivemode.html">Full-Screen Exclusive Mode</a>
 */

public class Screen extends JFrame {

    public int xOffset, yOffset;
    public int tempScreenX, tempScreenY;

    public Screen() {
        centerPlayer();
    }

    public Screen(Game game, boolean fullScreenMode) {
        // setIgnoreRepaint(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        // pack(); // I don't know why the graphics are deformed when the window is compressed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int op = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                switch (op) {
                    case JOptionPane.YES_OPTION -> System.exit(0);
                    case JOptionPane.NO_OPTION -> setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }

            }
        });
        if (fullScreenMode) setFullScreen();
        else setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Center the player on the screen by calculating the offsets.
     */
    private void centerPlayer() {
        xOffset = WINDOW_WIDTH / 2 - (tile / 2);
        yOffset = WINDOW_HEIGHT / 2 - (tile * 2 / 2);
    }

    /**
     * Sets the full screen resolution if full screen only mode is available.
     */
    private void setFullScreen() {
        // Gets the default display (the only display on a single monitor system) through the local graphics environment
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        // DisplayMode display = gd.getDisplayMode();
        // Whether the main screen supports full screen exclusive mode
        if (gd.isFullScreenSupported()) gd.setFullScreenWindow(this);
        else gd.setFullScreenWindow(null); // Set the resolution to the size of the Canvas
    }

}
