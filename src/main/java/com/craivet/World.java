package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.EntityManager;
import com.craivet.entity.Player;
import com.craivet.entity.item.Item;
import com.craivet.entity.mob.Mob;
import com.craivet.entity.npc.Npc;
import com.craivet.entity.projectile.Projectile;
import com.craivet.environment.EnvironmentManager;
import com.craivet.tile.Interactive;
import com.craivet.tile.Tile;
import com.craivet.tile.TileManager;
import com.craivet.utils.Utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.craivet.utils.Global.*;

/**
 * En el mundo se crean los tiles, las entidades y el entorno que son actualizados y dibujados por sus respectivos
 * administradores.
 * <p>
 * El tamaño de cada mapa es el mismo para todos. Cada mapa tiene un tamaño de 50x50 tiles y este valor es fijo hasta
 * que se aplique una forma de utilizar diferentes dimensiones.
 */

public class World {

    // Managers
    private final TileManager tiles;
    private final EntityManager entitites;
    public final EnvironmentManager environment;

    // Tiles
    public int map;
    public HashMap<Integer, String> maps = new HashMap<>();
    public Tile[] tileData;
    public int[][][] tileIndex = new int[MAX_MAP][MAX_MAP_ROW][MAX_MAP_COL];
    // TODO Creo que se podria reemplazar por un HashMap
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> solids = new ArrayList<>();

    // Entities
    public Player player;
    public List<Entity> itemsList = new ArrayList<>();
    public List<Entity> particles = new ArrayList<>();
    public Entity[][] items = new Item[MAX_MAP][20];
    public Entity[][] mobs = new Mob[MAX_MAP][20];
    public Entity[][] npcs = new Npc[MAX_MAP][10];
    public Entity[][] projectiles = new Projectile[MAX_MAP][20];
    public Interactive[][] interactives = new Interactive[MAX_MAP][50];

    public boolean drawPath;

    public World(Game game) {
        player = new Player(game, this);
        tiles = new TileManager(game, this);
        entitites = new EntityManager(game, this);
        environment = new EnvironmentManager(this);

        loadTiles();
        loadMaps();
    }

    /**
     * Actualiza las entidades y el entorno.
     */
    public void update() {
        entitites.update();
        environment.update();
    }

    /**
     * Renderiza los tiles, las entidades y el entorno.
     *
     * @param g2 componente grafico.
     */
    public void render(Graphics2D g2) {
        tiles.render(g2);
        entitites.render(g2);
        environment.render(g2);
    }

    /**
     * Lee los datos de cada tile (nombre y estado solido) desde el archivo "tile_data.txt" y los agrega a sus
     * respectivas listas. Luego utiliza esos datos para cargar (crear) todos los tiles dentro de un array.
     */
    private void loadTiles() {
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
     * @param i     el indice del tile.
     * @param name  el nombre del tile.
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
    private void loadMaps() {
        loadMap("maps/nix.txt", NIX, "Nix");
        loadMap("maps/nix_indoor01.txt", NIX_INDOOR_01, "Nix Indoor 01");
    }

    /**
     * Carga el mapa utilizando la ruta especificada y almacena cada valor (tile) leido del archivo en la matriz.
     *
     * @param path la ruta del recurso.
     * @param map  el numero del mapa como clave.
     * @param name el nombre del mapa como valor.
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

}
