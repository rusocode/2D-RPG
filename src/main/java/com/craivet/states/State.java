package com.craivet.states;

/**
 * Estados del juego.
 */

public enum State {

    MAIN,
    PLAY,
    DIALOGUE,
    CUTSCENE,
    STATS,
    INVENTORY,
    OPTION,
    GAME_OVER,
    TELEPORT,
    TRADE,
    SLEEP;

    private static State currentState = MAIN;  // Estado inicial por defecto

    public static void setState(State state) {
        currentState = state;
    }

    public static State getState() {
        return currentState;
    }

    public static boolean isState(State state) {
        return currentState == state;
    }

}
