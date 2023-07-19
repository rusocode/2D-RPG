package com.craivet.world;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.item.*;
import com.craivet.world.entity.mob.*;
import com.craivet.world.entity.projectile.Projectile;
import com.craivet.util.Utils;
import com.craivet.world.management.EntityManager;
import com.craivet.world.management.EnvironmentManager;
import com.craivet.world.management.TileManager;
import com.craivet.world.tile.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.craivet.gfx.Assets.*;
import static com.craivet.util.Global.*;

/**
 * La clase World represeta el escenario del Player. Este a su vez esta compuesto por tiles, entidades y un entorno.
 * <p>
 * El tamaño de cada mapa es de 50x50 tiles.
 */

public class World {

    private final Game game;

    private final TileManager tiles;
    private final EntityManager entitites;
    public final EnvironmentManager environment;

    // Map
    public int map;
    public int area, nextArea;
    public HashMap<Integer, String> maps = new HashMap<>();
    public Tile[] tileData;
    public int[][][] tileIndex = new int[MAX_MAP][MAX_MAP_ROW][MAX_MAP_COL];

    // Entities
    public Player player;
    public List<Entity> itemsList = new ArrayList<>();
    public List<Entity> particles = new ArrayList<>();
    // TODO No tendrian que declararse como HashMap?
    public Item[][] items = new Item[MAX_MAP][20];
    public Mob[][] mobs = new Mob[MAX_MAP][40];
    public Interactive[][] interactives = new Interactive[MAX_MAP][50];
    public Projectile[][] projectiles = new Projectile[MAX_MAP][20];

    public boolean drawPath;

    public World(Game game) {
        this.game = game;
        player = new Player(game, this); // TODO Deberia ir dentro de createEntities()
        // createEntities();
        tiles = new TileManager(game, this);
        entitites = new EntityManager(game, this);
        environment = new EnvironmentManager(this);
    }

    /**
     * Actualiza las entidades y el entorno.
     */
    public void update() {
        entitites.update();
        if (game.state != MAIN_STATE) environment.update();
    }

    /**
     * Renderiza los tiles, las entidades y el entorno.
     *
     * @param g2 componente grafico.
     */
    public void render(Graphics2D g2) {
        tiles.render(g2);
        entitites.render(g2);
        if (game.state != MAIN_STATE) environment.render(g2);
    }

    public void changeArea() {
        // Si hay un cambio de area
        if (nextArea != area) {
            if (nextArea == DUNGEON) game.playMusic(music_dungeon);
            if (area == DUNGEON && nextArea == OUTSIDE) game.stopMusic();
        }
        area = nextArea;
        createMOBs();
    }

    /**
     * Crea las entidades.
     */
    public void createEntities() {
        createItems();
        createMOBs();
        createInteractiveTile();
    }

    /**
     * Crea los items.
     */
    public void createItems() {
        int i = 0;
        items[NIX][i++] = new Axe(game, this, 33, 7);
        items[NIX][i++] = new PotionRed(game, this, 5, 20, 21);
        items[NIX][i++] = new Key(game, this, 1, 21, 21);
        items[NIX][i++] = new Door(game, this, 14, 28);
        items[NIX][i++] = new Door(game, this, 12, 12);

        items[NIX][i] = new Chest(game, this, 30, 29);
        items[NIX][i++].setLoot(new Key(game, this, 1));

        /* items[NIX][i] = new Chest(game, this, 17, 21);
        items[NIX][i++].setLoot(new Tent(game, this)); */

        items[NIX][i] = new Chest(game, this, 16, 21);
        // FIXME Se podria reemplazar el mal uso del Setter creando un nuevo item como argumento desde el constructor del objeto Chest
        // TODO Y si son muchos items?
        items[NIX][i++].setLoot(new PotionRed(game, this, 5));

        items[NIX][i] = new Chest(game, this, 23, 40);
        items[NIX][i++].setLoot(new PotionRed(game, this, 30));

        items[DUNGEON_01][i] = new Chest(game, this, 13, 16);
        items[DUNGEON_01][i++].setLoot(new PotionRed(game, this, 20));

        items[DUNGEON_01][i] = new Chest(game, this, 26, 34);
        items[DUNGEON_01][i++].setLoot(new PotionRed(game, this, 5));

        items[DUNGEON_01][i] = new Chest(game, this, 40, 41);
        items[DUNGEON_01][i++].setLoot(new Pickaxe(game, this));

        items[DUNGEON_01][i] = new DoorIron(game, this, 18, 23);

    }

    /**
     * Crea los mobs.
     */
    public void createMOBs() {
        int i = 0, j = 0, k = 0;

        mobs[NIX][i++] = new Oldman(game, this, 23, 19);
        mobs[NIX][i++] = new BigRock(game, this, 26, 21);
        mobs[NIX][i++] = new Slime(game, this, 23, 41);
        mobs[NIX][i++] = new Slime(game, this, 24, 37);
        mobs[NIX][i++] = new Slime(game, this, 34, 42);
        mobs[NIX][i++] = new Slime(game, this, 38, 42);
        mobs[NIX][i++] = new Orc(game, this, 12, 33);

        mobs[NIX_INDOOR_01][j++] = new Trader(game, this, 12, 7);

        mobs[DUNGEON_01][k++] = new BigRock(game, this, 20, 25);
        mobs[DUNGEON_01][k++] = new BigRock(game, this, 11, 18);
        mobs[DUNGEON_01][k++] = new BigRock(game, this, 23, 14);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 34, 39);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 36, 25);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 39, 26);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 28, 11);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 10, 19);

    }

    /**
     * Crea los tiles interactivos.
     */
    public void createInteractiveTile() {
        int i = 0;
        interactives[NIX][i++] = new DryTree(game, this, 21, 20);
        interactives[NIX][i++] = new DryTree(game, this, 23, 22);

        interactives[NIX][i++] = new DryTree(game, this, 25, 27);
        interactives[NIX][i++] = new DryTree(game, this, 26, 27);
        interactives[NIX][i++] = new DryTree(game, this, 27, 27);
        interactives[NIX][i++] = new DryTree(game, this, 27, 28);
        interactives[NIX][i++] = new DryTree(game, this, 27, 29);
        interactives[NIX][i++] = new DryTree(game, this, 27, 30);
        interactives[NIX][i++] = new DryTree(game, this, 27, 31);
        interactives[NIX][i++] = new DryTree(game, this, 28, 31);
        interactives[NIX][i++] = new DryTree(game, this, 29, 31);
        interactives[NIX][i++] = new DryTree(game, this, 30, 31);

        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 18, 30);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 17, 31);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 17, 32);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 17, 34);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 18, 34);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 18, 33);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 10, 22);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 10, 24);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 38, 18);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 38, 19);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 38, 20);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 38, 21);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 18, 13);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 18, 14);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 22, 28);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 30, 28);
        interactives[DUNGEON_01][i++] = new DestructibleWall(game, this, 32, 28);

        interactives[DUNGEON_01][i++] = new MetalPlate(game, this, 20, 22);
        interactives[DUNGEON_01][i++] = new MetalPlate(game, this, 8, 17);
        interactives[DUNGEON_01][i++] = new MetalPlate(game, this, 39, 31);

    }


}
