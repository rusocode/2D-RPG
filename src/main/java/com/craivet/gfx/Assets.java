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

    private static final String sounds_path = "audio/sounds/";
    private static final String textures_items_path = "textures/entity/items/";
    private static final String textures_mob_path = "textures/entity/mob/";
    private static final String textures_projectile_path = "textures/entity/projectile/";
    private static final String textures_tiles_interactive_path = "textures/tiles/interactive/";

    // AUDIO (ambient, music and sounds)
    // Music
    public static final URL music_main = Utils.loadAudio("audio/music/main.wav");
    public static final URL music_dungeon = Utils.loadAudio("audio/music/dungeon.wav");
    // Sounds
    public static final URL sound_fireball = Utils.loadAudio(sounds_path + "fireball5.wav");
    public static final URL sound_chest_opening = Utils.loadAudio(sounds_path + "chest_opening.wav");
    public static final URL sound_chipwall = Utils.loadAudio(sounds_path + "chipwall.wav");
    public static final URL sound_gold_pickup = Utils.loadAudio(sounds_path + "gold_pickup.wav");
    public static final URL sound_slot = Utils.loadAudio(sounds_path + "slot.wav");
    public static final URL sound_cut_tree = Utils.loadAudio(sounds_path + "cut_tree.wav");
    public static final URL sound_door_opening = Utils.loadAudio(sounds_path + "door_opening.wav");
    public static final URL sound_door_iron_opening = Utils.loadAudio(sounds_path + "door_iron_opening.wav");
    public static final URL sound_draw_sword = Utils.loadAudio(sounds_path + "draw_sword.wav");
    public static final URL sound_hit_bat = Utils.loadAudio(sounds_path + "hit_bat.wav");
    public static final URL sound_hit_mob = Utils.loadAudio(sounds_path + "hit_mob.wav");
    public static final URL sound_hit_orc = Utils.loadAudio(sounds_path + "hit_orc.wav");
    public static final URL sound_hit_slime = Utils.loadAudio(sounds_path + "hit_slime.wav");
    public static final URL sound_level_up = Utils.loadAudio(sounds_path + "level_up2.wav");
    public static final URL sound_mine = Utils.loadAudio(sounds_path + "mine.wav");
    public static final URL sound_mob_death = Utils.loadAudio(sounds_path + "mob_death.wav");
    public static final URL sound_orc_death = Utils.loadAudio(sounds_path + "orc_death2.wav");
    public static final URL sound_bat_death = Utils.loadAudio(sounds_path + "bat_death.wav");
    public static final URL sound_player_damage = Utils.loadAudio(sounds_path + "player_damage.wav");
    public static final URL sound_player_death = Utils.loadAudio(sounds_path + "player_death.wav");
    public static final URL sound_drink_potion = Utils.loadAudio(sounds_path + "drink_potion.wav");
    public static final URL sound_item_pickup = Utils.loadAudio(sounds_path + "item_pickup2.wav");
    public static final URL sound_sleep = Utils.loadAudio(sounds_path + "sleep.wav");
    public static final URL sound_spawn = Utils.loadAudio(sounds_path + "spawn.wav");
    public static final URL sound_swing_weapon = Utils.loadAudio(sounds_path + "swing_weapon.wav");
    public static final URL sound_swing_axe = Utils.loadAudio(sounds_path + "swing_axe.wav");
    public static final URL sound_trade_buy = Utils.loadAudio(sounds_path + "trade_buy.wav");
    public static final URL sound_trade_opening = Utils.loadAudio(sounds_path + "trade_opening2.wav");
    public static final URL sound_trade_sell = Utils.loadAudio(sounds_path + "trade_sell.wav");

    // FONT
    // TODO El tamaño de fuente tendria que cambiar con respecto a la visualizacion de pantalla
    public static final Font font_minecraft = Utils.loadFont("font/minecraft.ttf", 24);
    public static final Font font_marumonica = Utils.loadFont("font/MaruMonica.ttf", 24);
    public static final Font font_medieval1 = Utils.loadFont("font/medieval1.ttf", 22);
    public static final Font font_medieval2 = Utils.loadFont("font/medieval2.ttf", 32);
    public static final Font font_medieval3 = Utils.loadFont("font/medieval3.ttf", 32);

    // TEXTURES
    // Items
    public static final BufferedImage axe = Utils.loadImage(textures_items_path + "axe.png");
    public static final BufferedImage boots = Utils.loadImage(textures_items_path + "boots.png");
    public static final SpriteSheet chest = new SpriteSheet(Utils.loadImage(textures_items_path + "chest.png"));
    public static final BufferedImage door = Utils.loadImage(textures_items_path + "door.png");
    public static final BufferedImage door_iron = Utils.loadImage(textures_items_path + "door_iron.png");
    public static final BufferedImage gold = Utils.loadImage(textures_items_path + "gold.png");
    public static final BufferedImage key = Utils.loadImage(textures_items_path + "key.png");
    public static final BufferedImage lantern = Utils.loadImage(textures_items_path + "lantern.png");
    public static final BufferedImage pickaxe = Utils.loadImage(textures_items_path + "pickaxe.png");
    public static final BufferedImage potion_red = Utils.loadImage(textures_items_path + "potion_red.png");
    public static final BufferedImage shield_blue = Utils.loadImage(textures_items_path + "shield_blue.png");
    public static final BufferedImage shield_wood = Utils.loadImage(textures_items_path + "shield_wood.png");
    public static final BufferedImage stone = Utils.loadImage(textures_items_path + "stone.png");
    public static final BufferedImage sword_iron = Utils.loadImage(textures_items_path + "sword_iron.png");
    public static final BufferedImage tent = Utils.loadImage(textures_items_path + "tent.png");
    // Items frames
    public static final SpriteSheet sword_frame = new SpriteSheet(Utils.loadImage(textures_items_path + "sword_frame.png")); // TODO Se podria usar una sola imagen y en base a la direccion se voltea y se cambia el color dependiendo del tipo de espada (piedra, hierro, diamante oro, minecraft)
    public static final SpriteSheet axe_frame = new SpriteSheet(Utils.loadImage(textures_items_path + "axe_frame.png"));
    public static final SpriteSheet pickaxe_frame = new SpriteSheet(Utils.loadImage(textures_items_path + "pickaxe_frame.png"));
    // Mobs
    public static final SpriteSheet orc_attack = new SpriteSheet(Utils.loadImage(textures_mob_path + "orc/attack.png"));
    public static final SpriteSheet orc_movement = new SpriteSheet(Utils.loadImage(textures_mob_path + "orc/movement.png"));
    public static final SpriteSheet skeleton_attack = new SpriteSheet(Utils.loadImage(textures_mob_path + "skeleton/attack.png"));
    public static final SpriteSheet skeleton_movement = new SpriteSheet(Utils.loadImage(textures_mob_path + "skeleton/movement.png"));
    public static final SpriteSheet player_axe = new SpriteSheet(Utils.loadImage(textures_mob_path + "player/axe.png"));
    public static final SpriteSheet player_movement = new SpriteSheet(Utils.loadImage(textures_mob_path + "player/player.png"));
    public static final SpriteSheet player_movement2 = new SpriteSheet(Utils.loadImage(textures_mob_path + "player/player 300x360.png"));
    public static final SpriteSheet player_movement3 = new SpriteSheet(Utils.loadImage(textures_mob_path + "player/player 150x180.png"));
    public static final SpriteSheet player_pickaxe = new SpriteSheet(Utils.loadImage(textures_mob_path + "player/pickaxe.png"));
    public static final SpriteSheet bat = new SpriteSheet(Utils.loadImage(textures_mob_path + "bat.png"));
    public static final BufferedImage rock = Utils.loadImage(textures_mob_path + "rock.png");
    public static final SpriteSheet oldman = new SpriteSheet(Utils.loadImage(textures_mob_path + "oldman.png"));
    public static final SpriteSheet redslime = new SpriteSheet(Utils.loadImage(textures_mob_path + "redslime.png"));
    public static final SpriteSheet slime = new SpriteSheet(Utils.loadImage(textures_mob_path + "slime.png"));
    public static final BufferedImage trader = Utils.loadImage(textures_mob_path + "trader.png");
    // Projectile
    public static final SpriteSheet fireball = new SpriteSheet(Utils.loadImage(textures_projectile_path + "fireball.png"));
    public static final BufferedImage stickyball = Utils.loadImage(textures_projectile_path + "stickyball.png");
    // Gui
    public static final SpriteSheet icons = new SpriteSheet(Utils.loadImage("textures/gui/icons.png"));
    // Interactive tiles
    public static final BufferedImage itile_destructiblewall = Utils.loadImage(textures_tiles_interactive_path + "destructiblewall.png");
    public static final BufferedImage itile_drytree = Utils.loadImage(textures_tiles_interactive_path + "drytree.png");
    public static final BufferedImage itile_metalplate = Utils.loadImage(textures_tiles_interactive_path + "metalplate.png");

}
