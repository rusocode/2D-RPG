package com.craivet.world;

import com.craivet.Game;
import com.craivet.io.Progress;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Player;
import com.craivet.world.entity.item.*;
import com.craivet.world.entity.mob.*;
import com.craivet.world.entity.projectile.Projectile;
import com.craivet.world.management.CutsceneManager;
import com.craivet.world.management.EntityManager;
import com.craivet.world.management.EnvironmentManager;
import com.craivet.world.management.TileManager;
import com.craivet.world.tile.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * The World class represents the scenery of the game. Each part of the world is divided into 50x50 tile maps that are
 * composed of tiles, entities and an environment (weather).
 * <p>
 * TODO Los mobs, items y tiles interactivos se podrian agregar desde otra clase para dejarla mas limpia a la clase World
 */

public class World {

    private final Game game;

    // Managers
    private TileManager tiles;
    private EntityManager entities;
    public EnvironmentManager environment;
    public CutsceneManager cutscene;

    // Map
    public int map, zone, nextZone;
    public HashMap<Integer, String> maps = new HashMap<>();
    public Tile[] tileData;
    public int[][][] tileIndex = new int[MAPS][MAX_MAP_ROW][MAX_MAP_COL];

    // Entities
    public Player player;
    public List<Entity> particles = new ArrayList<>();
    // TODO Shouldn't they be declared as HashMap or ArrayList?
    public Item[][] items = new Item[MAPS][20];
    public Mob[][] mobs = new Mob[MAPS][40];
    public Interactive[][] interactives = new Interactive[MAPS][50];
    public Projectile[][] projectiles = new Projectile[MAPS][20];

    /* Variable para saber cuando el player esta dentro de la boss area para evitar que se produsca el mismo evento cada
     * ves que pasa por ese evento. Solo se vuelve a desactivar cuando muere en la boss area. */
    public boolean bossBattleOn;

    public World(Game game) {
        this.game = game;
        player = new Player(game, this);
        createEntities();
        initializeManagers();
    }

    /**
     * Updates the entities and environment.
     */
    public void update() {
        // Update entities only if the game is in PLAY_STATE
        if (game.state == PLAY_STATE) entities.update();
        if (game.state != MAIN_STATE) environment.update();
    }

    /**
     * Renders the tiles, entities and the environment.
     *
     * @param g2 graphic component.
     */
    public void render(Graphics2D g2) {
        // Render tiles, entities, and weather only if the game is other than MAIN_STATE
        if (game.state != MAIN_STATE) {
            tiles.render(g2);
            entities.render(g2);
            environment.render(g2);
            cutscene.render();
        }
        if (game.state == MAIN_STATE) {
            // TODO Replace with a background with an image
            g2.setColor(Color.black);
            g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        }

        // Dibuja el recorrido del pathfinding
        if (false) {
            g2.setColor(new Color(255, 0, 0, 70));
            for (int i = 0; i < game.aStar.pathList.size(); i++) {
                int worldX = game.aStar.pathList.get(i).col * tile;
                int worldY = game.aStar.pathList.get(i).row * tile;
                int screenX = worldX - player.pos.x + player.screen.xOffset;
                int screenY = worldY - player.pos.y + player.screen.yOffset;
                g2.fillRect(screenX, screenY, tile, tile);
            }
        }

    }

    private void initializeManagers() {
        tiles = new TileManager(game, this);
        entities = new EntityManager(this);
        environment = new EnvironmentManager(this);
        cutscene = new CutsceneManager(game, this);
    }

    public void changeArea() {
        // If there is a change of area
        if (nextZone != zone) {
            if (nextZone == BOSS) game.playMusic(music_boss);
            if (nextZone == DUNGEON) game.playMusic(ambient_dungeon);
            if (zone == DUNGEON && nextZone == OVERWORLD) game.playMusic(music_overworld);
        }
        zone = nextZone;
        createMobs();
    }

    /**
     * Create the entities.
     */
    public void createEntities() {
        createItems();
        createMobs();
        createInteractiveTile();
    }

    public void removeTempEntities() {
        for (int map = 0; map < MAPS; map++)
            for (int i = 0; i < items[1].length; i++)
                if (items[map][i] != null && items[map][i].temp) items[map][i] = null;
    }

