package com.craivet.utils;

/**
 * TODO Estos valores no tendrian que ir en un JSON?
 */

public final class Global {

    private Global() {
    }

    /* Define el numero de actualizaciones (ticks) por segundo. Al usar un valor como 40 (cantidad de ticks que usa el
     * AO, creo), se genera un efecto de tartamudeo en el renderizado de tiles cuando el player se mueve (probar en
     * pantalla completa y aumentar la velocidad del player para apreciar mejor el efecto) ¿A que se debe esto?. Parece
     * que lo ideal es mantener una sincronizacion entre la cantidad de ticks y fps, en donde 60 ticks y 60 o mas fps es
     * una buena opcion para tener fluidez en el movimiento. */
    public static final int TICKS_PER_SEC = 60;
    /* Cantidad maxima de veces que el GameLoop PUEDE llegar a renderizar la pantalla por segundo. Cuando digo "puede",
     * significa que no siempre puede llegar al maximo especificado.
     * TIP: Sincronizar la cantidad de fps con los hz del monitor para evitar un posible "tearing" o "stuttering". */
    public static final int MAX_FPS = 60;
    /* En caso de activar la ilimitacion de fps, el renderizado se va a realizar la mayor cantidad de veces que pueda.
     * A diferencia de MAX_FPS, los limites para la cantidad de fps son el poder de procesamiento de la computadora, el
     * rendimiento del juego, la cantidad de objetos que se renderizan en pantalla, el uso de la CPU, entre otros.
     * TIP: Activa esta opcion para testear bajones de fps en diferentes areas del juego. */
    public static final boolean FPS_UNLIMITED = false;

    // Others
    public static final int MAX_INVENTORY_SIZE = 20;
    public static final int ENTITY_WIDTH = 16;
    public static final int ENTITY_HEIGHT = 16;
    public static final int SUBWINDOW_ALPHA = 210;

    // Screen settings
    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 2;
    public static final int tile_size = ORIGINAL_TILE_SIZE * SCALE;
    // Tamaño en tiles de la pantalla de visualizacion
    public static final int MAX_SCREEN_COL = 17; // 20
    public static final int MAX_SCREEN_ROW = 13; // 12
    public static final int SCREEN_WIDTH = tile_size * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = tile_size * MAX_SCREEN_ROW;

    // Map settings
    public static final int MAX_MAP = 10;
    public static final int MAX_MAP_ROW = 50;
    public static final int MAX_MAP_COL = 50;
    public static final int NIX = 0;
    public static final int NIX_INDOOR_01 = 1;
    public static final int DUNGEON_01 = 2;
    public static final int DUNGEON_02 = 3;
    // Areas (or zones)
    public static final int OUTSIDE = 0;
    public static final int INDOOR = 1;
    public static final int DUNGEON = 2;

    // Screen titles
    public static final int MAIN_SCREEN = 0;
    public static final int SELECTION_SCREEN = 1;

    // Probabilitys %
    public static final int PROBABILITY_GOLD_DROP = 47;
    public static final int PROBABILITY_STONE_DROP = 90;

    // Animations interval
    public static final int INTERVAL_DEAD_ANIMATION = 10;
    public static final int INTERVAL_MOVEMENT_ANIMATION = 15;
    public static final int INTERVAL_PROJECTILE_ANIMATION = 10;
    // Weapons interval
    public static final int INTERVAL_WEAPON = 30;
    // Projectiles interval
    public static final int INTERVAL_PROJECTILE = 80;
    // Others interval
    public static final int INTERVAL_INVINCIBLE = 60;
    public static final int INTERVAL_INVINCIBLE_INTERACTIVE = 20;
    public static final int INTERVAL_DIRECTION = 120;
    public static final int INTERVAL_HP_BAR = 240;
    public static final int INTERVAL_KNOCKBACK = 10;
    public static final int INTERVAL_TRANSITION = 50;

    // Game states
    /* TODO Se podrian separar estas constantes en un enum tal vez? GameState con enums TITLE, PLAY, etc. o en un
     * paquete con los diferentes tipos de constantes en clases final. */
    public static final int MAIN_STATE = 0;
    public static final int PLAY_STATE = 1;
    public static final int PAUSE_STATE = 2;
    public static final int DIALOGUE_STATE = 3;
    public static final int CHARACTER_STATE = 4;
    public static final int OPTION_STATE = 5;
    public static final int GAME_OVER_STATE = 6;
    public static final int TRANSITION_STATE = 7;
    public static final int TRADE_STATE = 8;
    public static final int SLEEP_STATE = 9;

}
