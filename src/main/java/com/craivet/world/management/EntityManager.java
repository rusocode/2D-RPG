package com.craivet.world.management;

import com.craivet.*;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.states.State;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.craivet.util.Global.*;

public class EntityManager implements State {

    private final Game game;
    private final World world;

    private final List<Entity> entities = new ArrayList<>();

    public EntityManager(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    @Override
    public void update() {
        if (game.state == PLAY_STATE) {
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
    }

    @Override
    public void render(Graphics2D g2) {
        if (game.state == MAIN_STATE) {
            // Creo que evitaba un mal renderizado cuando estaba en pantalla completa
            g2.setColor(Color.black);
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            game.ui.render(g2);
        } else {
            for (int i = 0; i < world.interactives[1].length; i++)
                if (world.interactives[world.map][i] != null) world.interactives[world.map][i].render(g2);

            // Agrega las entidades a la lista de entidades
            entities.add(world.player);
            for (int i = 0; i < world.items[1].length; i++) {
                if (world.items[world.map][i] != null) {
                    if (!world.items[world.map][i].solid) world.itemsList.add(world.items[world.map][i]);
                        // Agrega la puerta a la lista de entidades para poder ordenarla con respecto a la posicion Y del player
                    else entities.add(world.items[world.map][i]);
                }
            }
            for (int i = 0; i < world.mobs[1].length; i++)
                if (world.mobs[world.map][i] != null) entities.add(world.mobs[world.map][i]);
            for (int i = 0; i < world.projectiles[1].length; i++)
                if (world.projectiles[world.map][i] != null) entities.add(world.projectiles[world.map][i]);
            for (Entity particle : world.particles)
                if (particle != null) entities.add(particle);

            /* Ordena la lista de entidades dependiendo de la posicion Y. Es decir, si el player esta por encima del npc
             * entonces este se dibuja por debajo. */
            entities.sort(Comparator.comparingInt(o -> o.y));

            for (Entity item : world.itemsList) item.render(g2);
            for (Entity entity : entities) entity.render(g2);

            entities.clear();
            world.itemsList.clear();

        }
    }

}