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
                /* Cuando muere el mob, primero establece el estado dead a true evitando que siga moviendose. Luego
                 * genera la animacion de muerte y al finalizarla, establece alive en false para que no genere
                 * movimiento y elimine el objeto. */
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
                if (!world.projectiles[world.map][i].flags.alive) world.projectiles[world.map][i] = null;
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
                /* Agrega los items solidos (door, chest, etc.) a la lista de entidades para poder ordenarlos
                 * con respecto a la posicion y del player. Los items que no son solidos se agregan a la lista de
                 * items. */
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

        /* Ordena la lista de entidades dependiendo de la posicion y. Es decir, si el player esta por encima del mob,
         * entonces este se dibuja por debajo. Pero si el player esta por debajo del mob, este se dibuja por encima.
         * Lo mismo se aplica para los items solidos. Es decir que cuando el player se posiciona por encima de un
         * item solido o un mob, este se dibuja por debajo, y cuando el player se posiciona por debajo, este se
         * dibuja por arriba. Esto se debe porque estan todos en una misma lista y se ordenan de manera ascendente
         * por la posicion de la coordena y de cada entidad. */
        entities.sort(Comparator.comparingInt(e -> e.y + e.hitbox.y));

        // Ahora se dibujan por orden ascendente
        for (Entity item : items) item.render(g2);
        for (Entity entity : entities) entity.render(g2);
        for (Entity projectile : projectiles) projectile.render(g2);

        items.clear();
        projectiles.clear();
        entities.clear();
    }

}
