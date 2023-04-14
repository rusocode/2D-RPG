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
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();

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
        is = getClass().getClassLoader().getResourceAsStream("maps/testdata.txt");
        br = new BufferedReader(new InputStreamReader(is));

        getTiles();
        loadTiles();

        // Obtiene la cantidad de filas y columnas del mapa
        is = getClass().getClassLoader().getResourceAsStream("maps/test.txt");
        br = new BufferedReader(new InputStreamReader(is));

        try {
            String line2 = br.readLine();
            String[] maxTile = line2.split(" ");
            MAX_WORLD_ROW = maxTile.length;
            MAX_WORLD_COL = maxTile.length;
            tileIndex = new int[MAX_MAP][MAX_WORLD_ROW][MAX_WORLD_COL];
            br.close();
        } catch (IOException e) {
            System.out.println("Exception!");
        }

        // loadMaps();
        loadMap("maps/test.txt", NIX, "Nix");

        player = new Player(game, this);
        tileManager = new TileManager(game, this);
        entityManager = new EntityManager(game, this);
        environmentManager = new EnvironmentManager(this);
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

    private void getTiles() {
         String line;
        try {
            while ((line = br.readLine()) != null) {
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tile = new Tile[fileNames.size()];
    }

    /**
     * Carga los tiles.
     *
     * <p>Los primeros 10 tiles definen el marcador de posicion (utilizando el tile grass00) para poder trabajar con
     * numeros de dos digitos y mejorar la lectura del archivo world.txt.
     */
    private void loadTiles() {
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName;
            boolean solid;
            fileName = fileNames.get(i);
            solid = Boolean.parseBoolean(collisionStatus.get(i));
            loadTile(i, fileName, solid);
        }
    }

    /**
     * Carga los mapas.
     */
    private void loadMaps() {
        loadMap("maps/nix.txt", NIX, "Nix");
        loadMap("maps/nix_trade.txt", NIX_TRADE, "Nix trade");
    }

    /**
     * Carga el tile.
     *
     * @param i        el indice del tile.
     * @param fileName el nombre del tile.
     * @param solid    si es solido o no.
     */
    private void loadTile(int i, String fileName, boolean solid) {
        tile[i] = new Tile();
        tile[i].texture = Utils.loadImage("textures/tiles/" + fileName);
        tile[i].texture = Utils.scaleImage(tile[i].texture, tile_size, tile_size);
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
