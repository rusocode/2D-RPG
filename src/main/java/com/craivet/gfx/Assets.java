package com.craivet.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import com.craivet.utils.Utils;

/**
 * Esta clase carga los recursos solo una vez cuando se inicia la aplicacion.
 */

public final class Assets {

    private Assets() {
    }

    // AUDIO (ambient, music and sounds)
    // Music
    public static final URL music_blue_boy_adventure = Utils.loadAudio("audio/music/blue_boy_adventure.wav");
    // Sounds
    private static final String sounds_path = "audio/sounds/";
    public static final URL sound_burning = Utils.loadAudio(sounds_path + "burning5.wav");
    public static final URL sound_chest_opening = Utils.loadAudio(sounds_path + "chest_opening.wav");
    public static final URL sound_coin = Utils.loadAudio(sounds_path + "coin.wav");
    public static final URL sound_cursor = Utils.loadAudio(sounds_path + "500.wav");
    public static final URL sound_cut_tree = Utils.loadAudio(sounds_path + "cut_tree.wav");
    public static final URL sound_door_opening = Utils.loadAudio(sounds_path + "door_opening.wav");
    public static final URL sound_draw_sword = Utils.loadAudio(sounds_path + "draw_sword.wav");
    public static final URL sound_hit_monster = Utils.loadAudio(sounds_path + "hit_monster5.wav");
    public static final URL sound_level_up = Utils.loadAudio(sounds_path + "level_up.wav");
    public static final URL sound_mob_death = Utils.loadAudio(sounds_path + "mob_death.wav");
    public static final URL sound_player_die = Utils.loadAudio(sounds_path + "player_die.wav");
    public static final URL sound_potion_red = Utils.loadAudio(sounds_path + "potion2.wav");
    public static final URL sound_pick_up = Utils.loadAudio(sounds_path + "472.wav");
    public static final URL sound_receive_damage = Utils.loadAudio(sounds_path + "receive_damage.wav");
    public static final URL sound_spawn = Utils.loadAudio(sounds_path + "spawn.wav");
    public static final URL sound_swing_weapon = Utils.loadAudio(sounds_path + "swing_weapon.wav");
    public static final URL sound_swing_axe = Utils.loadAudio(sounds_path + "swing_axe.wav");
    public static final URL sound_trade = Utils.loadAudio(sounds_path + "485.wav");
    public static final URL sound_trade_open = Utils.loadAudio(sounds_path + "trade_open.wav");

    // FONT
    public static final Font font_medieval1 = Utils.loadFont("font/medieval1.ttf", 22);
    public static final Font font_medieval2 = Utils.loadFont("font/medieval2.ttf", 32);
    public static final Font font_medieval3 = Utils.loadFont("font/medieval3.ttf", 32);

    // TEXTURES
    // Entity
    private static final String textures_entity_path = "textures/entity/";
    public static final SpriteSheet entity_player_movement = new SpriteSheet(Utils.loadImage(textures_entity_path + "player/movement.png"));
    public static final SpriteSheet entity_player_attack_sword = new SpriteSheet(Utils.loadImage(textures_entity_path + "player/attack_sword.png")); // TODO SpriteSheet de armas?
    public static final SpriteSheet entity_player_attack_axe = new SpriteSheet(Utils.loadImage(textures_entity_path + "player/attack_axe.png"));
    public static final SpriteSheet entity_fireball = new SpriteSheet(Utils.loadImage(textures_entity_path + "fireball.png"));
    public static final SpriteSheet entity_oldman = new SpriteSheet(Utils.loadImage(textures_entity_path + "oldman.png"));
    public static final SpriteSheet entity_slime = new SpriteSheet(Utils.loadImage(textures_entity_path + "slime.png"));
    public static final BufferedImage entity_sticky_ball = Utils.loadImage(textures_entity_path + "sticky_ball.png");
    public static final BufferedImage entity_trader = Utils.loadImage(textures_entity_path + "trader.png");

