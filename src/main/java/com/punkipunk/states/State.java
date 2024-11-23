package com.punkipunk.states;

/**
 * Game states.
 */

public enum State {

    MAIN,
    PLAY,
    DIALOGUE,
    CUTSCENE,
    INVENTORY,
    OPTION,
    GAME_OVER,
    TELEPORT,
    TRADE,
    SLEEP;

    private static State currentState = MAIN;

    public static State getState() {
        return currentState;
    }

    public static void setState(State state) {
        currentState = state;
    }

    public static boolean isState(State state) {
        return currentState == state;
    }

}
