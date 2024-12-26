package com.punkipunk.entity.components;

import com.punkipunk.core.Game;
import com.punkipunk.entity.base.Entity;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.punkipunk.utils.Global.*;

/**
 * Particles are small rectangles drawn with the fillRect() method. This is optional, but you can also add images for this case.
 */

public class Particle extends Entity {

    private final Color color;
    private final int xd;
    private int size;
    private int yd;

    public Particle(Game game, World world, Entity generator, Color color, int size, int speed, int maxHp, int xd, int yd) {
        super(game, world);
        //The generator would be, for example, a dry tree or a projectile.
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

        /* If the life of the particle is half of its maximum life, then the position y increases by 1, thus generating
         * a gravity effect. */
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