    // Gui
    public static final SpriteSheet icons = new SpriteSheet(Utils.loadImage("textures/gui/icons.png"));
    // Items
    private static final String textures_items_path = "textures/items/";
    public static final BufferedImage item_axe = Utils.loadImage(textures_items_path + "axe.png");
    public static final BufferedImage item_boots = Utils.loadImage(textures_items_path + "boots.png");
    public static final BufferedImage item_chest_closed = Utils.loadImage(textures_items_path + "chest_closed.png");
    public static final BufferedImage item_chest_opened = Utils.loadImage(textures_items_path + "chest_opened.png");
    public static final BufferedImage item_door = Utils.loadImage(textures_items_path + "door.png");
    public static final BufferedImage item_coin = Utils.loadImage(textures_items_path + "coin.png");
    public static final BufferedImage item_key = Utils.loadImage(textures_items_path + "key.png");
    public static final BufferedImage item_lantern = Utils.loadImage(textures_items_path + "lantern.png");
    public static final BufferedImage item_potion_red = Utils.loadImage(textures_items_path + "potion_red.png");
    public static final BufferedImage item_shield_blue = Utils.loadImage(textures_items_path + "shield_blue.png");
    public static final BufferedImage item_shield_wood = Utils.loadImage(textures_items_path + "shield_wood.png");
    public static final BufferedImage item_sword_normal = Utils.loadImage(textures_items_path + "sword_normal.png");
    // Tiles
    private static final String textures_tiles_path = "textures/tiles/";
    public static final BufferedImage tile_earth = Utils.loadImage(textures_tiles_path + "earth.png");
    public static final BufferedImage tile_floor01 = Utils.loadImage(textures_tiles_path + "floor01.png");
    public static final BufferedImage tile_grass00 = Utils.loadImage(textures_tiles_path + "grass00.png");
    public static final BufferedImage tile_grass01 = Utils.loadImage(textures_tiles_path + "grass01.png");
    public static final BufferedImage tile_hut = Utils.loadImage(textures_tiles_path + "hut.png");
    public static final BufferedImage tile_road00 = Utils.loadImage(textures_tiles_path + "road00.png");
    public static final BufferedImage tile_road01 = Utils.loadImage(textures_tiles_path + "road01.png");
    public static final BufferedImage tile_road02 = Utils.loadImage(textures_tiles_path + "road02.png");
    public static final BufferedImage tile_road03 = Utils.loadImage(textures_tiles_path + "road03.png");
    public static final BufferedImage tile_road04 = Utils.loadImage(textures_tiles_path + "road04.png");
    public static final BufferedImage tile_road05 = Utils.loadImage(textures_tiles_path + "road05.png");
    public static final BufferedImage tile_road06 = Utils.loadImage(textures_tiles_path + "road06.png");
    public static final BufferedImage tile_road07 = Utils.loadImage(textures_tiles_path + "road07.png");
    public static final BufferedImage tile_road08 = Utils.loadImage(textures_tiles_path + "road08.png");
    public static final BufferedImage tile_road09 = Utils.loadImage(textures_tiles_path + "road09.png");
    public static final BufferedImage tile_road10 = Utils.loadImage(textures_tiles_path + "road10.png");
    public static final BufferedImage tile_road11 = Utils.loadImage(textures_tiles_path + "road11.png");
    public static final BufferedImage tile_road12 = Utils.loadImage(textures_tiles_path + "road12.png");
    public static final BufferedImage tile_table01 = Utils.loadImage(textures_tiles_path + "table01.png");
    public static final BufferedImage tile_tree = Utils.loadImage(textures_tiles_path + "tree.png");
    public static final BufferedImage tile_wall = Utils.loadImage(textures_tiles_path + "wall.png");
    public static final BufferedImage tile_water00 = Utils.loadImage(textures_tiles_path + "water00.png");
    public static final BufferedImage tile_water01 = Utils.loadImage(textures_tiles_path + "water01.png");
    public static final BufferedImage tile_water02 = Utils.loadImage(textures_tiles_path + "water02.png");
    public static final BufferedImage tile_water03 = Utils.loadImage(textures_tiles_path + "water03.png");
    public static final BufferedImage tile_water04 = Utils.loadImage(textures_tiles_path + "water04.png");
    public static final BufferedImage tile_water05 = Utils.loadImage(textures_tiles_path + "water05.png");
    public static final BufferedImage tile_water06 = Utils.loadImage(textures_tiles_path + "water06.png");
    public static final BufferedImage tile_water07 = Utils.loadImage(textures_tiles_path + "water07.png");
    public static final BufferedImage tile_water08 = Utils.loadImage(textures_tiles_path + "water08.png");
    public static final BufferedImage tile_water09 = Utils.loadImage(textures_tiles_path + "water09.png");
    public static final BufferedImage tile_water10 = Utils.loadImage(textures_tiles_path + "water10.png");
    public static final BufferedImage tile_water11 = Utils.loadImage(textures_tiles_path + "water11.png");
    public static final BufferedImage tile_water12 = Utils.loadImage(textures_tiles_path + "water12.png");
    public static final BufferedImage tile_water13 = Utils.loadImage(textures_tiles_path + "water13.png");
    // Interactive tiles
    public static final BufferedImage itile_drytree = Utils.loadImage(textures_tiles_path + "interactive/drytree.png");
    public static final BufferedImage itile_trunk = Utils.loadImage(textures_tiles_path + "interactive/trunk.png");

}
