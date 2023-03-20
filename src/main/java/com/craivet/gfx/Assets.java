package com.craivet.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import com.craivet.utils.Utils;

/**
 * TODO Los metodos loadFont, loadSound y loadImage pueden ser llamados desde un AssetsManager
 */

public final class Assets {

	private Assets() {
	}

	// AUDIO (ambient, music and sounds)
	// Music
	public static final URL music_blue_boy_adventure = Utils.loadAudio("audio/music/blue_boy_adventure.wav");
	// Sounds
	public static final URL sound_burning = Utils.loadAudio("audio/sounds/burning5.wav");
	public static final URL sound_coin = Utils.loadAudio("audio/sounds/coin.wav");
	public static final URL sound_cursor = Utils.loadAudio("audio/sounds/cursor.wav");
	public static final URL sound_cut_tree = Utils.loadAudio("audio/sounds/cut_tree.wav");
	public static final URL sound_draw_sword = Utils.loadAudio("audio/sounds/draw_sword.wav");
	public static final URL sound_hit_monster = Utils.loadAudio("audio/sounds/hit_monster5.wav");
	public static final URL sound_level_up = Utils.loadAudio("audio/sounds/level_up.wav");
	public static final URL sound_mob_death = Utils.loadAudio("audio/sounds/mob_death.wav");
	public static final URL sound_player_die = Utils.loadAudio("audio/sounds/player_die.wav");
	public static final URL sound_potion_red = Utils.loadAudio("audio/sounds/potion2.wav");
	public static final URL sound_power_up = Utils.loadAudio("audio/sounds/power_up.wav");
	public static final URL sound_receive_damage = Utils.loadAudio("audio/sounds/receive_damage.wav");
	public static final URL sound_spawn = Utils.loadAudio("audio/sounds/spawn.wav");
	public static final URL sound_swing_weapon = Utils.loadAudio("audio/sounds/swing_weapon.wav");
	public static final URL sound_swing_axe = Utils.loadAudio("audio/sounds/swing_axe.wav");
	public static final URL sound_trade_open = Utils.loadAudio("audio/sounds/trade_open.wav");

	// FONT
	public static final Font font_medieval1 = Utils.loadFont("font/medieval1.ttf", 22);
	public static final Font font_medieval2 = Utils.loadFont("font/medieval2.ttf", 32);
	public static final Font font_medieval3 = Utils.loadFont("font/medieval3.ttf", 32);

