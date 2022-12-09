package com.craivet.tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

public class TileManager {

	Game game;
	// TODO Nombre world seria mas logico
	public int[][] map; // Los tiles se almacenan en esta matriz
	public Tile[] tile;

	public TileManager(Game game) {
		this.game = game;
		tile = new Tile[50];
		map = new int[game.maxWorldCol][game.maxWorldRow];
		initTiles();
		loadMap("maps/worldV2.txt");
	}

	private void initTiles() {

		// Marcador de posicion para trabajar con numeros de dos digitos y mejorar la lectura del archivo world.txt
		createTile(0, Assets.grass00, false);
		createTile(1, Assets.grass00, false);
		createTile(2, Assets.grass00, false);
		createTile(3, Assets.grass00, false);
		createTile(4, Assets.grass00, false);
		createTile(5, Assets.grass00, false);
		createTile(6, Assets.grass00, false);
		createTile(7, Assets.grass00, false);
		createTile(8, Assets.grass00, false);
		createTile(9, Assets.grass00, false);
		// Marcador de posicion

		createTile(10, Assets.grass00, false);
		createTile(11, Assets.grass01, false);
		createTile(12, Assets.water00, true);
		createTile(13, Assets.water01, true);
		createTile(14, Assets.water02, true);
		createTile(15, Assets.water03, true);
		createTile(16, Assets.water04, true);
		createTile(17, Assets.water05, true);
		createTile(18, Assets.water06, true);
		createTile(19, Assets.water07, true);
		createTile(20, Assets.water08, true);
		createTile(21, Assets.water09, true);
		createTile(22, Assets.water10, true);
		createTile(23, Assets.water11, true);
		createTile(24, Assets.water12, true);
		createTile(25, Assets.water13, true);
		createTile(26, Assets.road00, false);
		createTile(27, Assets.road01, false);
		createTile(28, Assets.road02, false);
		createTile(29, Assets.road03, false);
		createTile(30, Assets.road04, false);
		createTile(31, Assets.road05, false);
		createTile(32, Assets.road06, false);
		createTile(33, Assets.road07, false);
		createTile(34, Assets.road08, false);
		createTile(35, Assets.road09, false);
		createTile(36, Assets.road10, false);
		createTile(37, Assets.road11, false);
		createTile(38, Assets.road12, false);
		createTile(39, Assets.earth, false);
		createTile(40, Assets.wall, true);
		createTile(41, Assets.tree, true);
	}

	private void createTile(int i, BufferedImage image, boolean collision) {
		tile[i] = new Tile();
		tile[i].image = Utils.scaleImage(image, game.tileSize, game.tileSize);
		tile[i].collision = collision;
	}

	public void loadMap(String path) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader((Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)))))) {
			for (int row = 0; row < game.maxWorldRow; row++) {
				String line = br.readLine();
				String[] numbers = line.split(" ");
				for (int col = 0; col < game.maxWorldCol; col++)
					map[col][row] = Integer.parseInt(numbers[col]);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dibuja los tiles aplicando el desplazamiento a cada uno.
	 */
	public void draw(Graphics2D g2) {
		for (int row = 0; row < game.maxWorldRow; row++) {
			for (int col = 0; col < game.maxWorldCol; col++) {
				int worldX = col * game.tileSize;
				int worldY = row * game.tileSize;
				/* Resta la posicion del player (en el mundo) al tile y suma los desplazamientos de la pantalla para que
				 * quede en el centro de esta. Este desplazamiento compensa la diferencia y obtiene las coordenadas
				 * correctas para los tiles en la pantalla. Los tiles se dibujan en base al "movimiento" del player. El
				 * player se mantiene estatico en el centro de la pantalla. */
				int screenX = (worldX - game.player.worldX) + game.player.screenX;
				int screenY = (worldY - game.player.worldY) + game.player.screenY;
				// Dibuja los tiles que estan dentro de la camara para una mejor eficiencia
				if (worldX + game.tileSize > game.player.worldX - game.player.screenX &&
						worldX - game.tileSize < game.player.worldX + game.player.screenX &&
						worldY + game.tileSize > game.player.worldY - game.player.screenY &&
						worldY - game.tileSize < game.player.worldY + game.player.screenY) {
					g2.drawImage(tile[map[col][row]].image, screenX, screenY, null);
				}
			}
		}
	}

}
