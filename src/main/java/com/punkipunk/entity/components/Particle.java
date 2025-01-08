package com.punkipunk.entity.components;

import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.punkipunk.utils.Global.*;

/**
 * <p>
 * Las particulas son pequeños rectangulos dibujados con el metodo fillRect(). Esto es opcional, pero tambien puedes agregar
 * imagenes en este caso.
 * <p>
 * TODO Usar libreria externa para el efecto de particulas
 */

public class Particle extends Entity {

    private final Color color;
    private final int xd;
    private int size;
    private int yd;

    public Particle(Game game, World world, Entity generator, Color color, int size, int speed, int maxHp, int xd, int yd) {
        super(game, world);
        // El generador seria, por ejemplo, un DryTree
        this.color = color;
        this.size = size;
        stats.speed = speed;
        stats.maxHp = maxHp;
        this.xd = xd;
        this.yd = yd;

        stats.hp = maxHp;
        int offset = (tile / 2) - (size - 2);
        pos.x = generator.pos.x + offset;
        pos.y = generator.pos.y + offset;

    }

    public void update() {
        stats.hp--;

        /* Si la vida de la particula es la mitad de su vida maxima, entonces la posicion y aumenta en 1, generando asi un efecto de gravedad */
        if (stats.hp < stats.maxHp / 3) {
            yd++;
            size--;
        }

        pos.x += xd * stats.speed;
        pos.y += yd * stats.speed;

        if (stats.hp == 0) flags.alive = false;

    }

    public void render(GraphicsContext g2) {
        int screenX = pos.x - world.entities.player.pos.x + X_OFFSET;
        int screenY = pos.y - world.entities.player.pos.y + Y_OFFSET;
        g2.setFill(color);
        // g2.setColor(color);
        g2.fillRect(screenX, screenY, size, size); // Draw a rectangle as a particle
    }

}
