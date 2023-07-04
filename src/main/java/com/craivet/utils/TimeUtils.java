package com.craivet.utils;

/**
 * Wrapper alrededor de System.nanoTime() y System.currentTimeMillis(). ¡Usa esto si quieres ser compatible en todas
 * las plataformas!
 */

public final class TimeUtils {

    private TimeUtils() {
    }

    public static long nanoTime() {
        return System.nanoTime();
    }

    public static long millis() {
        return System.currentTimeMillis();
    }

}
