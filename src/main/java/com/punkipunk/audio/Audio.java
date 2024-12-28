package com.punkipunk.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

/**
 * <p>
 * La clase Audio proporciona funcionalidad para la reproduccion y control de archivos de audio en el juego. Maneja clips de audio
 * individuales y permite controlar su reproduccion, volumen y estado.
 */

public class Audio {

    private static final float[] VOLUMES = {
            -80f,  // Muted
            -20f,  // Very Low
            -12f,  // Low
            -5f,   // Medium
            1f,    // High
            6f     // Maximum
    };

    /** Indice que determina el nivel de volumen actual, referenciando al array VOLUMES */
    public int volume;
    /** Objeto que maneja la reproduccion del archivo de audio */
    private Clip clip;
    /** Control para ajustar el volumen maestro del clip de audio */
    private FloatControl volumeControl;

    /**
     * Reproduce un archivo de audio.
     * <p>
     * El metodo play gestiona la reproduccion de archivos de audio. Obtiene un clip de audio, carga el archivo desde la URL
     * proporcionada, configura el control de volumen y comienza la reproduccion. Si ocurre algun error durante el proceso, lo
     * captura e imprime en la consola sin detener el programa. La reproduccion se realiza de forma asincrona, permitiendo que el
     * programa continue su ejecucion mientras el audio suena en segundo plano.
     *
     * @param url URL del archivo de audio a reproducir
     */
    public void play(URL url) {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume();
            clip.start();
        } catch (Exception e) {
            System.err.println("Failed to play audio: " + e.getMessage());
        }
    }

    /**
     * Detiene la reproduccion del audio actual y libera los recursos asociados.
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Configura el audio actual para reproducirse en bucle continuo.
     */
    public void loop() {
        if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Establece el nivel de volumen del audio basado en el indice almacenado en el atributo {@code volume}.
     * <p>
     * Este metodo ajusta el volumen del audio basado en el valor almacenado en, utilizando un array de volumenes (VOLUMES) para
     * mapear el indice de volume a un valor de volumen especifico.
     */
    public void setVolume() {
        if (volumeControl != null && volume >= 0 && volume < VOLUMES.length)
            volumeControl.setValue(VOLUMES[volume]);
    }

}