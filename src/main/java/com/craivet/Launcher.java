package com.craivet;

import javax.swing.*;
import javax.xml.bind.JAXBException;

import org.mapeditor.core.Map;
import org.mapeditor.io.TMXMapReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

        // Ruta al archivo del mapa TMX
        String mapPath = "tiled/map.tmx";
        TMXMapReader tmxMapReader = new TMXMapReader();

        // Carga el archivo TMX
        try {
            Map map = tmxMapReader.readMap(new FileInputStream(mapPath));

            // Accede a la información del mapa
            System.out.println("Ancho del mapa: " + map.getWidth());
            System.out.println("Alto del mapa: " + map.getHeight());
            // ... y así sucesivamente

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // new Game().start();
    }


}
