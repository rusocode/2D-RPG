package com.craivet.gfx;

import com.craivet.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.craivet.utils.Global.*;

/**
 * La resolucion de pantalla se refiere tanto al numero de pixeles en la pantalla fisica como al tamaño de la ventana en
 * la que se muestra el juego.
 * <p>
 * Resoluciones mas usadas:
 * <ul>
 * <li>800x600 (544x416)
 * <li>1024x768 (768x584)
 * <li>1600x900
 * <li>1920x1080
 * </ul>
 * El AO presenta diversas resoluciones graficas que se basan en la cantidad de tiles visibles dentro de la camara y las
 * interfaces graficas (consola, inventario, etc.). En una resolucion como 800x600, la porcion de tiles renderizados es
 * 544x416, denominada "Vista del Mapa", mientras que los 256x184 restantes corresponden a la interfaz de usuario (UI).
 * Optar por una resolucion mas baja afectaria la calidad visual (para el caso de Minecraft, la calidad de la fuente y
 * los graficos se mantienen constantes). Cabe destacar que la cantidad de tiles visibles no varia, permaneciendo
 * constante independientemente de la resolucion y siendo estirados en casos de resoluciones mas altas.
 * <p>
 * A diferencia de las coordenadas del World, estas coordenadas representan la posicion en pantalla. Las variables
 * tempScreenX-tempScreenY se utilizan como coordenadas temporales para representar los frames de ataque left y up de la
 * entidad evitando modificar las coordenadas originales en pantalla. Igual creo que no son realmente necesarios, solo
 * difiere en el renderizado de los rectangulos pero no afecta la colision.
 * <p>
 * <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/exclusivemode.html">Full-Screen Exclusive Mode</a>
 */

public class Screen extends JFrame {

    public int x, y;
    public int tempScreenX, tempScreenY;

    public Screen() {
        centerPlayer();
    }

    public Screen(Game game, boolean fullScreenMode) {
        // setIgnoreRepaint(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        // pack(); // TODO Nose porque se deforman los graficos al comprimir la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (game.state != MAIN_STATE) {
                    int op = JOptionPane.showConfirmDialog(null, "Do you want to save the changes?", "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (op) {
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
        else setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Centra el player en la pantalla.
     */
    private void centerPlayer() {
        x = WINDOW_WIDTH / 2 - (tile / 2);
        y = WINDOW_HEIGHT / 2 - (tile * 2 / 2);
    }

    /**
     * Establece la resolucion a pantalla completa en caso de que el modo exclusivo de pantalla completa este disponible.
     */
    private void setFullScreen() {
        // Obtiene la pantalla predeterminada (la unica pantalla en un sistema de un solo monitor) a traves del entorno de graficos local
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        // DisplayMode display = gd.getDisplayMode();
        // Si la pantalla principal admite el modo exclusivo de pantalla completa
        if (gd.isFullScreenSupported()) gd.setFullScreenWindow(this);
        else gd.setFullScreenWindow(null); // Establece la resolucion al tamaño del Canvas
    }

}
