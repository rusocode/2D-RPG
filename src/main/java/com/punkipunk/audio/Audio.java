package com.punkipunk.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

/**
 * Clase que maneja la reproduccion de archivos de audio en formato WAV.
 */

public class Audio {

    private final float[] VOLUME_LEVELS = {
            -80f,  // Muted
            -20f,  // Very Low
            -12f,  // Low
            -5f,   // Medium
            1f,    // High
            6f     // Maximum
    };

    /** Indice actual del nivel de volumen, por defecto esta en nivel bajo (2) */
    public int volumeScale = 2;
    /** Clip que maneja la reproduccion del audio */
    private Clip clip;
    /** Control para ajustar el volumen maestro del clip */
    private FloatControl volumeControl;

    /**
     * Reproduce un archivo de audio.
     * <p>
     * Abre el recurso de audio especificado, configura su volumen inicial y comienza la reproduccion.
     *
     * @param url URL del archivo de audio a reproducir
     */
    public void play(URL url) {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
            clip.start();
        } catch (Exception e) {
            System.err.println("Failed to play audio: " + e.getMessage());
        }
    }

    /**
     * Detiene la reproduccion del audio y libera los recursos.
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Activa la reproduccion en bucle continuo del audio actual.
     */
    public void loop() {
        if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Actualiza el volumen del audio segun el nivel seleccionado.
     * <p>
     * Verifica que el indice de volumen sea valido y que exista un control de volumen antes de realizar el cambio.
     */
    public void checkVolume() {
        if (volumeScale >= 0 && volumeScale < VOLUME_LEVELS.length && volumeControl != null)
            volumeControl.setValue(VOLUME_LEVELS[volumeScale]);
    }

}