    /**
     * Create the items.
     */
    public void createItems() {
        int i = 0; // TODO No se tiene que reiniciar la i despues de cada mapa?
        items[ABANDONED_ISLAND][i++] = new Axe(game, this, 33, 7);
        items[ABANDONED_ISLAND][i++] = new Door(game, this, 14, 28);
        items[ABANDONED_ISLAND][i++] = new Door(game, this, 12, 12);
        // TODO No es mejor pasarle directamente al cofre el item que va a tener desde el constructor?
        items[ABANDONED_ISLAND][i] = new Chest(game, this, 30, 29);
        // TODO What if there are many items?
        items[ABANDONED_ISLAND][i++].setLoot(new Key(game, this, 1));
        items[ABANDONED_ISLAND][i] = new Chest(game, this, 23, 40);
        items[ABANDONED_ISLAND][i++].setLoot(new PotionRed(game, this, 30));

        items[DUNGEON_BREG][i] = new Chest(game, this, 13, 16);
        items[DUNGEON_BREG][i++].setLoot(new PotionRed(game, this, 20));
        items[DUNGEON_BREG][i] = new Chest(game, this, 26, 34);
        items[DUNGEON_BREG][i++].setLoot(new PotionRed(game, this, 5));
        items[DUNGEON_BREG][i] = new Chest(game, this, 40, 41);
        items[DUNGEON_BREG][i++].setLoot(new Pickaxe(game, this));
        items[DUNGEON_BREG][i++] = new DoorIron(game, this, 18, 23);

        items[DUNGEON_BREG_SUB][i++] = new DoorIron(game, this, 25, 15);
        items[DUNGEON_BREG_SUB][i] = new Chest(game, this, 25, 8);
        items[DUNGEON_BREG_SUB][i].setLoot(new Chicken(game, this));

    }

    /**
     * Create the mobs.
     */
    public void createMobs() {
        int i = 0, j = 0, k = 0, z = 0;

        mobs[ABANDONED_ISLAND][i++] = new Oldman(game, this, 23, 16); // TODO set pos?
        mobs[ABANDONED_ISLAND][i++] = new Bat(game, this, 26, 19);
        mobs[ABANDONED_ISLAND][i++] = new Slime(game, this, 24, 37);
        mobs[ABANDONED_ISLAND][i++] = new Slime(game, this, 34, 42);
        mobs[ABANDONED_ISLAND][i++] = new Slime(game, this, 38, 42);
        mobs[ABANDONED_ISLAND][i++] = new Orc(game, this, 12, 33);

        mobs[ABANDONED_ISLAND_MARKET][j++] = new Trader(game, this, 12, 7);

        mobs[DUNGEON_BREG][k++] = new Box(game, this, 20, 25);
        mobs[DUNGEON_BREG][k++] = new Box(game, this, 11, 18);
        mobs[DUNGEON_BREG][k++] = new Box(game, this, 23, 14);
        mobs[DUNGEON_BREG][k++] = new Bat(game, this, 34, 39);
        mobs[DUNGEON_BREG][k++] = new Bat(game, this, 36, 25);
        mobs[DUNGEON_BREG][k++] = new Bat(game, this, 39, 26);
        mobs[DUNGEON_BREG][k++] = new Bat(game, this, 28, 11);
        mobs[DUNGEON_BREG][k++] = new Bat(game, this, 10, 19);

        if (!Progress.bossDefeated) mobs[DUNGEON_BREG_SUB][z++] = new Skeleton(game, this, 23, 16);

    }

    /**
     * Create the interactive tiles.
     */
    public void createInteractiveTile() {
        int i = 0;

        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 25, 27);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 26, 27);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 27, 27);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 27, 28);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 27, 29);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 27, 30);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 27, 31);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 28, 31);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 29, 31);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, this, 30, 31);

        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 18, 30);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 17, 31);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 17, 32);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 17, 34);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 18, 34);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 18, 33);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 10, 22);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 10, 24);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 38, 18);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 38, 19);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 38, 20);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 38, 21);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 18, 13);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 18, 14);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 22, 28);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 30, 28);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, this, 32, 28);

        interactives[DUNGEON_BREG][i++] = new MetalPlate(game, this, 20, 22);
        interactives[DUNGEON_BREG][i++] = new MetalPlate(game, this, 8, 17);
        interactives[DUNGEON_BREG][i++] = new MetalPlate(game, this, 39, 31);

    }

}
