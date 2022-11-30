package com.craivet.object;

import com.craivet.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {

	public String name;
	public BufferedImage image;
	public int worldX, worldY;
	public boolean collision;

	public void draw(Graphics2D g2, Game game) {
		int screenX = (worldX - game.player.worldX) + game.player.screenX;
		int screenY = (worldY - game.player.worldY) + game.player.screenY;
		if (worldX + game.tileSize > game.player.worldX - game.player.screenX &&
				worldX - game.tileSize < game.player.worldX + game.player.screenX &&
				worldY + game.tileSize > game.player.worldY - game.player.screenY &&
				worldY - game.tileSize < game.player.worldY + game.player.screenY) {
			g2.drawImage(image, screenX, screenY, game.tileSize, game.tileSize, null);
		}
	}

}
