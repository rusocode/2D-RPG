package com.craivet.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.utils.Utils;

/**
 * TODO final?
 */

public class Assets {

	// Fonts
	public static Font medieval1 = Utils.loadFont("font/Blackpearl-vPxA.ttf", 22);
	public static Font medieval2 = Utils.loadFont("font/KnightsQuest-nvDV.ttf", 32);
	public static Font medieval3 = Utils.loadFont("font/Vecna-oppx.ttf", 32);

	// Entities
	public static SpriteSheet player_movement = new SpriteSheet(Utils.loadImage("textures/entity/player/movement.png"));
	public static SpriteSheet player_attack = new SpriteSheet(Utils.loadImage("textures/entity/player/attack.png"));
	public static SpriteSheet oldman = new SpriteSheet(Utils.loadImage("textures/entity/oldman.png"));
	public static SpriteSheet slime = new SpriteSheet(Utils.loadImage("textures/entity/slime.png"));

	// Objects
	public static BufferedImage boots = Utils.loadImage("textures/objs/boots.png");
	public static BufferedImage chest = Utils.loadImage("textures/objs/chest.png");
	public static BufferedImage door = Utils.loadImage("textures/objs/door.png");
	public static BufferedImage key = Utils.loadImage("textures/objs/key.png");
	public static BufferedImage heart_full = Utils.loadImage("textures/objs/heart_full.png");
	public static BufferedImage heart_half = Utils.loadImage("textures/objs/heart_half.png");
	public static BufferedImage heart_blank = Utils.loadImage("textures/objs/heart_blank.png");
	public static BufferedImage sword_normal = Utils.loadImage("textures/objs/sword_normal.png");
	public static BufferedImage shield_wood = Utils.loadImage("textures/objs/shield_wood.png");

	// Tiles
	public static BufferedImage earth = Utils.loadImage("textures/tiles/earth.png");
	public static BufferedImage floor01 = Utils.loadImage("textures/tiles/floor01.png");
	public static BufferedImage grass00 = Utils.loadImage("textures/tiles/grass00.png");
	public static BufferedImage grass01 = Utils.loadImage("textures/tiles/grass01.png");
	public static BufferedImage hut = Utils.loadImage("textures/tiles/hut.png");
	public static BufferedImage road00 = Utils.loadImage("textures/tiles/road00.png");
	public static BufferedImage road01 = Utils.loadImage("textures/tiles/road01.png");
	public static BufferedImage road02 = Utils.loadImage("textures/tiles/road02.png");
	public static BufferedImage road03 = Utils.loadImage("textures/tiles/road03.png");
	public static BufferedImage road04 = Utils.loadImage("textures/tiles/road04.png");
	public static BufferedImage road05 = Utils.loadImage("textures/tiles/road05.png");
	public static BufferedImage road06 = Utils.loadImage("textures/tiles/road06.png");
	public static BufferedImage road07 = Utils.loadImage("textures/tiles/road07.png");
	public static BufferedImage road08 = Utils.loadImage("textures/tiles/road08.png");
	public static BufferedImage road09 = Utils.loadImage("textures/tiles/road09.png");
	public static BufferedImage road10 = Utils.loadImage("textures/tiles/road10.png");
	public static BufferedImage road11 = Utils.loadImage("textures/tiles/road11.png");
	public static BufferedImage road12 = Utils.loadImage("textures/tiles/road12.png");
	public static BufferedImage table01 = Utils.loadImage("textures/tiles/table01.png");
	public static BufferedImage tree = Utils.loadImage("textures/tiles/tree.png");
	public static BufferedImage wall = Utils.loadImage("textures/tiles/wall.png");
	public static BufferedImage water00 = Utils.loadImage("textures/tiles/water00.png");
	public static BufferedImage water01 = Utils.loadImage("textures/tiles/water01.png");
	public static BufferedImage water02 = Utils.loadImage("textures/tiles/water02.png");
	public static BufferedImage water03 = Utils.loadImage("textures/tiles/water03.png");
	public static BufferedImage water04 = Utils.loadImage("textures/tiles/water04.png");
	public static BufferedImage water05 = Utils.loadImage("textures/tiles/water05.png");
	public static BufferedImage water06 = Utils.loadImage("textures/tiles/water06.png");
	public static BufferedImage water07 = Utils.loadImage("textures/tiles/water07.png");
	public static BufferedImage water08 = Utils.loadImage("textures/tiles/water08.png");
	public static BufferedImage water09 = Utils.loadImage("textures/tiles/water09.png");
	public static BufferedImage water10 = Utils.loadImage("textures/tiles/water10.png");
	public static BufferedImage water11 = Utils.loadImage("textures/tiles/water11.png");
	public static BufferedImage water12 = Utils.loadImage("textures/tiles/water12.png");
	public static BufferedImage water13 = Utils.loadImage("textures/tiles/water13.png");

	private Assets() {
	}

}
