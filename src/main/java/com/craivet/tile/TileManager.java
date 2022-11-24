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
		map = new int[game.maxScreenCol][game.maxScreenRow];
		getTileImage();
		loadMap("maps/map0.txt");
	}

	public void getTileImage() {
		tile[0] = new Tile();
		tile[0].image = Assets.grass;
		tile[1] = new Tile();
		tile[1].image = Assets.wall;
		tile[2] = new Tile();
		tile[2].image = Assets.water;
	}

	public void loadMap(String path) {
		try { // TODO try-with-resources
			BufferedReader br = new BufferedReader(new InputStreamReader((Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)))));
			for (int row = 0; row < game.maxScreenRow; row++) {
				String line = br.readLine();
				String[] numbers = line.split(" "); // Divide la linea con el delimitante especificado y obtiene los numeros de tile uno por uno
				for (int col = 0; col < game.maxScreenCol; col++)
					map[col][row] = Integer.parseInt(numbers[col]);
			}
			br.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dibuja los tiles uno por uno usando la matriz para obtenerlos.
	 */
	public void draw(Graphics2D g2) {
		int x = 0, y = 0;
		for (int row = 0; row < game.maxScreenRow; row++) {
			for (int col = 0; col < game.maxScreenCol; col++) {
				g2.drawImage(tile[map[col][row]].image, x, y, game.tileSize, game.tileSize, null);
				x += game.tileSize;
			}
			x = 0;
			y += game.tileSize;
		}
	}

}
