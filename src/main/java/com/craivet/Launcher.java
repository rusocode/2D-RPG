package com.craivet;

import com.craivet.gfx.Display;

import javax.swing.*;

/**
 * IMPORTANTE!
 * Este programa depende de la CPU para renderizar, por lo que el rendimiento grafico sera mas debil que el de los
 * juegos que utilizan GPU. Para utilizar la GPU, debemos dar un paso adelante y acceder a OpenGL. Por lo tanto, al usar
 * el modo fullScreen en una pc de bajos recursos, los FPS se verian afectados.
 */

public class Launcher {

	public static Display display;

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		display = new Display(new Game());
		display.start();

	}

}
