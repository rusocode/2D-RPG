package com.craivet.utils;

public final class Constants {

	public static final int FPS = 60;

	// Screen settings
	public static final int ORIGINAL_TILE_SIZE = 16; // 16x16 tile
	public static final int SCALE = 3;
	public static final int MAX_SCREEN_COL = 16;
	public static final int MAX_SCREEN_ROW = 12;

	// World settings
	public static final int MAX_WORLD_COL = 50;
	public static final int MAX_WORLD_ROW = 50;

	public static final int ENTITY_WIDTH = 16;
	public static final int ENTITY_HEIGHT = 16;
	public static final int PLAYER_SPEED = 3;

	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;

	// Title screen
	public static final int MAIN_SCREEN = 0;
	public static final int SELECTION_SCREEN = 1;

	// Game state
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
