package com.craivet.tile;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.gfx.Assets;
import com.craivet.input.KeyManager;
import com.craivet.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static com.craivet.utils.Constants.*;

public class World {

	private final Game game;
	public EntityManager entityManager;

	public final Tile[] tile;
	public final int[][][] tileIndex;
	private boolean drawPath = false;


	public World(Game game, KeyManager key) {
		tile = new Tile[50];
		tileIndex = new int[MAX_MAP][MAX_WORLD_ROW][MAX_WORLD_COL];
		initTiles();
		loadMap("maps/map1.txt", 0); // TODO Crear constantes
		loadMap("maps/interior1.txt", 1);

		this.game = game;
		entityManager = new EntityManager(game, key);

	}

	public void update() {
		entityManager.update();
	}

	/**
	 * Renderiza los tiles dentro de la vista de la camara aplicando los desplazamientos a cada uno.
	 *
	 * @param g2 componente grafico.
	 */
	public void render(Graphics2D g2) {
		// TODO Se podria reemplazar llamada a la actualizacion de la clase Tile
		// long drawStart = System.nanoTime();

		// Calcula los desplazamientos
		int xOffset = entityManager.player.worldX - entityManager.player.screenX; // 1104 - 456 = 648
		int yOffset = entityManager.player.worldY - entityManager.player.screenY;

		// Calcula los tiles que estan dentro de la vista de la camara
		int yStart = Math.max(0, yOffset / tile_size);
		int yEnd = Math.min(MAX_WORLD_ROW, (yOffset + SCREEN_HEIGHT) / tile_size + 1);
		int xStart = Math.max(0, xOffset / tile_size); // 648 / 48 = 13
		int xEnd = Math.min(MAX_WORLD_COL, (xOffset + SCREEN_WIDTH) / tile_size + 1);

		for (int y = yStart; y < yEnd; y++) {
			for (int x = xStart; x < xEnd; x++) {
				final int tileIndex = this.tileIndex[game.map][y][x];
				final BufferedImage tileImage = tile[tileIndex].texture;
				g2.drawImage(tileImage, x * tile_size - xOffset, y * tile_size - yOffset, null);
				// g2.drawRect(x * tile_size - xOffset, y * tile_size - yOffset, tile_size, tile_size); // Dibuja una grilla
			}
		}

		// System.out.println("Draw time: " + (System.nanoTime() - drawStart) / 1_000_000 + " ms");

		if (drawPath) {
			g2.setColor(new Color(255, 0, 0, 70));
			for (int i = 0; i < entityManager.aStar.pathList.size(); i++) {
				int worldX = entityManager.aStar.pathList.get(i).col * tile_size;
				int worldY = entityManager.aStar.pathList.get(i).row * tile_size;
				int screenX = worldX - entityManager.player.worldX + entityManager.player.screenX;
				int screenY = worldY - entityManager.player.worldY + entityManager.player.screenY;
				g2.fillRect(screenX, screenY, tile_size, tile_size);
			}
		}

		entityManager.render(g2);

	}

	/**
	 * Inicializa todos los tiles que componen al mundo.
	 *
	 * <p>Los primeros 10 tiles definen el marcador de posicion (utilizando el tile grass00) para poder trabajar con
	 * numeros de dos digitos y mejorar la lectura del archivo world.txt.
	 */
	private void initTiles() {
		for (int i = 0; i < 10; i++) createTile(i, Assets.tile_grass00, false);
		createTile(10, Assets.tile_grass00, false);
		createTile(11, Assets.tile_grass01, false);
		createTile(12, Assets.tile_water00, true);
		createTile(13, Assets.tile_water01, true);
		createTile(14, Assets.tile_water02, true);
		createTile(15, Assets.tile_water03, true);
		createTile(16, Assets.tile_water04, true);
		createTile(17, Assets.tile_water05, true);
		createTile(18, Assets.tile_water06, true);
		createTile(19, Assets.tile_water07, true);
		createTile(20, Assets.tile_water08, true);
		createTile(21, Assets.tile_water09, true);
		createTile(22, Assets.tile_water10, true);
		createTile(23, Assets.tile_water11, true);
		createTile(24, Assets.tile_water12, true);
		createTile(25, Assets.tile_water13, true);
		createTile(26, Assets.tile_road00, false);
		createTile(27, Assets.tile_road01, false);
		createTile(28, Assets.tile_road02, false);
		createTile(29, Assets.tile_road03, false);
		createTile(30, Assets.tile_road04, false);
		createTile(31, Assets.tile_road05, false);
		createTile(32, Assets.tile_road06, false);
		createTile(33, Assets.tile_road07, false);
		createTile(34, Assets.tile_road08, false);
		createTile(35, Assets.tile_road09, false);
		createTile(36, Assets.tile_road10, false);
		createTile(37, Assets.tile_road11, false);
		createTile(38, Assets.tile_road12, false);
		createTile(39, Assets.tile_earth, false);
		createTile(40, Assets.tile_wall, true);
		createTile(41, Assets.tile_tree, true);
		createTile(42, Assets.tile_hut, false);
		createTile(43, Assets.tile_floor01, false);
		createTile(44, Assets.tile_table01, true);
	}

	/**
	 * Crea el tile.
	 *
	 * @param i       el indice del tile.
	 * @param texture la imagen del tile.
	 * @param solid   si es solido o no.
	 */
	private void createTile(int i, BufferedImage texture, boolean solid) {
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
