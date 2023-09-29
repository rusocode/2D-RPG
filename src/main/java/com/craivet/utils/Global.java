package com.craivet.utils;

/**
 * TODO Estos valores no tendrian que ir en un JSON?
 */

public final class Global {

    private Global() {
    }

    /* Define el numero de actualizaciones (ticks) por segundo. Al usar un valor como 40 (cantidad de ticks que usa el
     * AO, creo), se genera un efecto de tartamudeo en el renderizado de tiles cuando el player se mueve (probar en
     * pantalla completa y aumentar la velocidad del player para apreciar mejor el efecto) ¿A que se debe esto?.
     * Cuanto mas alto sean los ticks (pasos cortos, poca diferencia de tiempo entre ciclos), mas tiempo de
     * procesamiento se necesita para ponerse al dia en tiempo real. Cuanto mas bajo es (pasos largos, mucha diferencia
     * de tiempo entre ciclos), mas entrecortado es el juego. Lo ideal es que sea bastante alto, a menudo mas rapido que
     * 60 FPS, para que el juego simule con alta calidad en maquinas rapidas. */
    public static final int UPDATES = 60;
    /* Cantidad maxima de veces que el GameLoop PUEDE llegar a renderizar la pantalla por segundo. Cuando digo "puede",
     * significa que no siempre puede llegar al maximo especificado.
     * TIP: Sincronizar la cantidad de fps con los hz del monitor para evitar un posible "tearing" o "stuttering". */
    public static final int FRAMES = 60;
    /* En caso de activar la ilimitacion de fps, el renderizado se va a realizar la mayor cantidad de veces que pueda.
     * El limite para la cantidad de fps se rigen por el poder de procesamiento de la computadora, el rendimiento del
     * juego, la cantidad de objetos que se renderizan en pantalla, el uso de la CPU, entre otros.
     * TIP: Activar esta opcion para testear bajones de fps en diferentes areas del juego. */
    public static final boolean FPS_UNLIMITED = false;

    // Others
    public static final int MAX_INVENTORY_SLOTS = 20;
    public static final int MAX_LVL = 50;
    public static final int SUBWINDOW_ALPHA = 210;

    /* Una aplicacion puede ejecutarse mucho mas rápido si las imagenes que elige mostrar comparten la misma profundidad
     * de bits que la pantalla. https://docs.oracle.com/javase/tutorial/extra/fullscreen/displaymode.html */
    // Window settings
    private static final int original_tile = 16;
    private static final int scale = 2;
    public static final int tile = original_tile * scale;
    // Cantidad de tiles visibles
    public static final int MAX_WINDOW_ROW = 13; // 12, 13 TODO Por que no coiciden la cantidad de tiles visibles (6) hacia arriba de la cabeza con los del ao (5)?
    public static final int MAX_WINDOW_COL = 17; // 20, 17 TODO o MAX_TILES_COL?
    // Tamaño de la ventana
    public static final int WINDOW_WIDTH = tile * MAX_WINDOW_COL; // 544px conocido como renderTilesWidth
    public static final int WINDOW_HEIGHT = tile * MAX_WINDOW_ROW; // 416px conocido como renderTilesHeight

    // Map settings
    public static final int MAPS = 10;
    public static final int MAX_MAP_ROW = 50; // Cantidad de tiles
    public static final int MAX_MAP_COL = 50;
    // Map list
    public static final int NASHE = 0;
    public static final int NASHE_INDOOR_01 = 1;
    public static final int DUNGEON_01 = 2;
    public static final int DUNGEON_02 = 3;
    // Zones
    public static final int OUTSIDE = 0; // TODO Cambiar a TERRENO o CAMPO
    public static final int INDOOR = 1;
    public static final int DUNGEON = 2;

    // Main window
    public static final int MAIN_WINDOW = 0;
    public static final int SELECTION_WINDOW = 1;

    // Probabilitys %
    public static final int PROBABILITY_GOLD_DROP = 47;
    public static final int PROBABILITY_STONE_DROP = 90;
    public static final int PROBABILITY_KEY_DROP = 30;

    // Animations interval
    public static final int INTERVAL_DEAD_ANIMATION = 10;
    public static final int INTERVAL_MOVEMENT_ANIMATION = 15;
    public static final int INTERVAL_PROJECTILE_ANIMATION = 10;
    // Weapons interval
    public static final int INTERVAL_WEAPON = 30; // TODO o INTERVAL_ATTACK?
    // Projectiles interval
    public static final int INTERVAL_PROJECTILE = 80;
    // Others interval
    public static final int INTERVAL_INVINCIBLE = 60;
    public static final int INTERVAL_INVINCIBLE_INTERACTIVE = 20;
    public static final int INTERVAL_DIRECTION = 120;
    public static final int INTERVAL_DIRECTION_BAT = 50;
    public static final int INTERVAL_HP_BAR = 240;
    public static final int INTERVAL_KNOCKBACK = 10;
    public static final int INTERVAL_TRANSITION = 50;

    // Game states
    /* TODO Se podrian separar estas constantes en un enum tal vez? GameState con enums TITLE, PLAY, etc. o en un
     * paquete con los diferentes tipos de constantes en clases final. */
    public static final int MAIN_STATE = 0;
    public static final int PLAY_STATE = 1;
    public static final int DIALOGUE_STATE = 2;
    public static final int STATS_STATE = 3;
    public static final int OPTION_STATE = 4;
    public static final int GAME_OVER_STATE = 5;
    public static final int TRANSITION_STATE = 6;
    public static final int TRADE_STATE = 7;
    public static final int SLEEP_STATE = 8;
    public static final int INVENTORY_STATE = 9;

}
