package com.craivet;

import com.craivet.gfx.Display;

import javax.swing.*;

/**
 * <h3>Notas</h3>
 * # Este programa depende de la CPU para renderizar, por lo que el rendimiento grafico sera mas debil que el de los
 * juegos que utilizan GPU. Para utilizar la GPU, debemos dar un paso adelante y acceder a OpenGL.
 * <p>
 * # El renderizado con Canvas parece ser mas potente a diferencia de JPanel con respecto a la cantidad de fps.
 */

public class Launcher {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Display(new Game()).startGame();
    }

}
