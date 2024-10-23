package com.punkipunk.assets;

import com.punkipunk.utils.Utils;

import java.net.URL;

/**
 * <p>
 * Esta clase usa constantes estaticas de acceso privado que se cargan desde {@code Utils.loadAudio()}. Esta implementacion tiene
 * algunas ventajas ya que los archivos de audio se cargan una sola vez cuando se inicializa la clase, lo que puede mejorar el
 * rendimiento si los archivos se usan con frecuencia.
 * <p>
 * Sin embargo, ten en cuenta que esta implementacion cargara todos los archivos de audio cuando se inicialice la clase, lo que
 * podria aumentar el tiempo de inicio de la aplicacion y el uso de memoria. Si esto es un problema, podrias considerar una
 * estrategia de carga perezosa (lazy loading) donde los archivos se cargan solo cuando se necesitan por primera vez.
 */

public enum AudioAssets {

    // Ambient
    AMBIENT_DUNGEON,
    AMBIENT_OVERWORLD,
    // Music
    MUSIC_BOSS,
    MUSIC_MAIN,
    // Sound
    BAT_DEATH,
    BAT_DEATH2,
    BAT_HIT,
    BAT_HIT2,
    BURST_OF_FIRE,
    BURST_OF_FIRE2,
    CHEST_OPENING,
    CHIPWALL,
    CLICK2,
    CUT_TREE,
    DOOR_IRON_OPENING,
    DOOR_OPENING,
    DRAW_SWORD,
    DRINK_POTION,
    EAT,
    FIREBALL,
    GOLD_PICKUP,
    ITEM_PICKUP,
    LEVEL_UP,
    MINE,
    MOB_DEATH,
    MOB_HIT,
    ORC_DEATH,
    ORC_HIT,
    PLAYER_DAMAGE,
    PLAYER_DEATH,
    SLEEP,
    SLIME_HIT,
    SLOT,
    SPAWN,
    SPAWN2,
    SWING_AXE,
    SWING_WEAPON,
    TRADE_BUY,
    TRADE_OPENING,
    TRADE_SELL;

    // FIXME Sacar el hardcodeo
    private static final String ambient_path = "audio/ambient/";
    private static final String music_path = "audio/music/";
    private static final String sound_path = "audio/sound/";

    // Ambient
    private static final URL ambient_dungeon = Utils.loadAudio(ambient_path + "dungeon.wav");
    private static final URL ambient_overworld = Utils.loadAudio(ambient_path + "overworld.wav");

    // Music
    private static final URL music_boss = Utils.loadAudio(music_path + "boss.wav");
    private static final URL music_main = Utils.loadAudio(music_path + "main.wav");

