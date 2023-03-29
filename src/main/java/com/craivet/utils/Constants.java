package com.craivet.utils;

/**
 * TODO Y si aplico enums?
 * <p>
 * <a href="https://github.com/iluwatar/java-design-patterns/tree/master/singleton">...</a>
 */

public final class Constants {

	private Constants() {
	}

	public static final String CONFIG_FILE = "config.txt";
	public static final int FPS = 60;
	public static final int MAX_INVENTORY_SIZE = 20;
	public static final int PLAYER_SPEED = 3;
	public static final int ENTITY_WIDTH = 16;
	public static final int ENTITY_HEIGHT = 16;
	public static final int SUBWINDOW_ALPHA = 210;

	// Directions
	public static final int DOWN = 0;
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int ANY = 4;

	// Screen settings
	public static final int ORIGINAL_TILE_SIZE = 16;
	public static final int SCALE = 3;
	public static final int tile_size = ORIGINAL_TILE_SIZE * SCALE;
	// Relacion de aspecto 4:3 = 960 x 576 pixels
	public static final int MAX_SCREEN_COL = 20;
	public static final int MAX_SCREEN_ROW = 12;
	public static final int SCREEN_WIDTH = tile_size * MAX_SCREEN_COL;
	public static final int SCREEN_HEIGHT = tile_size * MAX_SCREEN_ROW;
	public static final int MAX_MAP = 10;

	// World settings
	public static final int MAX_WORLD_ROW = 50;
	public static final int MAX_WORLD_COL = 50;

	// Title screen
	public static final int MAIN_SCREEN = 0;
	public static final int SELECTION_SCREEN = 1;

	// Probabilidades
	public static final int PROBABILIDAD_DROP_ORO = 47;

	// Interval animation
	public static final int INTERVAL_DEAD_ANIMATION = 10;
	public static final int INTERVAL_MOVEMENT_ANIMATION = 10;
	public static final int INTERVAL_PROJECTILE_ANIMATION = 10;
	// Interval attack
	public static final int INTERVAL_SWORD_ATTACK = 40;
	public static final int INTERVAL_PROJECTILE_ATTACK = 80;
	// Interval others
	public static final int INTERVAL_INVINCIBLE = 60;
	public static final int INTERVAL_INVINCIBLE_TREE = 20;
	public static final int INTERVAL_DIRECTION = 120;
	public static final int INTERVAL_HP_BAR = 240;
	public static final int INTERVAL_KNOCKBACK = 10;
	public static final int INTERVAL_TRANSITION = 50;

	// Game state
	/* TODO Se podrian separar estas constantes en un enum tal vez? GameState con enums TITLE, PLAY, etc. o en un
	 * paquete con los diferentes tipos de constantes en clases final. */
	public static final int TITLE_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int PAUSE_STATE = 2;
	public static final int DIALOGUE_STATE = 3;
	public static final int CHARACTER_STATE = 4;
	public static final int OPTION_STATE = 5;
	public static final int GAME_OVER_STATE = 6;
	public static final int TRANSITION_STATE = 7;
	public static final int TRADE_STATE = 8;

	// Entity type
	public static final int TYPE_PLAYER = 0;
	public static final int TYPE_NPC = 1;
	public static final int TYPE_MOB = 2;
	public static final int TYPE_SWORD = 3;
	public static final int TYPE_AXE = 4;
	public static final int TYPE_SHIELD = 5;
	public static final int TYPE_CONSUMABLE = 6;
	public static final int TYPE_PICKUP_ONLY = 7;

}
