package com.punkipunk.utils;

/**
 * <p>
 * Una aplicacion puede ejecutarse mucho mas rapido si las imagenes que elige mostrar comparten la misma profundidad de bits que
 * la pantalla. Ver: <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/displaymode.html">Display Mode</a>
 * <p>
 * TODO ¿No deberian estos valores ir en JSON?
 * <p>
 * TODO Cuando se agranda la ventana, los items tendria que cambiar a x64 para evitar deformaciones, por lo tanto el tamaño
 * original de cada item tiene que ser de 64px para poder escalar hacia abajo sin perder calidad.
 */

public final class Global {

    /**
     * <p>
     * Define el numero de actualizaciones logicas (ticks) por segundo. Al utilizar un valor como 40, se genera un efecto de
     * tartamudeo en la representacion de los tiles cuando el jugador se mueve. Pruebe en pantalla completa y aumente la velocidad
     * del jugador para apreciar mejor el efecto. ¿A que se debe esto? Cuanto mas altos sean los ticks (pasos cortos, poca
     * diferencia de tiempo entre ciclos), mas tiempo de procesamiento se necesita para ponerse al dia en tiempo real. Cuanto mas
     * bajo sea (pasos largos, mucha diferencia de tiempo entre ciclos), mas entrecortado sera el juego. Lo ideal es que sea
     * bastante alto, a menudo mas rapido que 60 FPS, para que el juego simule con alta calidad en maquinas rapidas.
     */
    public static final int UPDATES = 60;

    /**
     * <p>
     * Numero maximo de veces que el GameLoop PUEDE renderizar la pantalla por segundo. Cuando digo "puede", quiero decir que no
     * siempre puede alcanzar el maximo especificado.
     * <p>
     * TIP: Sincroniza la cantidad de FPS con los Hz del monitor para evitar posibles "tearing" o "stuttering". Aunque esto ultimo
     * se soluciona con la implementacion de JavaFX.
     */
    public static final int FRAMES = 120;

    /**
     * <p>
     * Si se activa la opcion de {@code FPS_UNLIMITED}, el renderizado se realizara tantas veces como sea posible. El limite de
     * FPS esta determinado por la potencia de procesamiento del hardware, el rendimiento del juego, la cantidad de objetos
     * renderizados en pantalla, el uso de la CPU, entre otros.
     * <p>
     * TIP: Activa esta opcion para probar caidas de FPS en diferentes areas del juego.
     * <p>
     * Aunque al usar AnimationTimer, esta propiedad queda obsoleta
     */
    @Deprecated
    public static final boolean FPS_UNLIMITED = false;

    // Otros
    public static final String VERSION = "0.1.2-alpha"; // https://semver.org/
    public static final int MAX_LVL = 50;
    public static final int SUBWINDOW_ALPHA = 1;
    // Numero de tiles visibles
    public static final int MAX_WINDOW_ROW = 15;
    public static final int MAX_WINDOW_COL = 21; // TODO o MAX_TILES_COL?
    // Configuracion del mapa
    public static final int MAX_MAP_ROW = 50;
    public static final int MAX_MAP_COL = 50;
    // Intervalo de animaciones
    public static final int INTERVAL_DEAD_ANIMATION = 10;
    public static final int INTERVAL_MOVEMENT_ANIMATION = 15;
    public static final int INTERVAL_PROJECTILE_ANIMATION = 10;
    // Intervalo de armas
    public static final int INTERVAL_ATTACK = 30;
    // Intervalo de proyectiles
    public static final int INTERVAL_PROJECTILE = 60;
    // Otros intervalo
    public static final int INTERVAL_INVINCIBLE = 60;
    public static final int INTERVAL_INVINCIBLE_INTERACTIVE = 20;
    public static final int INTERVAL_DIRECTION = 120;
    public static final int INTERVAL_HP_BAR = 240;
    public static final int INTERVAL_KNOCKBACK = 10;
    public static final int INTERVAL_TELEPORT = 50;
    // Configuracion de la ventana
    private static final int original_tile = 16;
    private static final int scale = 2;
    public static final int tile = original_tile * scale;
    // Tamaño de la ventana
    public static final int WINDOW_WIDTH = tile * MAX_WINDOW_COL;
    public static final int X_OFFSET = WINDOW_WIDTH / 2 - (tile / 2);
    public static final int WINDOW_HEIGHT = tile * MAX_WINDOW_ROW;
    public static final int Y_OFFSET = WINDOW_HEIGHT / 2 - (tile * 2 / 2);

    private Global() {
    }

}
