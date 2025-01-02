package com.punkipunk.world.management;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.EntityFactory;
import com.punkipunk.entity.player.Player;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.projectile.Projectile;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.punkipunk.utils.Global.MAPS;

/**
 * Logic related to updating and rendering entities.
 */

public class EntityManager { // TODO Ya que implementa la interfaz Renderable no es mejor llamarla EntityRenderer?

    private final World world;
    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> itemsList = new ArrayList<>();
    private final List<Entity> projectilesList = new ArrayList<>();
    public EntityFactory factory;
    public Player player;
    public List<Entity> particles = new ArrayList<>();
    public Item[][] items = new Item[MAPS][20]; // TODO Shouldn't they be declared as HashMap or ArrayList?
    public Mob[][] mobs = new Mob[MAPS][40];
    public Interactive[][] interactives = new Interactive[MAPS][50];
    public Projectile[][] projectiles = new Projectile[MAPS][20];

    public EntityManager(Game game, World world) {
        this.world = world;
        player = new Player(game, world);
        factory = new EntityFactory(game, world, items, mobs, interactives);
    }

    public void update() {
        player.update();
        for (int i = 0; i < mobs[1].length; i++) {
            if (mobs[world.map.num][i] != null) {
                /* When the mob dies, first set the dead state to true preventing it from moving further. Then generate
                 * the death animation and when finished, set alive to false so that it does not generate movement and
                 * eliminates the object. */
                if (mobs[world.map.num][i].flags.alive && !mobs[world.map.num][i].flags.dead)
                    mobs[world.map.num][i].update();
                if (!mobs[world.map.num][i].flags.alive) {
                    mobs[world.map.num][i].checkDrop();
                    mobs[world.map.num][i] = null;
                }
            }
        }
        for (int i = 0; i < projectiles[1].length; i++) {
            if (projectiles[world.map.num][i] != null) {
                if (projectiles[world.map.num][i].flags.alive) projectiles[world.map.num][i].update();
                else projectiles[world.map.num][i] = null;
            }
        }
        for (int i = 0; i < particles.size(); i++) {
            if (particles.get(i) != null) {
                if (particles.get(i).flags.alive) particles.get(i).update();
                if (!particles.get(i).flags.alive) particles.remove(i);
            }
        }
        for (int i = 0; i < interactives[1].length; i++)
            if (interactives[world.map.num][i] != null) interactives[world.map.num][i].update();
    }

    public void render(GraphicsContext g2) {
        for (int i = 0; i < interactives[1].length; i++)
            if (interactives[world.map.num][i] != null) interactives[world.map.num][i].render(g2);

        entities.add(player);

        for (int i = 0; i < items[1].length; i++) {
            if (items[world.map.num][i] != null) {
                /* Adds the solid items (door, chest, etc.) to the list of entities to be able to sort them with respect
                 * to the position and the player. Items that are not solid are added to the item list. */
                if (!items[world.map.num][i].solid) itemsList.add(items[world.map.num][i]);
                else entities.add(items[world.map.num][i]);
            }
        }

        for (int i = 0; i < mobs[1].length; i++)
            if (mobs[world.map.num][i] != null) entities.add(mobs[world.map.num][i]);

        for (int i = 0; i < projectiles[1].length; i++)
            if (projectiles[world.map.num][i] != null) projectilesList.add(projectiles[world.map.num][i]);

        for (Entity particle : particles)
            if (particle != null) entities.add(particle);

        /* Sorts the list of entities depending on the position y. That is, if the player is above the mob, then it is
         * drawn below. But if the player is below the mob, it is drawn above it. The same applies for solid items. That
         * is, when the player is positioned above a solid item or a mob, it is drawn below, and when the player is
         * positioned below, it is drawn above. This is because they are all in the same list and are ordered in
         * ascending order by the position of the y coordinate of each entity. */
        entities.sort(Comparator.comparingInt(e -> (int) (e.pos.y + e.hitbox.getY())));

        // They are now drawn in ascending order
        for (Entity item : itemsList) item.render(g2);
        for (Entity entity : entities) entity.render(g2);
        for (Entity projectile : projectilesList) projectile.render(g2);

        itemsList.clear();
        projectilesList.clear();
        entities.clear();
    }

    public void removeTempEntities() {
        for (int map = 0; map < MAPS; map++)
            for (int i = 0; i < items[1].length; i++)
                if (items[map][i] != null && items[map][i].temp) items[map][i] = null;
    }

}
