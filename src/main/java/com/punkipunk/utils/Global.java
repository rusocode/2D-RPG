package com.punkipunk.utils;

/**
 * TODO Shouldn't these values go in JSON?
 */

public final class Global {

    /* Defines the number of updates (ticks) per second. When using a value like 40, a stuttering effect is generated in
     * the rendering of tiles when the player moves (try in full screen and increase the speed of the player to better
     * appreciate the effect). Why is this? The higher the ticks (short steps, little time difference between cycles),
     * the more processing time is needed to catch up in real time. The lower it is (long steps, a lot of time
     * difference between cycles), the more choppy the game is. Ideally, it should be quite high, often faster than 60
     * FPS, so that the game simulates with high quality on fast machines. */
    public static final int UPDATES = 60; // Se relaciona con los ticks (actualizaciones logicas)
    /* Maximum number of times that GameLoop CAN render the screen per second. When I say "can", it means that it can't
     * always reach the specified maximum. TIP: Synchronize the amount of fps with the monitor's Hz to avoid possible
     * "tearing" or "stuttering". */
    public static final int FRAMES = 120; // Se relaciona con los FPS (actualizaciones visuales)
    /* If unlimited fps is activated, the rendering will be performed as many times as possible. The limit for the
     * number of fps is governed by the computer's processing power, game performance, the number of objects rendered on
     * the screen, CPU usage, among others. TIP: Activate this option to test fps drops in different areas of the game. */
    public static final boolean FPS_UNLIMITED = false; // FIXME Al usar AnimationTimer queda obsoleto
    // Others
    public static final String VERSION = "0.0.11a";
    public static final int MAX_INVENTORY_SLOTS = 16; // Tiene que ser igual al total de ROWS * COLS de InventoryController
    public static final int MAX_LVL = 50;
    public static final int SUBWINDOW_ALPHA = 1;
    // Number of visible tiles
    public static final int MAX_WINDOW_ROW = 15;
    public static final int MAX_WINDOW_COL = 21; // TODO o MAX_TILES_COL?
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
    /* An application can run much faster if the images it chooses to display share the same bit depth as the screen.
     * https://docs.oracle.com/javase/tutorial/extra/fullscreen/displaymode.html */
    // Window settings
    /* TODO Cuando se agrande la ventana, los items tendria que cambiar a x64 para evitar deformaciones, por lo tanto
     * el tamanio original de cada item tiene que ser de 64px para poder escalar hacia abajo sin perder calidad. */
    private static final int original_tile = 16;
    private static final int scale = 2;
    public static final int tile = original_tile * scale;
    // Window size
    public static final int WINDOW_WIDTH = tile * MAX_WINDOW_COL;
    public static final int WINDOW_HEIGHT = tile * MAX_WINDOW_ROW;
    public static final int X_OFFSET = WINDOW_WIDTH / 2 - (tile / 2);
    public static final int Y_OFFSET = WINDOW_HEIGHT / 2 - (tile * 2 / 2);

    private Global() {
    }

}
