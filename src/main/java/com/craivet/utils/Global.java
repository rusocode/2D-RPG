package com.craivet.utils;

/**
 * TODO Shouldn't these values go in JSON?
 */

public final class Global {

    private Global() {
    }

    /* Defines the number of updates (ticks) per second. When using a value like 40, a stuttering effect is generated in
     * the rendering of tiles when the player moves (try in full screen and increase the speed of the player to better
     * appreciate the effect). Why is this? The higher the ticks (short steps, little time difference between cycles),
     * the more processing time is needed to catch up in real time. The lower it is (long steps, a lot of time
     * difference between cycles), the more choppy the game is. Ideally, it should be quite high, often faster than 60
     * FPS, so that the game simulates with high quality on fast machines. */
    public static final int UPDATES = 60;
    /* Maximum number of times that GameLoop CAN render the screen per second. When I say "can", it means that it can't
     * always reach the specified maximum. TIP: Synchronize the amount of fps with the monitor's Hz to avoid possible
     * "tearing" or "stuttering". */
    public static final int FRAMES = 60;
    /* If unlimited fps is activated, the rendering will be performed as many times as possible. The limit for the
     * number of fps is governed by the computer's processing power, game performance, the number of objects rendered on
     * the screen, CPU usage, among others. TIP: Activate this option to test fps drops in different areas of the game. */
    public static final boolean FPS_UNLIMITED = false;

    // Others
    public static final String VERSION = "0.0.11a";
    public static final int MAX_INVENTORY_SLOTS = 20;
    public static final int MAX_LVL = 50;
    public static final int SUBWINDOW_ALPHA = 210;

    /* An application can run much faster if the images it chooses to display share the same bit depth as the screen.
     * https://docs.oracle.com/javase/tutorial/extra/fullscreen/displaymode.html */
    // Window settings
    /* TODO Cuando se agrande la ventana, los items tendria que cambiar a x64 para evitar deformaciones, por lo tanto
     * el tamanio original de cada item tiene que ser de 64px para poder escalar hacia abajo sin perder calidad. */
    private static final int original_tile = 16;
    private static final int scale = 2;
    public static final int tile = original_tile * scale;
    // Number of visible tiles
    public static final int MAX_WINDOW_ROW = 13;
    public static final int MAX_WINDOW_COL = 17; // TODO o MAX_TILES_COL?
    // Window size
    public static final int WINDOW_WIDTH = tile * MAX_WINDOW_COL;
    public static final int WINDOW_HEIGHT = tile * MAX_WINDOW_ROW;

    // Map settings
    public static final int MAPS = 10;
    public static final int MAX_MAP_ROW = 50;
    public static final int MAX_MAP_COL = 50;
    // Maps
    public static final int ABANDONED_ISLAND = 0;
    public static final int ABANDONED_ISLAND_MARKET = 1;
    public static final int DUNGEON_BREG = 2;
    public static final int DUNGEON_BREG_SUB = 3;
    // Zones
    public static final int OVERWORLD = 0;
    public static final int MARKET = 1;
    public static final int DUNGEON = 2;
    public static final int BOSS = 3;

    // Main window
    public static final int MAIN_WINDOW = 0;
    public static final int SELECTION_WINDOW = 1;

    // Probabilitys
    public static final int PROBABILITY_GOLD_DROP = 47;
    public static final int PROBABILITY_STONE_DROP = 90;
    public static final int PROBABILITY_WOOD_DROP = 80;

    // Animations interval
    public static final int INTERVAL_DEAD_ANIMATION = 10;
    public static final int INTERVAL_MOVEMENT_ANIMATION = 15;
    public static final int INTERVAL_PROJECTILE_ANIMATION = 10;
    // Weapons interval
    public static final int INTERVAL_WEAPON = 30; // TODO o INTERVAL_ATTACK?
    // Projectiles interval
    public static final int INTERVAL_PROJECTILE = 60;
    // Others interval
    public static final int INTERVAL_INVINCIBLE = 60;
    public static final int INTERVAL_INVINCIBLE_INTERACTIVE = 20;
    public static final int INTERVAL_DIRECTION = 120;
    public static final int INTERVAL_DIRECTION_BAT = 50;
    public static final int INTERVAL_HP_BAR = 240;
    public static final int INTERVAL_KNOCKBACK = 10;
    public static final int INTERVAL_TELEPORT = 50;

    // Game states
    /* TODO Could these constants be separated into an enum perhaps? GameState with enums TITLE, PLAY, etc. or in a
     * package with the different types of constants in final classes. */
    public static final int MAIN_STATE = 0;
    public static final int PLAY_STATE = 1;
    public static final int DIALOGUE_STATE = 2;
    public static final int STATS_STATE = 3;
    public static final int OPTION_STATE = 4;
    public static final int GAME_OVER_STATE = 5;
    public static final int TELEPORT_STATE = 6;
    public static final int TRADE_STATE = 7;
    public static final int SLEEP_STATE = 8;
    public static final int INVENTORY_STATE = 9;
    public static final int CUTSCENE_STATE = 10;

}
