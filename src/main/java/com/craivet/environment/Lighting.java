package com.craivet.environment;

import com.craivet.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Constants.*;

/**
 * Activa la iluminacion.
 */

public class Lighting {

    private final Game game;
    private BufferedImage darknessFilter;

    public Lighting(Game game) {
        this.game = game;
        illuminate();
    }

    /**
     * Ilumina.
     */
    public void illuminate() {
        // Crea una imagen en buffer
        darknessFilter = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        if (game.player.light == null) g2.setColor(new Color(0, 0, 0, 0.98f));
        else { // Si el player selecciono la linterna

            // Obtiene el centro del player
            int centerX = game.player.screenX + (tile_size / 2);
            int centerY = game.player.screenY + (tile_size / 2);

            // Crea un efecto de gradacion para el circulo de luz
            Color[] color = new Color[12];
            float[] fraction = new float[12];

            color[0] = new Color(0, 0, 0, 0.1f);
            color[1] = new Color(0, 0, 0, 0.42f);
            color[2] = new Color(0, 0, 0, 0.52f);
            color[3] = new Color(0, 0, 0, 0.61f);
            color[4] = new Color(0, 0, 0, 0.69f);
            color[5] = new Color(0, 0, 0, 0.76f);
            color[6] = new Color(0, 0, 0, 0.82f);
            color[7] = new Color(0, 0, 0, 0.87f);
            color[8] = new Color(0, 0, 0, 0.91f);
            color[9] = new Color(0, 0, 0, 0.94f);
            color[10] = new Color(0, 0, 0, 0.96f);
            color[11] = new Color(0, 0, 0, 0.98f);

            fraction[0] = 0f;
            fraction[1] = 0.4f;
            fraction[2] = 0.5f;
            fraction[3] = 0.6f;
            fraction[4] = 0.65f;
            fraction[5] = 0.7f;
            fraction[6] = 0.75f;
            fraction[7] = 0.8f;
            fraction[8] = 0.85f;
            fraction[9] = 0.9f;
            fraction[10] = 0.95f;
            fraction[11] = 1f;

            // Crea el efecto de gradacion usando la posicion del player, el radio, los datos de fraccion y color
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, (game.player.light.lightRadius / 2), fraction, color);

            // Establece los datos de gradacion en g2
            g2.setPaint(gPaint);
        }

        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g2.dispose();
    }

    /**
     * Actualiza la iluminacion solo cuando el player enciende o apaga (selecciona) la luz del item especificado,
     * evitando que el metodo illuminate() se llame 60 veces por segundo afectando el rendimiemto del juego.
     */
    public void update() {
        if (game.player.lightUpdate) {
            illuminate();
            game.player.lightUpdate = false;
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(darknessFilter, 0, 0, null);
    }

}
