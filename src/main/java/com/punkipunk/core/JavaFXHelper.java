package com.punkipunk.core;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Helper para inicializar JavaFX en background sin crear una ventana. Necesario para poder usar Canvas y GraphicsContext de
 * JavaFX desde LWJGL.
 */

public class JavaFXHelper {

    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static final AtomicBoolean initializationStarted = new AtomicBoolean(false);

    /**
     * Inicializa el JavaFX Toolkit sin crear una ventana. Este metodo es seguro para llamar multiples veces.
     */
    public static void initializeJavaFX() {
        // Si ya esta inicializado, no hace nada
        if (initialized.get()) return;

        // Si ya se inicio la inicializacion, espera a que termine
        if (initializationStarted.getAndSet(true)) {
            waitForInitialization();
            return;
        }

        System.out.println("Inicializando JavaFX Toolkit en background...");

        try {
            // Inicializa el toolkit de JavaFX sin crear una ventana
            Platform.startup(() -> {
                // Este callback se ejecuta cuando JavaFX esta listo
                System.out.println("JavaFX Toolkit inicializado correctamente");
                initialized.set(true);
            });

            // Esperar a que JavaFX este listo
            waitForInitialization();

        } catch (IllegalStateException e) {
            // JavaFX ya fue inicializado por otro proceso
            System.out.println("JavaFX ya estaba inicializado");
            initialized.set(true);
        } catch (Exception e) {
            System.err.println("Error al inicializar JavaFX: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta una tarea en el FX Application Thread y espera a que termine.
     *
     * @param task la tarea a ejecutar
     * @throws RuntimeException si ocurre un error
     */
    public static void runAndWait(Runnable task) {
        if (!initialized.get())
            throw new IllegalStateException("JavaFX no esta inicializado. Llama a initializeJavaFX() primero.");

        // Si ya estamos en el FX Thread, ejecutar directamente
        if (Platform.isFxApplicationThread()) {
            task.run();
            return;
        }

        // Ejecutar en FX Thread y esperar
        final CountDownLatch latch = new CountDownLatch(1);
        final Exception[] exception = new Exception[1];

        Platform.runLater(() -> {
            try {
                task.run();
            } catch (Exception e) {
                exception[0] = e;
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for FX task", e);
        }

        if (exception[0] != null) throw new RuntimeException("Error executing FX task", exception[0]);

    }

    /**
     * Ejecuta una tarea en el FX Application Thread de forma asincrona.
     *
     * @param task la tarea a ejecutar
     */
    public static void runLater(Runnable task) {
        if (!initialized.get())
            throw new IllegalStateException("JavaFX no esta inicializado. Llama a initializeJavaFX() primero.");

        Platform.runLater(task);
    }

    /**
     * Verifica si JavaFX esta inicializado.
     */
    public static boolean isInitialized() {
        return initialized.get();
    }

    /**
     * Espera hasta que JavaFX este completamente inicializado.
     */
    private static void waitForInitialization() {
        int maxWaitMs = 5000; // 5 segundos maximo
        int waitedMs = 0;
        int sleepMs = 50;

        while (!initialized.get() && waitedMs < maxWaitMs) {
            try {
                Thread.sleep(sleepMs);
                waitedMs += sleepMs;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (!initialized.get()) {
            throw new RuntimeException("Timeout esperando inicializacion de JavaFX");
        }
    }
}