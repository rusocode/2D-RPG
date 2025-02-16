package com.punkipunk.audio;

/**
 * <p>
 * Define las operaciones basicas del servicio de audio.
 * <p>
 * Esta interfaz actua como una capa de abstraccion entre el controlador de audio y el engine de audio de bajo nivel,
 * proporcionando operaciones para:
 * <ul>
 * <li>Reproduccion de audio en diferentes canales</li>
 * <li>Control del sistema de audio</li>
 * <li>Gestion de volumen</li>
 * <li>Acceso a canales de audio individuales</li>
 * </ul>
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
     * <p>
     * Permite acceder a la configuracion y control detallado de un canal de audio. Para el canal SOUND, devuelve una source
     * virtual que controla el volumen del pool.
     *
     * @param channel canal del cual obtener la source de audio
     * @return source de audio asociada al canal especificado
     */
    AudioSource getAudioSource(AudioChannel channel);

    /**
     * Guarda la configuracion actual de volumen.
     * <p>
     * Persiste los niveles de volumen de todos los canales en el archivo {@code volume.json} para mantenerlos entre sesiones del
     * juego.
     */
    void saveVolume();

    /**
     * Cierra el sistema de audio.
     */
    void shutdown();

}
