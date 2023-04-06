package com.craivet;

import com.craivet.entity.Entity;
import com.craivet.entity.EntityManager;
import com.craivet.entity.item.Item;
import com.craivet.entity.mob.Mob;
import com.craivet.entity.npc.Npc;
import com.craivet.entity.projectile.Projectile;
import com.craivet.gfx.Assets;
import com.craivet.tile.Interactive;
import com.craivet.tile.Tile;
import com.craivet.tile.TileManager;
import com.craivet.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.craivet.utils.Constants.*;

/**
 * En el mundo se crean los tiles y las entidades que lo componen.
 * <p>
 * TODO Realmente se crean las entidades aca o en la clase Entity?. Logicamente la clase Entity declara los componentes
 * que componen cada entidad y el mundo las crea.
 */

public class World {

    private final Game game;
    private final TileManager tileManager;
    private final EntityManager entityManager;

    public int map;
    public Tile[] tile;
    public int[][][] tileIndex;

    // Entities
    public List<Entity> entities = new ArrayList<>();
    public List<Entity> itemsList = new ArrayList<>();
    public List<Entity> particles = new ArrayList<>();
    public Entity[][] items = new Item[MAX_MAP][20];
    public Entity[][] mobs = new Mob[MAX_MAP][20];
    public Entity[][] npcs = new Npc[MAX_MAP][10];
    public Entity[][] projectiles = new Projectile[MAX_MAP][20];
    public Interactive[][] interactives = new Interactive[MAX_MAP][50];

    public boolean drawPath;
    private long renderStart;
    private int lastFrames;

    public World(Game game) {
        tile = new Tile[50];
        tileIndex = new int[MAX_MAP][MAX_WORLD_ROW][MAX_WORLD_COL];
        this.game = game;
        tileManager = new TileManager(game, this);
        entityManager = new EntityManager(game, this);
        loadTiles();
        loadMaps();
    }

    /**
     * Actualiza las entidades.
     */
    public void update() {
        entityManager.update();
    }

    /**
     * Renderiza los tiles y las entidades.
     *
     * @param g2 componente grafico.
     */
    public void render(Graphics2D g2) {

        // Debug mode
        if (game.key.t) renderStart = System.nanoTime();

        tileManager.render(g2);
        entityManager.render(g2);

        if (game.key.t) {
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 8, y = SCREEN_HEIGHT - 65, gap = 20;
            if (game.showFPS) {
                g2.drawString("FPS: " + game.framesInRender, x, y);
                lastFrames = game.framesInRender;
                game.showFPS = false;
            } else
                g2.drawString("FPS: " + lastFrames, x, y); // Muestra los ultimos fps hasta que se complete el segundo
            y += gap;
            g2.drawString("X: " + (game.player.worldX + game.player.hitbox.x) / tile_size, x, y);
            y += gap;
            g2.drawString("Y: " + (game.player.worldY + game.player.hitbox.y) / tile_size, x, y);
            y += gap;
            g2.drawString("Draw time: " + (System.nanoTime() - renderStart) / 1_000_000 + " ms", x, y);
        }

    }

    /**
     * Carga los tiles.
     *
     * <p>Los primeros 10 tiles definen el marcador de posicion (utilizando el tile grass00) para poder trabajar con
     * numeros de dos digitos y mejorar la lectura del archivo world.txt.
     */
    private void loadTiles() {
        for (int i = 0; i < 10; i++) loadTile(i, Assets.tile_grass00, false);
        loadTile(10, Assets.tile_grass00, false);
        loadTile(11, Assets.tile_grass01, false);
        loadTile(12, Assets.tile_water00, true);
        loadTile(13, Assets.tile_water01, true);
        loadTile(14, Assets.tile_water02, true);
        loadTile(15, Assets.tile_water03, true);
        loadTile(16, Assets.tile_water04, true);
        loadTile(17, Assets.tile_water05, true);
        loadTile(18, Assets.tile_water06, true);
        loadTile(19, Assets.tile_water07, true);
        loadTile(20, Assets.tile_water08, true);
        loadTile(21, Assets.tile_water09, true);
        loadTile(22, Assets.tile_water10, true);
        loadTile(23, Assets.tile_water11, true);
        loadTile(24, Assets.tile_water12, true);
        loadTile(25, Assets.tile_water13, true);
        loadTile(26, Assets.tile_road00, false);
        loadTile(27, Assets.tile_road01, false);
        loadTile(28, Assets.tile_road02, false);
        loadTile(29, Assets.tile_road03, false);
        loadTile(30, Assets.tile_road04, false);
        loadTile(31, Assets.tile_road05, false);
        loadTile(32, Assets.tile_road06, false);
        loadTile(33, Assets.tile_road07, false);
        loadTile(34, Assets.tile_road08, false);
        loadTile(35, Assets.tile_road09, false);
        loadTile(36, Assets.tile_road10, false);
        loadTile(37, Assets.tile_road11, false);
        loadTile(38, Assets.tile_road12, false);
        loadTile(39, Assets.tile_earth, false);
        loadTile(40, Assets.tile_wall, true);
        loadTile(41, Assets.tile_tree, true);
        loadTile(42, Assets.tile_hut, false);
        loadTile(43, Assets.tile_floor01, false);
        loadTile(44, Assets.tile_table01, true);
    }

    /**
     * Carga los mapas.
     */
    private void loadMaps() {
        loadMap("maps/nix.txt", NIX);
        loadMap("maps/nix_trade.txt", NIX_TRADE);
    }

    /**
     * Carga el tile.
     *
     * @param i       el indice del tile.
     * @param texture la imagen del tile.
     * @param solid   si es solido o no.
     */
    private void loadTile(int i, BufferedImage texture, boolean solid) {
        tile[i] = new Tile();
        tile[i].texture = Utils.scaleImage(texture, tile_size, tile_size);
        tile[i].solid = solid;
    }

    /**
     * Carga el mapa utilizando la ruta especificada y almacena cada valor (tile) leido del archivo en la matriz.
     *
     * @param path la ruta del recurso.
     */
    public void loadMap(String path, int map) {
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
