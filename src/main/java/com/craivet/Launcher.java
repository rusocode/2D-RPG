package com.craivet;

import javax.swing.*;
import java.awt.*;

/**
 * <h3>Notas</h3>
 * # Este programa depende de la CPU para renderizar, por lo que el rendimiento grafico sera mas debil que el de los
 * juegos que utilizan GPU. Para utilizar la GPU, debemos dar un paso adelante y acceder a OpenGL.
 * <p>
 * # El renderizado con Canvas parece ser mas potente a diferencia de JPanel con respecto a la cantidad de FPS.
 */

public class Launcher {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Game().start();

     /* DisplayMode displayMode;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice(); // Obtiene la pantalla principal
        displayMode = gd.getDisplayMode();
        System.out.println("display width = " + displayMode.getWidth());
        System.out.println("display height = " + displayMode.getHeight());
        System.out.println("display BitDepth = " + displayMode.getBitDepth());
        System.out.println("display hz = " + displayMode.getRefreshRate());
        */

        /*
        * if (gd.isFullScreenSupported()) {
      gd.setFullScreenWindow(frame);
  } else {
     // proceed in non-full-screen mode
     frame.setSize(...);
     frame.setLocation(...);
     frame.setVisible(true);
  }
        *
        * */

    }

}
