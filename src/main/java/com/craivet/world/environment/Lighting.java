package com.craivet.world.environment;

import com.craivet.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.gfx.Assets.font_medieval1;
import static com.craivet.utils.Global.*;

/**
 * Activa la iluminacion.
 */

public class Lighting {

    private final World world;

    private BufferedImage darknessFilter;

    public int dayCounter;
    public float filterAlpha;

    // Day state
    public final int day = 0;
    public final int dusk = 1;
    public final int night = 2;
    public final int dawn = 3;
    public int dayState = day;

    public Lighting(World world) {
        this.world = world;
        illuminate(); // TODO Por que se llama desde aca este metodo?
    }

    /**
     * Ilumina.
     */
    public void illuminate() {
        // Crea una imagen en buffer
        darknessFilter = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        if (world.player.light == null) g2.setColor(new Color(0, 0, 0, 0.98f));
        else { // Si el player selecciono la linterna

            // Obtiene el centro del player
            int centerX = world.player.screenX + (tile_size / 2);
            int centerY = world.player.screenY + (tile_size / 2);

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
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, (world.player.light.lightRadius / 2), fraction, color);

            // Establece los datos de gradacion en g2
            g2.setPaint(gPaint);
        }

        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g2.dispose();
    }

    public void update() {
        /* Actualiza la iluminacion del item solo cuando el player selecciona el item especificado, evitando que el
         * metodo illuminate() se llame 60 veces por segundo afectando el rendimiemto del juego. */
        if (world.player.lightUpdate) {
            illuminate();
            world.player.lightUpdate = false;
        }

        // cycleDay();

    }

    /**
     * Si el area es OUTSIDE, configura el filterAlpha y dibuja el filtro oscuro.
     * <p>
     * Si el area es DUNGEON, solo dibuja el filtro oscuro. Significa que permanecera oscuro todo el tiempo.
     * <p>
     * Si el area es INDOOR, no configura ni dibuja el filtro oscuro. Significa que permanece luminoso tood el tiempo.
     */
    public void render(Graphics2D g2) {
        if (world.area == OUTSIDE) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        if (world.area == OUTSIDE || world.area == DUNGEON) g2.drawImage(darknessFilter, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        // debug(g2);
    }

    /**
     * Cicla el dia cada un cierto tiempo. El ciclo del dia cuenta con 4 estados: dia, oscuridad, noche y amanecer.
     * <p>
     * Para determinar el tiempo que duran los estados dia y noche, es necesario multiplicar la cantidad de segundos por
     * la cantidad de veces que se actualiza el Game Loop. Por ejemplo, si el Game Loop se ejecuta a 60 ticks y el dia
     * dura 10 segundos, entonces esto equivale a sumar 600 veces el contador.
     * <p>
     * Para determinar la velocidad de transicion entre la oscuridad y la noche, y entre la noche y el amanecer, es
     * necesario modificar el valor alpha. Por ejemplo, usando el valor 0,0001f se necesitan 10.000 fps para completar
     * el maximo valor alpha (1f). Por lo que el tiempo que tarda en completarse es de 166 segundos.
     */
    private void cycleDay() {
        if (dayState == day) {
            if (++dayCounter >= 600) {
                dayState = dusk;
                dayCounter = 0;
            }
        }
        if (dayState == dusk) {
            filterAlpha += 0.001f;
            // Evita que el valor alpha supere el maximo (1f) para evitar errores
            if (filterAlpha > 1f) {
                filterAlpha = 1f;
                dayState = night;
            }
        }
        if (dayState == night) {
            dayCounter++;
            if (dayCounter > 600) {
                dayState = dawn;
                dayCounter = 0;
            }
        }
        if (dayState == dawn) {
            filterAlpha -= 0.001f;
            if (filterAlpha < 0f) {
                filterAlpha = 0f;
                dayState = day;
            }
        }
    }

    public void resetDay() {
        dayState = day;
        filterAlpha = 0f;
    }

    private void debug(Graphics2D g2) {
        String state = switch (dayState) {
            case day -> "Day";
            case dusk -> "Dusk";
            case night -> "Night";
            case dawn -> "Dawn";
            default -> "";
        };
        g2.setFont(font_medieval1);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString(state, 750, 500);
    }

}
