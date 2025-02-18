package com.punkipunk.audio;

import static org.lwjgl.openal.AL10.*;

/**
 * Encapsula una source de audio OpenAL y proporciona una interfaz de alto nivel para su control.
 * <p>
 * Una source en OpenAL es un "reproductor de audio virtual" que puede reproducir datos de audio almacenados en un buffer. Esta
 * clase gestiona:
 * <ul>
 * <li>Reproduccion y detencion de audio</li>
 * <li>Control de volumen</li>
 * <li>Asociacion con buffers de audio</li>
 * </ul>
 *
 * @see <a href="https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/chapter22/chapter22.html">LWJGL Audio</a>
 */

public class AudioSource {

    /** Nivel de volumen por defecto (rango 0-5) */
    public static final int DEFAULT_VOLUME = 3; // TODO No tendria que ir en VolumeManager?
    /**
     * Niveles de volumen a valores OpenAL (0.0f a 1.0f).
     * <p>
     * Los indices representan:
     * <ul>
     * <li>0: Silencio (0.0f)</li>
     * <li>1: Muy bajo (0.2f)</li>
     * <li>2: Bajo (0.4f)</li>
     * <li>3: Medio (0.6f)</li>
     * <li>4: Alto (0.8f)</li>
     * <li>5: Muy alto (1.0f)</li>
     * </ul>
     */
    private static final float[] VOLUMES = {0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
    /** ID del source donde se reproducira el audio */
    final int source;
    /** Motor de audio para propagar cambios de volumen */
    private final AudioEngine audioEngine;
    /** Sistema de gestion de volumen */
    private final VolumeSystem volumeSystem;
    /** Canal de audio al que pertenece esta source */
    private final AudioChannel channel;
    /** Indica si los cambios de volumen deben propagarse al pool de sonidos */
    private final boolean propagateToSoundPool;
    /** Volumen actual (0-5) */
    public int volume = DEFAULT_VOLUME;
    /** ID del buffer asociado (-1 = ninguno) */
    private int buffer = -1;

    /**
     * Crea una nueva source de audio.
     *
     * @param source               identificador de la source OpenAL
     * @param loop                 true si el audio debe reproducirse en bucle
     * @param propagateToSoundPool true si los cambios de volumen deben propagarse al pool
     * @param audioEngine          motor de audio
     * @param volumeSystem         sistema de gestion de volumen
     * @param channel              canal de audio asociado
     */
    public AudioSource(int source, boolean loop, boolean propagateToSoundPool, AudioEngine audioEngine, VolumeSystem volumeSystem, AudioChannel channel) {
        this.source = source;
        this.audioEngine = audioEngine;
        this.volumeSystem = volumeSystem;
        this.propagateToSoundPool = propagateToSoundPool;
        this.channel = channel;
        initVolume(volumeSystem.get(channel));
        alSourcei(source, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    /**
     * Reproduce audio desde un buffer OpenAL.
     * <p>
     * Detiene cualquier reproduccion previa, asocia el nuevo buffer y comienza la reproduccion.
     *
     * @param buffer ID del buffer OpenAL que contiene los datos de audio
     */
    public void play(int buffer) {
        stop();
        alSourcei(source, AL_BUFFER, buffer);
        alSourcePlay(source);
        this.buffer = buffer;
    }

    /**
     * Detiene la reproduccion actual y desasocia el buffer.
     * <p>
     * La disociacion del buffer es importante para liberar recursos y permitir que la source pueda ser reutilizada por otros
     * buffers, especialmente en el pool de sonidos.
     */
    public void stop() {
        alSourceStop(source);
        if (buffer != -1) {
            alSourcei(source, AL_BUFFER, 0);
            buffer = -1;
        }
    }

    /**
     * Verifica si la source esta reproduciendo audio.
     * <p>
     * Consulta el estado de la source OpenAL usando AL_SOURCE_STATE para determinar si esta en estado AL_PLAYING.
     *
     * @return true si la source esta reproduciendo audio, false en caso contrario
     */
    public boolean isPlaying() {
        return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
    }

    /**
     * Establece el volumen con control sobre la propagacion.
     *
     * @param volumen   volumen actual
     * @param propagate true para propagar el cambio al sistema de audio
     */
    public void setVolume(int volumen, boolean propagate) {
        if (volumen >= 0 && volumen < VOLUMES.length) {
            this.volume = volumen;
            alSourcef(source, AL_GAIN, VOLUMES[volumen]);
            if (volumeSystem != null) volumeSystem.update(channel, volume);
        }
        if (propagate && propagateToSoundPool && audioEngine != null && audioEngine.soundPool != null)
            audioEngine.setSoundVolume(volumen);
    }

    /**
     * Inicializa el volumen.
     *
     * @param volume volumen actual
     */
    private void initVolume(int volume) {
        if (volume >= 0 && volume < VOLUMES.length) {
            this.volume = volume;
            alSourcef(source, AL_GAIN, VOLUMES[volume]);
        }
    }

}