package com.punkipunk.world.environment;

import com.punkipunk.world.World;
import com.punkipunk.world.Zone;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

import static com.punkipunk.utils.Global.*;

/**
 * Activate the lighting.
 */

public class Lighting {

    // Day state
    public final int day = 0, dusk = 1, night = 2, dawn = 3;
    private final World world;
    private final WritableImage darknessFilter = new WritableImage(WINDOW_WIDTH, WINDOW_HEIGHT);
    private final Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();
    ;
    // Filter
    private final ImageView darknessFilterView = new ImageView();
    public int dayCounter;
    public float filterAlpha;
    public int dayState = day;

    public Lighting(World world) {
        this.world = world;
        // this.gc = canvas.getGraphicsContext2D();
        this.darknessFilterView.setImage(darknessFilter);
        illuminate(); // TODO Why is this method called from here?
    }

    /**
     * Illuminate.
     */
    public void illuminate() {
        if (world.entitySystem.player.light == null) {
            gc.setFill(Color.color(0, 0, 0, 0.90));
            gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        } else { // If the player selected the latern

            // Gets the center of the player
            int centerX = X_OFFSET + (tile / 2);
            int centerY = Y_OFFSET + (tile / 2);

            // Crear el efecto de gradación para el círculo de luz
            Stop[] stops = new Stop[]{
                    new Stop(0, Color.color(0, 0, 0, 0.1)),
                    new Stop(0.4, Color.color(0, 0, 0, 0.42)),
                    new Stop(0.5, Color.color(0, 0, 0, 0.52)),
                    new Stop(0.6, Color.color(0, 0, 0, 0.61)),
                    new Stop(0.65, Color.color(0, 0, 0, 0.69)),
                    new Stop(0.7, Color.color(0, 0, 0, 0.76)),
                    new Stop(0.75, Color.color(0, 0, 0, 0.82)),
                    new Stop(0.8, Color.color(0, 0, 0, 0.86)),
                    new Stop(0.85, Color.color(0, 0, 0, 0.87)),
                    new Stop(0.9, Color.color(0, 0, 0, 0.88)),
                    new Stop(0.95, Color.color(0, 0, 0, 0.89)),
                    new Stop(1.0, Color.color(0, 0, 0, 0.90))
            };

            RadialGradient gradient = new RadialGradient(
                    0, 0, centerX, centerY,
                    (double) world.entitySystem.player.light.lightRadius / 2,
                    false, CycleMethod.NO_CYCLE, stops
            );

            // Establece el gradiente y dibuja el rectángulo
            gc.setFill(gradient);
            gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        }

        // Captura el contenido del canvas en la WritableImage
        canvas.snapshot(null, darknessFilter);

    }

    public void update() {
        /* Updates the item's lighting only when the player selects the specified item, preventing the illuminate()
         * method from being called 60 times per second, affecting game performance. */
        if (world.entitySystem.player.lightUpdate) {
            illuminate();
            world.entitySystem.player.lightUpdate = false;
        }

        // cycleDay();

    }

    /**
     * If the zone is OUTSIDE, set the filterAlpha and draw the dark filter.
     * <p>
     * If the zone is DUNGEON, just draw the dark filter. It means that it will remain dark all the time.
     * <p>
     * If the zone is INDOOR, it does not configure or draw the dark filter. It means that it remains luminous all the time.
     */
    public void render(GraphicsContext context) {
        // if (world.map.zone == OVERWORLD) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        // if (world.map.zone == OVERWORLD || world.map.zone == DUNGEON || world.map.zone == BOSS) g2.drawImage(darknessFilter, 0, 0, null);
        // g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        // debug(g2);

        // Asumimos que context es el GraphicsContext del canvas principal
        if (world.map.id.zone.name().equals(Zone.OVERWORLD.name()) ||
                world.map.id.zone.name().equals(Zone.DUNGEON.name()) ||
                world.map.id.zone.name().equals(Zone.BOSS.name())) {
            // Actualiza la imagen del ImageView
            darknessFilterView.setImage(darknessFilter);

            // Configura la opacidad
            if (world.map.id.zone.name().equalsIgnoreCase(Zone.OVERWORLD.name())) darknessFilterView.setOpacity(filterAlpha);
            else darknessFilterView.setOpacity(1.0);

            // Configura el modo de mezcla
            darknessFilterView.setBlendMode(BlendMode.MULTIPLY);

            // Dibuja el filtro de oscuridad
            gc.save(); // Guarda el estado actual del GraphicsContext
            gc.setGlobalBlendMode(BlendMode.MULTIPLY);
            gc.drawImage(darknessFilter, 0, 0);
            gc.restore(); // Restaura el estado anterior del GraphicsContext
        }
    }

    /**
     * Cycles the day every certain time. The day cycle has 4 states: day, dusk, night and dawn.
     * <p>
     * To determine how long the day and night states last, it is necessary to multiply the number of seconds by the number of
     * times the Game Loop is updated. For example, if the Game Loop runs at 60 ticks and the day lasts 10 seconds, then this is
     * equivalent to adding 600 times the counter.
     * <p>
     * To determine the speed of transition between dusk and night, and between night and dawn, it is necessary to modify the
     * alpha value. For example, using the value 0.0001f, 10,000 fps are needed to complete the maximum alpha value (1f). So the
     * time it takes to complete is 166 seconds.
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

    private void debug(GraphicsContext g2) {
        String state = switch (dayState) {
            case day -> "Day";
            case dusk -> "Dusk";
            case night -> "Night";
            case dawn -> "Dawn";
            default -> "";
        };
        g2.setFill(Color.WHITE);
        g2.setFont(Font.font(g2.getFont().getFamily(), 24));
        g2.fillText(state, WINDOW_WIDTH - tile * 3, (int) (WINDOW_HEIGHT - tile * 1.5));
    }

}
