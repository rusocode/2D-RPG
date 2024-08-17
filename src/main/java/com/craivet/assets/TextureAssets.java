package com.craivet.assets;

import com.craivet.utils.Utils;

import java.awt.image.BufferedImage;

public enum TextureAssets {

    // Items
    AXE,
    BOOTS,
    CHICKEN,
    DOOR,
    DOOR_IRON,
    GOLD,
    KEY,
    LANTERN,
    PICKAXE,
    POTION_BLUE,
    POTION_RED,
    SHIELD_IRON,
    SHIELD_WOOD,
    STONE,
    SWORD_IRON,
    TENT,
    WOOD,
    // Mobs
    BOX,
    TRADER,
    // Projectile
    STICKY_BALL,
    // Interactive tiles
    ITILE_DESTRUCTIBLE_WALL,
    ITILE_DRY_TREE,
    ITILE_METAL_PLATE;


    private static final String textures_items_path = "textures/entity/items/";
    private static final String textures_mob_path = "textures/entity/mob/";
    private static final String textures_projectile_path = "textures/entity/projectile/";
    private static final String textures_tiles_interactive_path = "textures/tiles/interactive/";

    // Items
    private static final BufferedImage axe = Utils.loadTexture(textures_items_path + "axe.png");
    private static final BufferedImage boots = Utils.loadTexture(textures_items_path + "boots.png");
    private static final BufferedImage chicken = Utils.loadTexture(textures_items_path + "chicken.png");
    private static final BufferedImage door = Utils.loadTexture(textures_items_path + "door.png");
    private static final BufferedImage door_iron = Utils.loadTexture(textures_items_path + "door_iron.png");
    private static final BufferedImage gold = Utils.loadTexture(textures_items_path + "gold.png");
    private static final BufferedImage key = Utils.loadTexture(textures_items_path + "key.png");
    private static final BufferedImage lantern = Utils.loadTexture(textures_items_path + "lantern.png");
    private static final BufferedImage pickaxe = Utils.loadTexture(textures_items_path + "pickaxe.png");
    private static final BufferedImage potion_blue = Utils.loadTexture(textures_items_path + "potion_blue.png");
    private static final BufferedImage potion_red = Utils.loadTexture(textures_items_path + "potion_red.png");
    private static final BufferedImage shield_iron = Utils.loadTexture(textures_items_path + "shield_iron.png");
    private static final BufferedImage shield_wood = Utils.loadTexture(textures_items_path + "shield_wood.png");
    private static final BufferedImage stone = Utils.loadTexture(textures_items_path + "stone.png");
    private static final BufferedImage sword_iron = Utils.loadTexture(textures_items_path + "sword_iron.png");
    private static final BufferedImage tent = Utils.loadTexture(textures_items_path + "tent.png");
    private static final BufferedImage wood = Utils.loadTexture(textures_items_path + "wood.png");

    // Mobs
    private static final BufferedImage box = Utils.loadTexture(textures_mob_path + "box.png");
    private static final BufferedImage trader = Utils.loadTexture(textures_mob_path + "trader.png");

    // Projectile
    private static final BufferedImage sticky_ball = Utils.loadTexture(textures_projectile_path + "stickyball.png");

    // Interactive tiles
    private static final BufferedImage itile_destructible_wall = Utils.loadTexture(textures_tiles_interactive_path + "destructiblewall.png");
    private static final BufferedImage itile_dry_tree = Utils.loadTexture(textures_tiles_interactive_path + "drytree.png");
    private static final BufferedImage itile_metal_plate = Utils.loadTexture(textures_tiles_interactive_path + "metalplate.png");

    static BufferedImage getTexture(TextureAssets textureAssets) {
        return switch (textureAssets) {
            // Items
            case AXE -> axe;
            case BOOTS -> boots;
            case CHICKEN -> chicken;
            case DOOR -> door;
            case DOOR_IRON -> door_iron;
            case GOLD -> gold;
            case KEY -> key;
            case LANTERN -> lantern;
            case PICKAXE -> pickaxe;
            case POTION_BLUE -> potion_blue;
            case POTION_RED -> potion_red;
            case SHIELD_IRON -> shield_iron;
            case SHIELD_WOOD -> shield_wood;
            case STONE -> stone;
            case SWORD_IRON -> sword_iron;
            case TENT -> tent;
            case WOOD -> wood;
            // Mobs
            case BOX -> box;
            case TRADER -> trader;
            // Projectile
            case STICKY_BALL -> sticky_ball;
            // Interactive tiles
            case ITILE_DESTRUCTIBLE_WALL -> itile_destructible_wall;
            case ITILE_DRY_TREE -> itile_dry_tree;
            case ITILE_METAL_PLATE -> itile_metal_plate;
        };
    }

}
