package com.craivet.utils;

public final class Constants {

	public static final int FPS = 60;
	public static final int MAX_INVENTORY_SIZE = 20;
	public static final int PLAYER_SPEED = 3;
	public static final int ENTITY_WIDTH = 16;
	public static final int ENTITY_HEIGHT = 16;

	// Screen settings
	public static final int ORIGINAL_TILE_SIZE = 16;
	public static final int SCALE = 3;
	public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
	public static final int MAX_SCREEN_COL = 16;
	public static final int MAX_SCREEN_ROW = 12;
	public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL; // 768 px
	public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW; // 576 px

	// World settings
	public static final int MAX_WORLD_COL = 50;
	public static final int MAX_WORLD_ROW = 50;

	// Title screen
	public static final int MAIN_SCREEN = 0;
	public static final int SELECTION_SCREEN = 1;

	// Intervals (ms)
	public static final int INTERVAL_MOVEMENT_ANIMATION = 10;
	public static final int INTERVAL_MOVEMENT_PROJECTILE_ANIMATION = 8;
	public static final int INTERVAL_INVINCIBLE = 60;
	public static final int INTERVAL_DIRECTION = 120;
	public static final int INTERVAL_DEAD_ANIMATION = 10;
	public static final int INTERVAL_HP_BAR = 240;

	// Game state
	/* TODO Se podrian separar estas constantes en un enum tal vez? GameState con enums TITLE, PLAY, etc. o en un
	 * paquete con los diferentes tipos de constantes en clases final. */
	public static final int TITLE_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int PAUSE_STATE = 2;
	public static final int DIALOGUE_STATE = 3;
	public static final int CHARACTER_STATE = 4;

	// Entity type
	public static final int TYPE_PLAYER = 0;
	public static final int TYPE_NPC = 1;
	public static final int TYPE_MOB = 2;
	public static final int TYPE_SWORD = 3;
	public static final int TYPE_AXE = 4;
	public static final int TYPE_SHIELD = 5;
	public static final int TYPE_CONSUMABLE = 6;

	private Constants() {
	}

}
