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
		map = new int[game.maxWorldCol][game.maxWorldRow];
		initTiles();
		loadMap("maps/world01.txt");
	}

	private void initTiles() {
		tile = new Tile[10];
		createTile(0, Assets.grass, false);
		createTile(1, Assets.wall, true);
		createTile(2, Assets.water, true);
		createTile(3, Assets.earth, false);
		createTile(4, Assets.tree, true);
		createTile(5, Assets.sand, false);
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
