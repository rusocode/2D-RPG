package com.punkipunk.audio;

/**
 * Operaciones del servicio de audio.
 */

public interface AudioService {

    /**
     * Reproduce un archivo de audio en el canal especificado.
     * <p>
     * El identificador del audio debe corresponder a una entrada en el archivo {@code audio.json}. Por ejemplo, para reproducir
     * musica de fondo:
     * <pre>{@code
     * play(AudioChannel.MUSIC, "main");
     * }</pre>
     *
     * @param channel canal donde reproducir el audio
     * @param id      identificador del archivo de audio en {@code audio.json}
     */
    void play(AudioChannel channel, String id);

    /**
     * Detiene toda la reproduccion de audio.
     */
    void stopPlayback();

    /**
     * Obtiene la source de audio asociada a un canal especifico.
     *
     * @param channel canal del cual obtener la source de audio
     * @return source de audio asociada al canal especificado
     */
    AudioSource getAudioSource(AudioChannel channel);

    /**
     * Guarda la configuracion actual de volumen de todos los canales en el archivo {@code volume.json}.
     */
    void saveVolume();

    /**
     * Cierra el sistema de audio.
     */
    void shutdown();

}