    // Sound
    private static final URL bat_death = Utils.loadAudio(sound_path + "bat_death.wav");
    private static final URL bat_death2 = Utils.loadAudio(sound_path + "bat_death2.wav");
    private static final URL bat_hit = Utils.loadAudio(sound_path + "bat_hit.wav");
    private static final URL bat_hit2 = Utils.loadAudio(sound_path + "bat_hit2.wav");
    private static final URL burst_of_fire = Utils.loadAudio(sound_path + "burst_of_fire.wav");
    private static final URL burst_of_fire2 = Utils.loadAudio(sound_path + "burst_of_fire2.wav");
    private static final URL chest_opening = Utils.loadAudio(sound_path + "chest_opening.wav");
    private static final URL chipwall = Utils.loadAudio(sound_path + "chipwall.wav");
    private static final URL click2 = Utils.loadAudio(sound_path + "click2.wav");
    private static final URL cut_tree = Utils.loadAudio(sound_path + "cut_tree.wav");
    private static final URL door_iron_opening = Utils.loadAudio(sound_path + "door_iron_opening.wav");
    private static final URL door_opening = Utils.loadAudio(sound_path + "door_opening.wav");
    private static final URL draw_sword = Utils.loadAudio(sound_path + "draw_sword.wav");
    private static final URL drink_potion = Utils.loadAudio(sound_path + "drink_potion.wav");
    private static final URL eat = Utils.loadAudio(sound_path + "eat.wav");
    private static final URL fireball = Utils.loadAudio(sound_path + "fireball.wav");
    private static final URL gold_pickup = Utils.loadAudio(sound_path + "gold_pickup.wav");
    private static final URL item_pickup = Utils.loadAudio(sound_path + "item_pickup.wav");
    private static final URL level_up = Utils.loadAudio(sound_path + "level_up2.wav");
    private static final URL mine = Utils.loadAudio(sound_path + "mine.wav");
    private static final URL mob_death = Utils.loadAudio(sound_path + "mob_death.wav");
    private static final URL mob_hit = Utils.loadAudio(sound_path + "mob_hit.wav");
    private static final URL orc_death = Utils.loadAudio(sound_path + "orc_death2.wav");
    private static final URL orc_hit = Utils.loadAudio(sound_path + "orc_hit.wav");
    private static final URL player_damage = Utils.loadAudio(sound_path + "player_damage.wav");
    private static final URL player_death = Utils.loadAudio(sound_path + "player_death.wav");
    private static final URL sleep = Utils.loadAudio(sound_path + "sleep.wav");
    private static final URL slime_hit = Utils.loadAudio(sound_path + "slime_hit.wav");
    private static final URL slot = Utils.loadAudio(sound_path + "slot.wav");
    private static final URL spawn = Utils.loadAudio(sound_path + "spawn.wav");
    private static final URL spawn2 = Utils.loadAudio(sound_path + "spawn2.wav");
    private static final URL swing_axe = Utils.loadAudio(sound_path + "swing_axe.wav");
    private static final URL swing_weapon = Utils.loadAudio(sound_path + "swing_weapon.wav");
    private static final URL trade_buy = Utils.loadAudio(sound_path + "trade_buy.wav");
    private static final URL trade_opening = Utils.loadAudio(sound_path + "trade_opening.wav");
    private static final URL trade_sell = Utils.loadAudio(sound_path + "trade_sell.wav");

    static URL getAudio(AudioAssets audio) {
        return switch (audio) {
            // Ambient
            case AMBIENT_DUNGEON -> ambient_dungeon;
            case AMBIENT_OVERWORLD -> ambient_overworld;
            // Music
            case MUSIC_BOSS -> music_boss;
            case MUSIC_MAIN -> music_main;
            // Sound
            case BAT_DEATH -> bat_death;
            case BAT_DEATH2 -> bat_death2;
            case BAT_HIT -> bat_hit;
            case BAT_HIT2 -> bat_hit2;
            case BURST_OF_FIRE -> burst_of_fire;
            case BURST_OF_FIRE2 -> burst_of_fire2;
            case CHEST_OPENING -> chest_opening;
            case CHIPWALL -> chipwall;
            case CLICK2 -> click2;
            case CUT_TREE -> cut_tree;
            case DOOR_IRON_OPENING -> door_iron_opening;
            case DOOR_OPENING -> door_opening;
            case DRAW_SWORD -> draw_sword;
            case DRINK_POTION -> drink_potion;
            case EAT -> eat;
            case FIREBALL -> fireball;
            case GOLD_PICKUP -> gold_pickup;
            case ITEM_PICKUP -> item_pickup;
            case LEVEL_UP -> level_up;
            case MINE -> mine;
            case MOB_DEATH -> mob_death;
            case MOB_HIT -> mob_hit;
            case ORC_DEATH -> orc_death;
            case ORC_HIT -> orc_hit;
            case PLAYER_DAMAGE -> player_damage;
            case PLAYER_DEATH -> player_death;
            case SLEEP -> sleep;
            case SLIME_HIT -> slime_hit;
            case SLOT -> slot;
            case SPAWN -> spawn;
            case SPAWN2 -> spawn2;
            case SWING_AXE -> swing_axe;
            case SWING_WEAPON -> swing_weapon;
            case TRADE_BUY -> trade_buy;
            case TRADE_OPENING -> trade_opening;
            case TRADE_SELL -> trade_sell;
        };
    }

}
