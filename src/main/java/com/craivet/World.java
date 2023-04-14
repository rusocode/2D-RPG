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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.craivet.utils.Constants.*;

/**
 * En el mundo se crean los tiles, las entidades y el entorno.
 * <p>
 * TODO Realmente se crean las entidades aca o en la clase Entity?. Logicamente la clase Entity declara los componentes
 * que componen cada entidad y el mundo las crea.
 */

public class World {

    // Managers
    private final TileManager tileManager;
    private final EntityManager entityManager;
    public final EnvironmentManager environmentManager;

    private InputStream is;
    private BufferedReader br;

    // Tiles
    public int map;
    public HashMap<Integer, String> maps = new HashMap<>();
    public Tile[] tile;
    public int[][][] tileIndex;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> solids = new ArrayList<>();

    // Entities
    public Player player;
    public List<Entity> entities = new ArrayList<>();
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
        tileManager = new TileManager(game, this);
        entityManager = new EntityManager(game, this);
        environmentManager = new EnvironmentManager(this);

        loadTiles("maps/testdata.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("maps/test.txt"))))) {
            String[] maxTile = br.readLine().split(" ");
            MAX_WORLD_ROW = maxTile.length;
            MAX_WORLD_COL = maxTile.length;
            tileIndex = new int[MAX_MAP][MAX_WORLD_ROW][MAX_WORLD_COL];
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo maps/test.txt", e);
        }

        // loadMap("maps/nix.txt", NIX, "Nix");
        // loadMap("maps/nix_trade.txt", NIX_TRADE, "Nix trade");
        loadMap("maps/test.txt", 0, "Abandoned Island");

    }

    /**
     * Actualiza las entidades y el entorno.
     */
    public void update() {
        entityManager.update();
        environmentManager.update();
    }

    /**
     * Renderiza los tiles, las entidades y el entorno.
     *
     * @param g2 componente grafico.
     */
    public void render(Graphics2D g2) {
        tileManager.render(g2);
        entityManager.render(g2);
        environmentManager.render(g2);
    }

    /**
     * Lee los valores de cada tile del mapa (nombre y estado solido) y los agrega a sus respectivas listas. Luego
     * utiliza esos valores para cargar todos los tiles del mapa dentro de un array.
     *
     * @param path la ruta del recurso.
     */
    private void loadTiles(String path) {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path))))) {
            while ((line = br.readLine()) != null) {
                names.add(line);
                solids.add(br.readLine());
            }
            tile = new Tile[names.size()]; // Crea un array con la cantidad de tiles del mapa
            for (int i = 0; i < names.size(); i++)
                loadTile(i, names.get(i), Boolean.parseBoolean(solids.get(i)));
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo " + path, e);
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
        tile[i] = new Tile();
        tile[i].texture = Utils.scaleImage(Utils.loadImage("textures/tiles/" + name), tile_size, tile_size);
        tile[i].solid = solid;
    }

    /**
     * Carga el mapa utilizando la ruta especificada y almacena cada valor (tile) leido del archivo en la matriz.
     *
     * @param path la ruta del recurso.
     */
    public void loadMap(String path, int map, String name) {
        maps.put(map, name);
        int row = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader((Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)))))) {
            for (row = 0; row < MAX_WORLD_ROW; row++) {
                String line = br.readLine();
                String[] numbers = line.split(" ");
                for (int col = 0; col < MAX_WORLD_COL; col++)
                    tileIndex[map][row][col] = Integer.parseInt(numbers[col]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo " + path + " en la linea " + (row + 1), e);
        }
    }

}
