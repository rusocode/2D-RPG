package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.managers.EntityManager;
import com.craivet.entity.Player;
import com.craivet.entity.item.*;
import com.craivet.entity.mob.Bat;
import com.craivet.entity.mob.Mob;
import com.craivet.entity.mob.Orc;
import com.craivet.entity.mob.Slime;
import com.craivet.entity.npc.BigRock;
import com.craivet.entity.npc.Npc;
import com.craivet.entity.npc.Oldman;
import com.craivet.entity.npc.Trader;
import com.craivet.entity.projectile.Projectile;
import com.craivet.managers.EnvironmentManager;
import com.craivet.managers.TileManager;
import com.craivet.tile.*;
import com.craivet.utils.Utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.craivet.gfx.Assets.music_dungeon;
import static com.craivet.utils.Global.*;

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
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> solids = new ArrayList<>();

    // Entities
    public Player player;
    public List<Entity> itemsList = new ArrayList<>();
    public List<Entity> particles = new ArrayList<>();
    // TODO No tendrian que declararse como HashMap?
    public Entity[][] items = new Item[MAX_MAP][20];
    public Entity[][] npcs = new Npc[MAX_MAP][10];
    public Entity[][] mobs = new Mob[MAX_MAP][20];
    public Interactive[][] interactives = new Interactive[MAX_MAP][50];
    public Entity[][] projectiles = new Projectile[MAX_MAP][20];

    public boolean drawPath;

    public World(Game game) {
        this.game = game;
        player = new Player(game, this);
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

    /**
     * Lee los datos de cada tile (nombre y estado solido) desde el archivo "tile_data.txt" y los agrega a sus
     * respectivas listas. Luego utiliza esos datos para cargar todos los tiles dentro de un array.
     */
    public void loadTiles() {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("maps/tile_data.txt"))))) {
            while ((line = br.readLine()) != null) {
                names.add(line);
                solids.add(br.readLine());
            }
            tileData = new Tile[names.size()];
            for (int i = 0; i < names.size(); i++)
                loadTile(i, names.get(i), Boolean.parseBoolean(solids.get(i)));
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo tile_data.txt", e);
        }
    }

    /**
     * Carga el tile.
     *
     * @param i     indice del tile.
     * @param name  nombre del tile.
     * @param solid si es solido o no.
     */
    private void loadTile(int i, String name, boolean solid) {
        tileData[i] = new Tile();
        tileData[i].texture = Utils.scaleImage(Utils.loadImage("textures/tiles/" + name), tile_size, tile_size);
        tileData[i].solid = solid;
    }

    /**
     * Carga todos los mapas que componen al mundo.
     */
    public void loadMaps() {
        loadMap("maps/nix.txt", NIX, "Nix");
        loadMap("maps/nix_indoor01.txt", NIX_INDOOR_01, "Nix Indoor 01");
        loadMap("maps/dungeon01.txt", DUNGEON_01, "Dungeon 01");
        loadMap("maps/dungeon02.txt", DUNGEON_02, "Dungeon 02");
    }

    /**
     * Carga el mapa utilizando la ruta especificada y almacena cada valor (tile) leido del archivo en la matriz.
     *
     * @param path ruta del recurso.
     * @param map  numero del mapa como clave.
     * @param name nombre del mapa como valor.
     */
    public void loadMap(String path, int map, String name) {
        maps.put(map, name);
        int row = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader((Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)))))) {
            for (row = 0; row < MAX_MAP_ROW; row++) {
                String line = br.readLine();
                String[] numbers = line.split(" ");
                for (int col = 0; col < MAX_MAP_COL; col++)
                    tileIndex[map][row][col] = Integer.parseInt(numbers[col]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo " + path + " en la linea " + (row + 1), e);
        }
    }

    public void changeArea() {
        // Si hay un cambio de area
        if (nextArea != area) {
            if (nextArea == DUNGEON) game.playMusic(music_dungeon);
            if (area == DUNGEON && nextArea == OUTSIDE) game.stopMusic();
        }
        area = nextArea;
        createMOBs();
        createNPCs();
    }

    /**
     * Crea las entidades.
     */
    public void createEntities() {
        createItems();
        createNPCs();
        createMOBs();
        createInteractiveTile();
    }

    /**
     * Crea los items.
     */
    public void createItems() {
        int i = 0;
        items[NIX][i++] = new Axe(game, this, 33, 7);
        items[NIX][i++] = new PotionRed(game, this, 20, 21, 5);
        items[NIX][i++] = new Key(game, this, 21, 21, 1);
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
     * Crea los npcs.
     */
    public void createNPCs() {
        int i = 0, j = 0, k = 0;

        npcs[NIX][i++] = new Oldman(game, this, 23, 18);
        npcs[NIX][i] = new BigRock(game, this, 26, 21);

        npcs[NIX_INDOOR_01][j] = new Trader(game, this, 12, 7);

        npcs[DUNGEON_01][k++] = new BigRock(game, this, 20, 25);
        npcs[DUNGEON_01][k++] = new BigRock(game, this, 11, 18);
        npcs[DUNGEON_01][k] = new BigRock(game, this, 23, 14);
    }

    /**
     * Crea los mobs.
     */
    public void createMOBs() {
        int i = 0, j = 0;
        // mobs[NIX][i++] = new RedSlime(game, this, 21, 23);
        mobs[NIX][i++] = new Slime(game, this, 23, 41);
        mobs[NIX][i++] = new Slime(game, this, 24, 37);
        mobs[NIX][i++] = new Slime(game, this, 34, 42);
        mobs[NIX][i++] = new Slime(game, this, 38, 42);
        mobs[NIX][i++] = new Orc(game, this, 12, 33);

        mobs[DUNGEON_01][j++] = new Bat(game, this, 34, 39);
        mobs[DUNGEON_01][j++] = new Bat(game, this, 36, 25);
        mobs[DUNGEON_01][j++] = new Bat(game, this, 39, 26);
        mobs[DUNGEON_01][j++] = new Bat(game, this, 28, 11);
        mobs[DUNGEON_01][j++] = new Bat(game, this, 10, 19);

    }

    /**
     * Crea los tiles interactivos.
     */
    public void createInteractiveTile() {
        int i = 0;
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
