package com.punkipunk.audio;

public interface AudioService {

    /**
     * Reproduce el audio en el canal especificado.
     *
     * @param channel canal del audio a reproducir
     * @param audioId identificador del audio a reproducir
     */
    void play(AudioChannel channel, String audioId);

    /**
     * Detiene el audio en el canal especificado.
     *
     * @param channel canal del audio a detener
     */
    void stop(AudioChannel channel);

    /**
     * Detiene la reproduccion en todos los canales de audio.
     */
    void stopAll();

    /**
     * Obtiene la instancia de Audio asociada al canal especificado.
     *
     * @param channel canal del cual obtener la instancia de Audio
     * @return instancia de Audio asociada al canal o null si el canal no existe
     */
    Audio getAudio(AudioChannel channel);

    /**
     * Guarda la configuracion actual de volumen en un archivo JSON.
     * <p>
     * Los valores se almacenan en la ubicacion especifica del sistema definida por UserDataUtils.
     */
    void saveVolumeSettings();

}
