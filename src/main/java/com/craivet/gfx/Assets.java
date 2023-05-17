package com.craivet.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import com.craivet.utils.Utils;

/**
 * Esta clase carga los recursos solo una vez al iniciar el juego.
 */

public final class Assets {

    private Assets() {
    }

    // AUDIO (ambient, music and sounds)
    // Music Gunfor
    public static final URL music_main = Utils.loadAudio("audio/music/main.wav");
    public static final URL music_dungeon = Utils.loadAudio("audio/music/dungeon.wav");
    // Sounds
    private static final String sounds_path = "audio/sounds/";
    public static final URL sound_burning = Utils.loadAudio(sounds_path + "burning5.wav");
    public static final URL sound_chest_opening = Utils.loadAudio(sounds_path + "chest_opening.wav");
    public static final URL sound_chipwall = Utils.loadAudio(sounds_path + "chipwall.wav");
    public static final URL sound_gold = Utils.loadAudio(sounds_path + "gold.wav");
    public static final URL sound_cursor = Utils.loadAudio(sounds_path + "500.wav");
    public static final URL sound_cut_tree = Utils.loadAudio(sounds_path + "cut_tree.wav");
    public static final URL sound_door_opening = Utils.loadAudio(sounds_path + "door_opening.wav");
    public static final URL sound_door_iron_opening = Utils.loadAudio(sounds_path + "door_iron_opening.wav");
    public static final URL sound_draw_sword = Utils.loadAudio(sounds_path + "draw_sword.wav");
    public static final URL sound_hit_mob = Utils.loadAudio(sounds_path + "hit_mob.wav");
    public static final URL sound_hit_orc = Utils.loadAudio(sounds_path + "hit_orc.wav");
    public static final URL sound_hit_slime = Utils.loadAudio(sounds_path + "hit_slime.wav");
    public static final URL sound_level_up = Utils.loadAudio(sounds_path + "level_up.wav");
    public static final URL sound_mine = Utils.loadAudio(sounds_path + "mine.wav");
    public static final URL sound_mob_death = Utils.loadAudio(sounds_path + "mob_death.wav");
    public static final URL sound_player_die = Utils.loadAudio(sounds_path + "player_die.wav");
    public static final URL sound_potion_red = Utils.loadAudio(sounds_path + "potion2.wav");
    public static final URL sound_pickup = Utils.loadAudio(sounds_path + "472.wav");
    public static final URL sound_receive_damage = Utils.loadAudio(sounds_path + "receive_damage.wav");
    public static final URL sound_sleep = Utils.loadAudio(sounds_path + "sleep.wav");
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
    public static final SpriteSheet entity_orc_attack = new SpriteSheet(Utils.loadImage(textures_entity_path + "mob/orc/attack.png"));
    public static final SpriteSheet entity_orc_movement = new SpriteSheet(Utils.loadImage(textures_entity_path + "mob/orc/movement.png"));
    public static final SpriteSheet entity_bat = new SpriteSheet(Utils.loadImage(textures_entity_path + "mob/bat.png"));
    public static final SpriteSheet entity_redslime = new SpriteSheet(Utils.loadImage(textures_entity_path + "mob/redslime.png"));
    public static final SpriteSheet entity_slime = new SpriteSheet(Utils.loadImage(textures_entity_path + "mob/slime.png"));
    public static final BufferedImage entity_bigrock = Utils.loadImage(textures_entity_path + "npc/bigrock.png");
    public static final SpriteSheet entity_oldman = new SpriteSheet(Utils.loadImage(textures_entity_path + "npc/oldman.png"));
    public static final BufferedImage entity_trader = Utils.loadImage(textures_entity_path + "npc/trader.png");
    public static final SpriteSheet entity_player_axe = new SpriteSheet(Utils.loadImage(textures_entity_path + "player/axe.png"));
    public static final SpriteSheet entity_player_sword = new SpriteSheet(Utils.loadImage(textures_entity_path + "player/sword.png")); // TODO SpriteSheet de armas?
    public static final SpriteSheet entity_player_pickaxe = new SpriteSheet(Utils.loadImage(textures_entity_path + "player/pickaxe.png"));
    public static final SpriteSheet entity_player_movement = new SpriteSheet(Utils.loadImage(textures_entity_path + "player/movement.png"));
    public static final SpriteSheet entity_fireball = new SpriteSheet(Utils.loadImage(textures_entity_path + "projectile/fireball.png"));
    public static final BufferedImage entity_sticky_ball = Utils.loadImage(textures_entity_path + "projectile/sticky_ball.png");
    // Gui
    public static final SpriteSheet icons = new SpriteSheet(Utils.loadImage("textures/gui/icons.png"));
    // Items
    private static final String textures_items_path = "textures/items/";
    public static final BufferedImage item_axe = Utils.loadImage(textures_items_path + "axe.png");
    public static final BufferedImage item_boots = Utils.loadImage(textures_items_path + "boots.png");
    public static final BufferedImage item_chest_closed = Utils.loadImage(textures_items_path + "chest_closed.png");
    public static final BufferedImage item_chest_opened = Utils.loadImage(textures_items_path + "chest_opened.png");
    public static final BufferedImage item_door = Utils.loadImage(textures_items_path + "door.png");
    public static final BufferedImage item_door_iron = Utils.loadImage(textures_items_path + "door_iron.png");
    public static final BufferedImage item_gold = Utils.loadImage(textures_items_path + "gold.png");
    public static final BufferedImage item_key = Utils.loadImage(textures_items_path + "key.png");
    public static final BufferedImage item_lantern = Utils.loadImage(textures_items_path + "lantern.png");
    public static final BufferedImage item_pickaxe = Utils.loadImage(textures_items_path + "pickaxe.png");
    public static final BufferedImage item_potion_red = Utils.loadImage(textures_items_path + "potion_red.png");
    public static final BufferedImage item_shield_blue = Utils.loadImage(textures_items_path + "shield_blue.png");
    public static final BufferedImage item_shield_wood = Utils.loadImage(textures_items_path + "shield_wood.png");
    public static final BufferedImage item_stone = Utils.loadImage(textures_items_path + "stone.png");
    public static final BufferedImage item_sword_normal = Utils.loadImage(textures_items_path + "sword_normal.png");
    public static final BufferedImage item_tent = Utils.loadImage(textures_items_path + "tent.png");
    // Interactive tiles
    private static final String textures_tiles_path = "textures/tiles/interactive/";
    public static final BufferedImage itile_destructiblewall = Utils.loadImage(textures_tiles_path + "destructiblewall.png");
    public static final BufferedImage itile_drytree = Utils.loadImage(textures_tiles_path + "drytree.png");
    public static final BufferedImage itile_metalplate = Utils.loadImage(textures_tiles_path + "metalplate.png");

}
