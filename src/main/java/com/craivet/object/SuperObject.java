package com.craivet.object;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;

public class SuperObject {

	public String name;
	public BufferedImage image, image2, image3;
	public int worldX, worldY;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX, solidAreaDefaultY;
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
