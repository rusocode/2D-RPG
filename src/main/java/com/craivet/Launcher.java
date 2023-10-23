package com.craivet;

import javax.swing.*;

import org.mapeditor.core.Map;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;
import org.mapeditor.view.OrthogonalRenderer;

import java.awt.*;
import java.io.FileInputStream;
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

        /*
        // Ruta al archivo del mapa TMX
        String mapPath = "map.tmx"; // TODO Seria bueno reemplazar la ruta absoluta por el cargador de clases String.valueOf(Launcher.class.getClassLoader().getResourceAsStream(mapPath)
        TMXMapReader tmxMapReader = new TMXMapReader();

        Graphics2D g2 = null;

        try {
            // Carga el archivo TMX y lo almacena en una instancia de tipo Map
            Map map = tmxMapReader.readMap(new FileInputStream(mapPath));
            // Crea un objeto TileLayer que recibe el mapa por parametro
            TileLayer layer = new TileLayer(map);
            // Crea un render de representacion ortogonal con el mapa cargado
            OrthogonalRenderer render = new OrthogonalRenderer(map);
            // Renderiza los tiles del mapa
            render.paintTileLayer(g2, layer);

            // Accede a la información del mapa
            System.out.println("Ancho del mapa: " + map.getWidth());
            System.out.println("Alto del mapa: " + map.getHeight());
            // System.out.println("Layer: " + map.getLayer(0).getProperties().getProperty("Name"));
            System.out.println("Orientacion: " + map.getOrientation());
            // ... y así sucesivamente

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } */

        new Game().start();
    }

}
