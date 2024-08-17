package com.craivet.assets;

import com.craivet.gfx.SpriteSheet;
import com.craivet.utils.Utils;

public enum SpriteSheetAssets {

    // Items
    AXE_FRAME,
    CHEST,
    PICKAXE_FRAME,
    SWORD_FRAME,
    // Mobs
    BAT,
    LIZARD,
    OLDMAN,
    ORC,
    PLAYER,
    RED_SLIME,
    SLIME,
    // Projectiles
    BURST_OF_FIRE,
    FIREBALL;

    private static final String textures_items_path = "textures/entity/items/";
    private static final String textures_mob_path = "textures/entity/mob/";
    private static final String textures_projectile_path = "textures/entity/projectile/";

    // Items
    private static final SpriteSheet axe_frame = new SpriteSheet(Utils.loadTexture(textures_items_path + "axe_frame.png"));
    private static final SpriteSheet chest = new SpriteSheet(Utils.loadTexture(textures_items_path + "chest.png"));
    private static final SpriteSheet pickaxe_frame = new SpriteSheet(Utils.loadTexture(textures_items_path + "pickaxe_frame.png"));
    private static final SpriteSheet sword_frame = new SpriteSheet(Utils.loadTexture(textures_items_path + "sword_frame.png"));

    // Mobs
    private static final SpriteSheet bat = new SpriteSheet(Utils.loadTexture(textures_mob_path + "bat.png"));
    private static final SpriteSheet lizard = new SpriteSheet(Utils.loadTexture(textures_mob_path + "lizard.png"));
    private static final SpriteSheet oldman = new SpriteSheet(Utils.loadTexture(textures_mob_path + "oldman.png"));
    private static final SpriteSheet orc = new SpriteSheet(Utils.loadTexture(textures_mob_path + "orc.png"));
    private static final SpriteSheet player = new SpriteSheet(Utils.loadTexture(textures_mob_path + "player/player.png"));
    private static final SpriteSheet red_slime = new SpriteSheet(Utils.loadTexture(textures_mob_path + "red_slime.png"));
    private static final SpriteSheet slime = new SpriteSheet(Utils.loadTexture(textures_mob_path + "slime.png"));

    // Projectile
    private static final SpriteSheet burst_of_fire = new SpriteSheet(Utils.loadTexture(textures_projectile_path + "burst_of_fire.png"));
    private static final SpriteSheet fireball = new SpriteSheet(Utils.loadTexture(textures_projectile_path + "fireball.png"));

    static SpriteSheet getSpriteSheet(SpriteSheetAssets spriteSheet) {
        return switch (spriteSheet) {
            // Items
            case AXE_FRAME -> axe_frame;
            case CHEST -> chest;
            case PICKAXE_FRAME -> pickaxe_frame;
            case SWORD_FRAME -> sword_frame;
            // Mobs
            case BAT -> bat;
            case LIZARD -> lizard;
            case OLDMAN -> oldman;
            case ORC -> orc;
            case PLAYER -> player;
            case RED_SLIME -> red_slime;
            case SLIME -> slime;
            // Projectile
            case BURST_OF_FIRE -> burst_of_fire;
            case FIREBALL -> fireball;
        };
    }

}
