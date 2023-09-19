package com.craivet.world;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Player;
import com.craivet.world.entity.item.*;
import com.craivet.world.entity.mob.*;
import com.craivet.world.entity.projectile.Projectile;
import com.craivet.world.management.EntityManager;
import com.craivet.world.management.EnvironmentManager;
import com.craivet.world.management.TileManager;
import com.craivet.world.tile.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * La clase World represeta el escenario del juego. Cada parte del mundo se divide en mapas de 50x50 tiles que estan
 * compuestos por tiles, entidades y un entorno (clima).
 */

public class World {

    private final Game game;

    private TileManager tiles;
    private EntityManager entities;
    public EnvironmentManager environment;

    // Map
    public int map, zone, nextZone;
    public HashMap<Integer, String> maps = new HashMap<>();
    public Tile[] tileData;
    public int[][][] tileIndex = new int[MAPS][MAX_MAP_ROW][MAX_MAP_COL];

    // Entities
    public Player player;
    public List<Entity> particles = new ArrayList<>();
    // TODO No tendrian que declararse como HashMap o ArrayList?
    public Item[][] items = new Item[MAPS][20];
    public Mob[][] mobs = new Mob[MAPS][40];
    public Interactive[][] interactives = new Interactive[MAPS][50];
    public Projectile[][] projectiles = new Projectile[MAPS][20];

    public World(Game game) {
        this.game = game;
        player = new Player(game, this);
        createEntities();
        initializeManagers();
    }

    /**
     * Actualiza las entidades y el entorno.
     */
    public void update() {
        // Actualiza las entidades solo si el juego esta en PLAY_STATE
        if (game.state == PLAY_STATE) entities.update();
        if (game.state != MAIN_STATE) environment.update();
    }

    /**
     * Renderiza los tiles, las entidades y el entorno.
     *
     * @param g2 componente grafico.
     */
    public void render(Graphics2D g2) {
        // Renderiza los tiles, las entidades y el clima solo si el juego es distinto a MAIN_STATE
        if (game.state != MAIN_STATE) {
            tiles.render(g2);
            entities.render(g2);
            environment.render(g2);
        }
        if (game.state == MAIN_STATE) {
            // TODO Reemplazar por un fondo
            g2.setColor(Color.black);
            g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        }

        // Dibuja el recorrido del pathfinding
        if (false) {
            g2.setColor(new Color(255, 0, 0, 70));
            for (int i = 0; i < game.aStar.pathList.size(); i++) {
                int worldX = game.aStar.pathList.get(i).col * tile;
                int worldY = game.aStar.pathList.get(i).row * tile;
                int screenX = worldX - player.pos.x + player.screen.x;
                int screenY = worldY - player.pos.y + player.screen.y;
                g2.fillRect(screenX, screenY, tile, tile);
            }
        }

    }

    private void initializeManagers() {
        tiles = new TileManager(this);
        entities = new EntityManager(this);
        environment = new EnvironmentManager(this);
    }

    public void changeArea() {
        // Si hay un cambio de area
        if (nextZone != zone) {
            if (nextZone == DUNGEON) game.playMusic(music_dungeon);
            if (zone == DUNGEON && nextZone == OUTSIDE) game.stopMusic();
        }
        zone = nextZone;
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
        items[NASHE][i++] = new Axe(game, this, 33, 7);
        items[NASHE][i++] = new PotionRed(game, this, 5, 23, 23);
        items[NASHE][i++] = new Key(game, this, 1, 21, 21);
        items[NASHE][i++] = new Door(game, this, 14, 28);
        items[NASHE][i++] = new Door(game, this, 12, 12);

        items[NASHE][i] = new Chest(game, this, 30, 29);
        items[NASHE][i++].setLoot(new Key(game, this, 1));

        /* items[NIX][i] = new Chest(game, this, 17, 21);
        items[NIX][i++].setLoot(new Tent(game, this)); */

        items[NASHE][i] = new Chest(game, this, 16, 21);
        // FIXME Se podria reemplazar el mal uso del Setter creando un nuevo item como argumento desde el constructor del objeto Chest
        // TODO Y si son muchos items?
        items[NASHE][i++].setLoot(new PotionRed(game, this, 5));

        items[NASHE][i] = new Chest(game, this, 23, 40);
        items[NASHE][i++].setLoot(new PotionRed(game, this, 30));

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
        int i = 0, j = 0, k = 0, z = 0;

        mobs[NASHE][i++] = new Oldman(game, this, 23, 19);
        mobs[NASHE][i++] = new Rock(game, this, 26, 21);
        mobs[NASHE][i++] = new Slime(game, this, 24, 37);
        mobs[NASHE][i++] = new Slime(game, this, 34, 42);
        mobs[NASHE][i++] = new Slime(game, this, 38, 42);
        mobs[NASHE][i++] = new Orc(game, this, 12, 33);

        mobs[NASHE_INDOOR_01][j++] = new Trader(game, this, 12, 7);

        mobs[DUNGEON_01][k++] = new Rock(game, this, 20, 25);
        mobs[DUNGEON_01][k++] = new Rock(game, this, 11, 18);
        mobs[DUNGEON_01][k++] = new Rock(game, this, 23, 14);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 34, 39);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 36, 25);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 39, 26);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 28, 11);
        mobs[DUNGEON_01][k++] = new Bat(game, this, 10, 19);

        mobs[DUNGEON_02][z++] = new Skeleton(game, this, 23, 16);

    }

    /**
     * Crea los tiles interactivos.
     */
    public void createInteractiveTile() {
        int i = 0;

        interactives[NASHE][i++] = new DryTree(game, this, 25, 27);
        interactives[NASHE][i++] = new DryTree(game, this, 26, 27);
        interactives[NASHE][i++] = new DryTree(game, this, 27, 27);
        interactives[NASHE][i++] = new DryTree(game, this, 27, 28);
        interactives[NASHE][i++] = new DryTree(game, this, 27, 29);
        interactives[NASHE][i++] = new DryTree(game, this, 27, 30);
        interactives[NASHE][i++] = new DryTree(game, this, 27, 31);
        interactives[NASHE][i++] = new DryTree(game, this, 28, 31);
        interactives[NASHE][i++] = new DryTree(game, this, 29, 31);
        interactives[NASHE][i++] = new DryTree(game, this, 30, 31);

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