	// TEXTURES
	// Entity
	public static final SpriteSheet entity_player_movement = new SpriteSheet(Utils.loadImage("textures/entity/player/movement.png"));
	public static final SpriteSheet entity_player_attack_sword = new SpriteSheet(Utils.loadImage("textures/entity/player/attack_sword.png")); // TODO SpriteSheet de armas?
	public static final SpriteSheet entity_player_attack_axe = new SpriteSheet(Utils.loadImage("textures/entity/player/attack_axe.png"));
	public static final SpriteSheet entity_oldman = new SpriteSheet(Utils.loadImage("textures/entity/oldman.png"));
	public static final SpriteSheet entity_slime = new SpriteSheet(Utils.loadImage("textures/entity/slime.png"));
	public static final SpriteSheet entity_fireball = new SpriteSheet(Utils.loadImage("textures/entity/fireball.png"));
	public static final BufferedImage entity_sticky_ball = Utils.loadImage("textures/entity/sticky_ball.png");
	public static final BufferedImage entity_merchant = Utils.loadImage("textures/entity/merchant.png");
	// Gui
	public static final SpriteSheet icons = new SpriteSheet(Utils.loadImage("textures/gui/icons.png"));
	public static final BufferedImage heart_full = Utils.loadImage("textures/gui/heart_full.png");
	public static final BufferedImage heart_half = Utils.loadImage("textures/gui/heart_half.png");
	public static final BufferedImage heart_blank = Utils.loadImage("textures/gui/heart_blank.png");
	public static final BufferedImage mana_full = Utils.loadImage("textures/gui/mana_full.png");
	public static final BufferedImage mana_blank = Utils.loadImage("textures/gui/mana_blank.png");
	// Items
	public static final BufferedImage item_axe = Utils.loadImage("textures/items/axe.png");
	public static final BufferedImage item_boots = Utils.loadImage("textures/items/boots.png");
	public static final BufferedImage item_chest = Utils.loadImage("textures/items/chest.png");
	public static final BufferedImage item_door = Utils.loadImage("textures/items/door.png");
	public static final BufferedImage item_coin = Utils.loadImage("textures/items/coin.png");
	public static final BufferedImage item_key = Utils.loadImage("textures/items/key.png");
	public static final BufferedImage item_potion_red = Utils.loadImage("textures/items/potion_red.png");
	public static final BufferedImage item_shield_blue = Utils.loadImage("textures/items/shield_blue.png");
	public static final BufferedImage item_shield_wood = Utils.loadImage("textures/items/shield_wood.png");
	public static final BufferedImage item_sword_normal = Utils.loadImage("textures/items/sword_normal.png");
	// Tiles
	public static final BufferedImage tile_earth = Utils.loadImage("textures/tiles/earth.png");
	public static final BufferedImage tile_floor01 = Utils.loadImage("textures/tiles/floor01.png");
	public static final BufferedImage tile_grass00 = Utils.loadImage("textures/tiles/grass00.png");
	public static final BufferedImage tile_grass01 = Utils.loadImage("textures/tiles/grass01.png");
	public static final BufferedImage tile_hut = Utils.loadImage("textures/tiles/hut.png");
	public static final BufferedImage tile_road00 = Utils.loadImage("textures/tiles/road00.png");
	public static final BufferedImage tile_road01 = Utils.loadImage("textures/tiles/road01.png");
	public static final BufferedImage tile_road02 = Utils.loadImage("textures/tiles/road02.png");
	public static final BufferedImage tile_road03 = Utils.loadImage("textures/tiles/road03.png");
	public static final BufferedImage tile_road04 = Utils.loadImage("textures/tiles/road04.png");
	public static final BufferedImage tile_road05 = Utils.loadImage("textures/tiles/road05.png");
	public static final BufferedImage tile_road06 = Utils.loadImage("textures/tiles/road06.png");
	public static final BufferedImage tile_road07 = Utils.loadImage("textures/tiles/road07.png");
	public static final BufferedImage tile_road08 = Utils.loadImage("textures/tiles/road08.png");
	public static final BufferedImage tile_road09 = Utils.loadImage("textures/tiles/road09.png");
	public static final BufferedImage tile_road10 = Utils.loadImage("textures/tiles/road10.png");
	public static final BufferedImage tile_road11 = Utils.loadImage("textures/tiles/road11.png");
	public static final BufferedImage tile_road12 = Utils.loadImage("textures/tiles/road12.png");
	public static final BufferedImage tile_table01 = Utils.loadImage("textures/tiles/table01.png");
	public static final BufferedImage tile_tree = Utils.loadImage("textures/tiles/tree.png");
	public static final BufferedImage tile_wall = Utils.loadImage("textures/tiles/wall.png");
	public static final BufferedImage tile_water00 = Utils.loadImage("textures/tiles/water00.png");
	public static final BufferedImage tile_water01 = Utils.loadImage("textures/tiles/water01.png");
	public static final BufferedImage tile_water02 = Utils.loadImage("textures/tiles/water02.png");
	public static final BufferedImage tile_water03 = Utils.loadImage("textures/tiles/water03.png");
	public static final BufferedImage tile_water04 = Utils.loadImage("textures/tiles/water04.png");
	public static final BufferedImage tile_water05 = Utils.loadImage("textures/tiles/water05.png");
	public static final BufferedImage tile_water06 = Utils.loadImage("textures/tiles/water06.png");
	public static final BufferedImage tile_water07 = Utils.loadImage("textures/tiles/water07.png");
	public static final BufferedImage tile_water08 = Utils.loadImage("textures/tiles/water08.png");
	public static final BufferedImage tile_water09 = Utils.loadImage("textures/tiles/water09.png");
	public static final BufferedImage tile_water10 = Utils.loadImage("textures/tiles/water10.png");
	public static final BufferedImage tile_water11 = Utils.loadImage("textures/tiles/water11.png");
	public static final BufferedImage tile_water12 = Utils.loadImage("textures/tiles/water12.png");
	public static final BufferedImage tile_water13 = Utils.loadImage("textures/tiles/water13.png");
	// Interactive tiles
	public static final BufferedImage itile_drytree = Utils.loadImage("textures/tiles/interactive/drytree.png");
	public static final BufferedImage itile_trunk = Utils.loadImage("textures/tiles/interactive/trunk.png");

}
