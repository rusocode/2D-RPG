package com.craivet.tile;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static com.craivet.utils.Constants.*;

public class TileManager {

	private final Game game;
	public final Tile[] tile;
	public final int[][][] tileIndex;

	public TileManager(Game game) {
		this.game = game;
		tile = new Tile[50];
		tileIndex = new int[MAX_MAP][MAX_WORLD_ROW][MAX_WORLD_COL];
		initTiles();
		loadMap("maps/map3.txt", 0);
		loadMap("maps/interior1.txt", 1);
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
		tile[i].texture = Utils.scaleImage(texture, TILE_SIZE, TILE_SIZE);
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
			// throw new RuntimeException(e);
			throw new RuntimeException("Error al leer el archivo " + path + " en la linea " + (row + 1), e);
		}
	}

	/**
	 * Carga los tiles usando la matriz como referencia y los dibuja (dentro de la camara) aplicando el desplazamiento a
	 * cada uno.
	 *
	 * @param g2 componente grafico.
	 */
	public void draw(Graphics2D g2) {
		final int playerScreenY = game.player.screenY;
		final int playerScreenX = game.player.screenX;
		final int playerWorldY = game.player.worldY;
		final int playerWorldX = game.player.worldX;
		for (int row = 0; row < MAX_WORLD_ROW; row++) {
			for (int col = 0; col < MAX_WORLD_COL; col++) {
				final int tileWorldY = row * TILE_SIZE;
				final int tileWorldX = col * TILE_SIZE;
				/* Resta la posicion del player (en el mundo) al tile y suma los desplazamientos de la pantalla para que
				 * quede en el centro de esta. Este desplazamiento compensa la diferencia y obtiene las coordenadas
				 * correctas para los tiles en la pantalla. Los tiles se dibujan en base al "movimiento" del player. El
				 * player se mantiene estatico en el centro de la pantalla. */
				final int screenY = (tileWorldY - playerWorldY) + playerScreenY;
				final int screenX = (tileWorldX - playerWorldX) + playerScreenX;

				final boolean isInCamera =
						tileWorldX + TILE_SIZE > playerWorldX - playerScreenX &&
								tileWorldX - TILE_SIZE < playerWorldX + playerScreenX &&
								tileWorldY + TILE_SIZE > playerWorldY - playerScreenY &&
								tileWorldY - TILE_SIZE < playerWorldY + playerScreenY;

				// Si el tile se encuentra dentro de la camara
				if (isInCamera) {
					final int tileIndex = this.tileIndex[game.currentMap][row][col];
					final BufferedImage tileImage = tile[tileIndex].texture;
					g2.drawImage(tileImage, screenX, screenY, null);
					// g2.drawRect(screenX, screenY, TILE_SIZE, TILE_SIZE); // Dibuja una grilla
				}
			}
		}
	}

}
