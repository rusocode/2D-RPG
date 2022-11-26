package com.craivet.tile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import com.craivet.Game;
import com.craivet.gfx.Assets;

public class TileManager {

	Game game;
	int[][] map; // Los tiles se almacenan en esta matriz
	Tile[] tile;

	public TileManager(Game game) {
		this.game = game;
		tile = new Tile[10];
		map = new int[game.maxWorldCol][game.maxWorldRow];
		getTileImage();
		loadMap("maps/world01.txt");
	}

	public void getTileImage() {
		tile[0] = new Tile();
		tile[0].image = Assets.grass;
		tile[1] = new Tile();
		tile[1].image = Assets.wall;
		tile[2] = new Tile();
		tile[2].image = Assets.water;
		tile[3] = new Tile();
		tile[3].image = Assets.earth;
		tile[4] = new Tile();
		tile[4].image = Assets.tree;
		tile[5] = new Tile();
		tile[5].image = Assets.sand;
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
					g2.drawImage(tile[map[col][row]].image, screenX, screenY, game.tileSize, game.tileSize, null);
				}
			}
		}
	}

}
