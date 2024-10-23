package com.punkipunk.engine.core;

import static com.punkipunk.utils.Global.*;

/**
 * <p>
 * GameLoop que separa la actualizacion logica (ticks) del renderizado (frames). Diferencias clave entre la logica y el
 * renderizado:
 * <ol>
 * <li>Proposito:
 * <ul>
 * <li>{@code shouldUpdate()} se centra en la logica del juego.
 * <li>{@code shouldRender()} se centra en la visualizacion del juego.
 * </ul>
 * <li>Frecuencia:
 * <ul>
 * <li>{@code shouldUpdate()} intenta mantener una frecuencia fija de actualizaciones.
 * <li>{@code shouldRender()} puede tener una frecuencia fija o ilimitada, dependiendo de <b>FPS_UNLIMITED</b>.
 * </ul>
 * <li>Comportamiento con retrasos:
 * <ul>
 * <li>{@code shouldUpdate()} puede "ponerse al dia" ejecutandose multiples veces si hay retrasos.
 * <li>{@code shouldRender()} no intenta "ponerse al dia"; simplemente renderiza el siguiente frame cuando es posible.
 * </ul>
 * <li>Impacto en el rendimiento:
 * <ul>
 * <li>Las actualizaciones logicas suelen ser mas criticas para la jugabilidad y consistencia del juego.
 * <li>Los frames renderizados pueden saltarse sin afectar la logica del juego, aunque pueden afectar la suavidad visual.
 * </ul>
 * </ol>
 * <p>
 * Esta separacion entre actualizacion y renderizado es una tecnica comun en el desarrollo de juegos, conocida como
 * "desacoplamiento de la logica y el renderizado". Permite que el juego mantenga una simulacion consistente incluso si el
 * renderizado se ralentiza, y viceversa.
 * <h4>Ticks y FPS</h4>
 * <p>
 * La cantidad de UPDATES (ticks) y FRAMES (fps) no necesitan ser identicos, aunque 60 para ambos es comun. Esta eleccion depende
 * de factores como el rendimiento del hardware, la consistencia del juego y el género. Por ejemplo, 30 UPDATES y 60 FRAMES pueden
 * ser adecuados en ciertas situaciones.
 * <p>
 * El tiempo se mide en nanosegundos por varias razones:
 * <ol>
 * <li>Mayor precision que los milisegundos.
 * <li>Reduce errores de redondeo en calculos de intervalos cortos.
 * <li>Compatible con APIs de Java como System.nanoTime().
 * <li>Permite calculos mas exactos para temporizaciones precisas.
 * </ol>
 * <p>
 * Esta precision es crucial para manejar los pequeños intervalos de tiempo en bucles de juego, aunque la precision real esta
 * limitada por el hardware y el sistema operativo. En resumen, la eleccion de tasas de actualizacion y renderizado, junto con el
 * uso de nanosegundos, busca optimizar el rendimiento y la suavidad del juego segun sus necesidades especificas y las capacidades
 * del sistema.
 * <p>
 * Áreas de mejora potencial:
 * <ol>
 * <li>Sincronizacion vertical: No hay manejo explicito de vsync, lo cual podria ser util para evitar tearing en algunos sistemas.
 * <li>Interpolacion: No se ve implementacion de interpolacion entre estados para renderizado suave a FPS mas altos que los ticks.
 * <li>Tiempo fijo vs variable: El bucle usa un tiempo fijo para actualizaciones. En algunos casos, un paso de tiempo variable
 * podria ser beneficioso.
 * <li>Manejo de retrasos: No hay un manejo explicito para casos donde el juego no pueda mantener la tasa de actualizacion deseada.
 * </ol>
 * <a href="https://gameprogrammingpatterns.com/update-method.html">Update Method</a>
 */

public class OldGameLoop {

    private final double nsPerUpdate, nsPerFrame;
    public int framesInRender;
    public boolean showFPS;
    private int ticks, framesInConsole;
    private long lastUpdate, lastFrame, timer;
    // Acumulador para rastrear el tiempo transcurrido desde la ultima actualizacion
    private double unprocessed;

    public OldGameLoop() {
        lastUpdate = System.nanoTime();
        lastFrame = System.nanoTime();
        timer = System.currentTimeMillis();
        nsPerUpdate = 1E9 / UPDATES;
        nsPerFrame = 1E9 / FRAMES;
    }

    /**
     * Checks if the time necessary to update the game has passed based on the fixed update timestep.
     *
     * @return true if a game update should be updated or false.
     */
    public boolean shouldUpdate() {
        boolean shouldUpdate = false;
        long currentTime = System.nanoTime();
        unprocessed += (currentTime - lastUpdate) / nsPerUpdate;
        lastUpdate = currentTime;
        while (unprocessed >= 1) {
            ticks++;
            unprocessed--;
            shouldUpdate = true;
        }
        return shouldUpdate;
    }

    /**
     * Checks if the time necessary to render the game has passed based on the fixed rendering timestep.
     *
     * @return true if the game should be rendered or false.
     */
    public boolean shouldRender() {
        boolean shouldRender = false;
        long currentTime = System.nanoTime();
        // If the FPS_UNLIMITED option is activated or if the time between each frame is reached
        if (FPS_UNLIMITED || currentTime - lastFrame >= nsPerFrame) {
            framesInConsole++;
            lastFrame = System.nanoTime();
            shouldRender = true;
        }
        return shouldRender;
    }

    /**
     * Time the number of ticks and frames each time interval.
     *
     * @param interval time interval in milliseconds.
     */
    public void timer(int interval) {
        if (System.currentTimeMillis() - timer >= interval) {
            System.out.println(ticks + " ticks, " + framesInConsole + " fps");
            timer = System.currentTimeMillis();
            ticks = 0;
            framesInRender = framesInConsole;
            framesInConsole = 0;
            showFPS = true;
        }
    }

}