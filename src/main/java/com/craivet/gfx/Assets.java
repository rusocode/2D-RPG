package com.craivet.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import com.craivet.utils.Utils;

/**
 * TODO Los metodos loadFont, loadSound y loadImage pueden ser llamados desde un AssetsManager
 */

public final class Assets {

	// Fonts
	public static final Font medieval1 = Utils.loadFont("font/Blackpearl-vPxA.ttf", 22);
	public static final Font medieval2 = Utils.loadFont("font/KnightsQuest-nvDV.ttf", 32);
	public static final Font medieval3 = Utils.loadFont("font/Vecna-oppx.ttf", 32);

	// Sounds
	public static final URL blue_boy_adventure = Utils.loadSound("sounds/blue_boy_adventure.wav"); // Music
	public static final URL burning = Utils.loadSound("sounds/114.wav");
	public static final URL coin = Utils.loadSound("sounds/coin.wav");
	public static final URL cursor = Utils.loadSound("sounds/cursor.wav");
	public static final URL fanfare = Utils.loadSound("sounds/fanfare.wav");
	public static final URL hit_monster = Utils.loadSound("sounds/hit_monster.wav");
	public static final URL level_up = Utils.loadSound("sounds/level_up.wav");
	public static final URL mob_death = Utils.loadSound("sounds/mob_death.wav");
	public static final URL power_up = Utils.loadSound("sounds/power_up.wav");
	public static final URL receive_damage = Utils.loadSound("sounds/receive_damage.wav");
	public static final URL spawn = Utils.loadSound("sounds/spawn.wav");
	public static final URL swing_weapon_wav = Utils.loadSound("sounds/swing_weapon.wav");
	public static final URL swing_weapon_ogg = Utils.loadSound("sounds/swing_weapon.ogg");
	public static final URL swing_weapon2 = Utils.loadSound("sounds/swing_weapon2.wav"); // TODO fix
	public static final URL unlock = Utils.loadSound("sounds/unlock.wav");

	// Entities
	public static final SpriteSheet player_movement = new SpriteSheet(Utils.loadImage("textures/entity/player/movement.png"));
	public static final SpriteSheet player_attack_sword = new SpriteSheet(Utils.loadImage("textures/entity/player/attack_sword.png"));
	public static final SpriteSheet player_attack_axe = new SpriteSheet(Utils.loadImage("textures/entity/player/attack_axe.png"));
	public static final SpriteSheet oldman = new SpriteSheet(Utils.loadImage("textures/entity/oldman.png"));
	public static final SpriteSheet slime = new SpriteSheet(Utils.loadImage("textures/entity/slime.png"));

	// Projectile
	public static final SpriteSheet fireball = new SpriteSheet(Utils.loadImage("textures/projectile/fireball.png"));
	public static final SpriteSheet rock = new SpriteSheet(Utils.loadImage("textures/projectile/rock.png"));

	// Objects
	public static final BufferedImage axe = Utils.loadImage("textures/objs/axe.png");
	public static final BufferedImage boots = Utils.loadImage("textures/objs/boots.png");
	public static final BufferedImage chest = Utils.loadImage("textures/objs/chest.png");
	public static final BufferedImage door = Utils.loadImage("textures/objs/door.png");
	public static final BufferedImage gold = Utils.loadImage("textures/objs/gold.png");
	public static final BufferedImage heart_blank = Utils.loadImage("textures/objs/heart_blank.png");
	public static final BufferedImage heart_full = Utils.loadImage("textures/objs/heart_full.png");
	public static final BufferedImage heart_half = Utils.loadImage("textures/objs/heart_half.png");
	public static final BufferedImage key = Utils.loadImage("textures/objs/key.png");
	public static final BufferedImage mana_blank = Utils.loadImage("textures/objs/mana_blank.png");
	public static final BufferedImage mana_full = Utils.loadImage("textures/objs/mana_full.png");
	public static final BufferedImage potion_red = Utils.loadImage("textures/objs/potion_red.png");
	public static final BufferedImage shield_blue = Utils.loadImage("textures/objs/shield_blue.png");
	public static final BufferedImage shield_wood = Utils.loadImage("textures/objs/shield_wood.png");
	public static final BufferedImage sword_normal = Utils.loadImage("textures/objs/sword_normal.png");

	// Tiles
	public static final BufferedImage earth = Utils.loadImage("textures/tiles/earth.png");
	public static final BufferedImage floor01 = Utils.loadImage("textures/tiles/floor01.png");
	public static final BufferedImage grass00 = Utils.loadImage("textures/tiles/grass00.png");
	public static final BufferedImage grass01 = Utils.loadImage("textures/tiles/grass01.png");
	public static final BufferedImage hut = Utils.loadImage("textures/tiles/hut.png");
	public static final BufferedImage road00 = Utils.loadImage("textures/tiles/road00.png");
	public static final BufferedImage road01 = Utils.loadImage("textures/tiles/road01.png");
	public static final BufferedImage road02 = Utils.loadImage("textures/tiles/road02.png");
	public static final BufferedImage road03 = Utils.loadImage("textures/tiles/road03.png");
	public static final BufferedImage road04 = Utils.loadImage("textures/tiles/road04.png");
	public static final BufferedImage road05 = Utils.loadImage("textures/tiles/road05.png");
	public static final BufferedImage road06 = Utils.loadImage("textures/tiles/road06.png");
	public static final BufferedImage road07 = Utils.loadImage("textures/tiles/road07.png");
	public static final BufferedImage road08 = Utils.loadImage("textures/tiles/road08.png");
	public static final BufferedImage road09 = Utils.loadImage("textures/tiles/road09.png");
	public static final BufferedImage road10 = Utils.loadImage("textures/tiles/road10.png");
	public static final BufferedImage road11 = Utils.loadImage("textures/tiles/road11.png");
	public static final BufferedImage road12 = Utils.loadImage("textures/tiles/road12.png");
	public static final BufferedImage table01 = Utils.loadImage("textures/tiles/table01.png");
	public static final BufferedImage tree = Utils.loadImage("textures/tiles/tree.png");
	public static final BufferedImage wall = Utils.loadImage("textures/tiles/wall.png");
	public static final BufferedImage water00 = Utils.loadImage("textures/tiles/water00.png");
	public static final BufferedImage water01 = Utils.loadImage("textures/tiles/water01.png");
	public static final BufferedImage water02 = Utils.loadImage("textures/tiles/water02.png");
	public static final BufferedImage water03 = Utils.loadImage("textures/tiles/water03.png");
	public static final BufferedImage water04 = Utils.loadImage("textures/tiles/water04.png");
	public static final BufferedImage water05 = Utils.loadImage("textures/tiles/water05.png");
	public static final BufferedImage water06 = Utils.loadImage("textures/tiles/water06.png");
	public static final BufferedImage water07 = Utils.loadImage("textures/tiles/water07.png");
	public static final BufferedImage water08 = Utils.loadImage("textures/tiles/water08.png");
	public static final BufferedImage water09 = Utils.loadImage("textures/tiles/water09.png");
	public static final BufferedImage water10 = Utils.loadImage("textures/tiles/water10.png");
	public static final BufferedImage water11 = Utils.loadImage("textures/tiles/water11.png");
	public static final BufferedImage water12 = Utils.loadImage("textures/tiles/water12.png");
	public static final BufferedImage water13 = Utils.loadImage("textures/tiles/water13.png");

	private Assets() {
	}

}
