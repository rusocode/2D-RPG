package com.craivet.world.management;

import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.states.State;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EntityManager implements State {

    private final World world;

    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> items = new ArrayList<>();
    private final List<Entity> projectiles = new ArrayList<>();

    public EntityManager(World world) {
        this.world = world;
    }

    @Override
    public void update() {
        world.player.update();
        for (int i = 0; i < world.mobs[1].length; i++) {
            if (world.mobs[world.map][i] != null) {
                /* When the mob dies, first set the dead state to true preventing it from moving further. Then generate
                 * the death animation and when finished, set alive to false so that it does not generate movement and
                 * eliminates the object. */
                if (world.mobs[world.map][i].flags.alive && !world.mobs[world.map][i].flags.dead)
                    world.mobs[world.map][i].update();
                if (!world.mobs[world.map][i].flags.alive) {
                    world.mobs[world.map][i].checkDrop();
                    world.mobs[world.map][i] = null;
                }
            }
        }
        for (int i = 0; i < world.projectiles[1].length; i++) {
            if (world.projectiles[world.map][i] != null) {
                if (world.projectiles[world.map][i].flags.alive) world.projectiles[world.map][i].update();
                else world.projectiles[world.map][i] = null;
            }
        }
        for (int i = 0; i < world.particles.size(); i++) {
            if (world.particles.get(i) != null) {
                if (world.particles.get(i).flags.alive) world.particles.get(i).update();
                if (!world.particles.get(i).flags.alive) world.particles.remove(i);
            }
        }
        for (int i = 0; i < world.interactives[1].length; i++)
            if (world.interactives[world.map][i] != null) world.interactives[world.map][i].update();
    }

    @Override
    public void render(Graphics2D g2) {
        for (int i = 0; i < world.interactives[1].length; i++)
            if (world.interactives[world.map][i] != null) world.interactives[world.map][i].render(g2);

        entities.add(world.player);

        for (int i = 0; i < world.items[1].length; i++) {
            if (world.items[world.map][i] != null) {
                /* Adds the solid items (door, chest, etc.) to the list of entities to be able to sort them with respect
                 * to the position and the player. Items that are not solid are added to the item list. */
                if (!world.items[world.map][i].solid) items.add(world.items[world.map][i]);
                else entities.add(world.items[world.map][i]);
            }
        }

        for (int i = 0; i < world.mobs[1].length; i++)
            if (world.mobs[world.map][i] != null) entities.add(world.mobs[world.map][i]);

        for (int i = 0; i < world.projectiles[1].length; i++)
            if (world.projectiles[world.map][i] != null) projectiles.add(world.projectiles[world.map][i]);

        for (Entity particle : world.particles)
            if (particle != null) entities.add(particle);

        /* Sorts the list of entities depending on the position y. That is, if the player is above the mob, then it is
         * drawn below. But if the player is below the mob, it is drawn above it. The same applies for solid items. That
         * is, when the player is positioned above a solid item or a mob, it is drawn below, and when the player is
         * positioned below, it is drawn above. This is because they are all in the same list and are ordered in
         * ascending order by the position of the y coordinate of each entity. */
        entities.sort(Comparator.comparingInt(e -> e.pos.y + e.hitbox.y));

        // They are now drawn in ascending order
        for (Entity item : items) item.render(g2);
        for (Entity entity : entities) entity.render(g2);
        for (Entity projectile : projectiles) projectile.render(g2);

        items.clear();
        projectiles.clear();
        entities.clear();
    }

}
