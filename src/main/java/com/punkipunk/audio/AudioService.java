package com.punkipunk.audio;

public interface AudioService {

    /**
     * Reproduce el audio en el canal especificado.
     *
     * @param channel canal del audio
     * @param id      identificador del audio
     */
    void play(AudioChannel channel, String id);

    /**
     * Detiene el audio que se esta reproduciendo en el canal especificado.
     *
     * @param channel canal del audio
     */
    void stop(AudioChannel channel);

    /**
     * Detiene la reproduccion del audio en todos los canales.
     */
    void stopAll();

    /**
     * Obtiene la instancia de Audio asociada al canal especificado.
     *
     * @param channel canal del audio
     * @return la instancia de Audio asociada al canal especificado
     */
    Audio get(AudioChannel channel);

    /**
     * Guarda el volumen en un archivo de configuracion.
     */
    void save();

}
