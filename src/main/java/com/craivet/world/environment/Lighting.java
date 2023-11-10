package com.craivet.world.environment;

import com.craivet.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

/**
 * Activate the lighting.
 */

public class Lighting {

    private final World world;

    private BufferedImage darknessFilter;

    public int dayCounter;
    public float filterAlpha;

    // Day state
    public final int day = 0, dusk = 1, night = 2, dawn = 3;
    public int dayState = day;

    public Lighting(World world) {
        this.world = world;
        illuminate(); // TODO Why is this method called from here?
    }

    /**
     * Illuminate.
     */
    public void illuminate() {
        // Create a buffered image
        darknessFilter = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        if (world.player.light == null) g2.setColor(new Color(0, 0, 0, 0.90f));
        else { // If the player selected the latern

            // Gets the center of the player
            int centerX = world.player.screen.xOffset + (tile / 2);
            int centerY = world.player.screen.yOffset + (tile / 2);

            // Create a gradation effect for the light circle
            Color[] color = new Color[12];
            float[] fraction = new float[12];

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

            color[0] = new Color(0, 0, 0, 0.1f);
            color[1] = new Color(0, 0, 0, 0.42f);
            color[2] = new Color(0, 0, 0, 0.52f);
            color[3] = new Color(0, 0, 0, 0.61f);
            color[4] = new Color(0, 0, 0, 0.69f);
            color[5] = new Color(0, 0, 0, 0.76f);
            color[6] = new Color(0, 0, 0, 0.82f);
            color[7] = new Color(0, 0, 0, 0.86f);
            color[8] = new Color(0, 0, 0, 0.87f);
            color[9] = new Color(0, 0, 0, 0.88f);
            color[10] = new Color(0, 0, 0, 0.89f);
            color[11] = new Color(0, 0, 0, 0.90f);

            // Create the gradation effect using the player position, radius, fraction and color data
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, ((float) world.player.light.lightRadius / 2), fraction, color);

            // Set the gradation data to g2
            g2.setPaint(gPaint);
        }

        g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        g2.dispose();
    }

    public void update() {
        /* Updates the item's lighting only when the player selects the specified item, preventing the illuminate()
         * method from being called 60 times per second, affecting game performance. */
        if (world.player.lightUpdate) {
            illuminate();
            world.player.lightUpdate = false;
        }

        // cycleDay();

    }

    /**
     * If the zone is OUTSIDE, set the filterAlpha and draw the dark filter.
     * <p>
     * If the zone is DUNGEON, just draw the dark filter. It means that it will remain dark all the time.
     * <p>
     * If the zone is INDOOR, it does not configure or draw the dark filter. It means that it remains luminous all the
     * time.
     */
    public void render(Graphics2D g2) {
        if (world.zone == OVERWORLD) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        if (world.zone == OVERWORLD || world.zone == DUNGEON || world.zone == BOSS) g2.drawImage(darknessFilter, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        debug(g2);
    }

    /**
     * Cycles the day every certain time. The day cycle has 4 states: day, dusk, night and dawn.
     * <p>
     * To determine how long the day and night states last, it is necessary to multiply the number of seconds by the
     * number of times the Game Loop is updated. For example, if the Game Loop runs at 60 ticks and the day lasts 10
     * seconds, then this is equivalent to adding 600 times the counter.
     * <p>
     * To determine the speed of transition between dusk and night, and between night and dawn, it is necessary to
     * modify the alpha value. For example, using the value 0.0001f, 10,000 fps are needed to complete the maximum alpha
     * value (1f). So the time it takes to complete is 166 seconds.
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
            // Prevent the alpha value from exceeding the maximum (1f) to avoid errors
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
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(24F));
        g2.drawString(state, WINDOW_WIDTH - tile * 3, (int) (WINDOW_HEIGHT - tile * 1.5));
    }

}
