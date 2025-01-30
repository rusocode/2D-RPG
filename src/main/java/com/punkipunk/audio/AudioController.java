package com.punkipunk.audio;

/**
 * Controlador que gestiona la reproduccion de audio en el juego.
 * <p>
 * Actua como una capa de abstraccion entre el mundo del juego y el servicio de audio, facilitando la reproduccion de efectos de
 * sonido, musica y sonidos ambientales.
 */

public class AudioController {

    /** Servicio que maneja las operaciones de audio de bajo nivel */
    private final AudioService audioService;

    public AudioController() {
        this.audioService = new AudioServiceImpl();
    }

    /**
     * Reproduce un efecto de sonido.
     *
     * @param id identificador del sonido a reproducir
     */
    public void playSound(String id) {
        audioService.play(AudioChannel.SOUND, id);
    }

    /**
     * Reproduce un sonido ambiental.
     *
     * @param id identificador del sonido ambiental a reproducir
     */
    public void playAmbient(String id) {
        audioService.stop(AudioChannel.AMBIENT);
        audioService.play(AudioChannel.AMBIENT, id);
    }

    /**
     * Reproduce una pista musical.
     *
     * @param id identificador de la musica a reproducir
     */
    public void playMusic(String id) {
        audioService.stop(AudioChannel.MUSIC);
        audioService.play(AudioChannel.MUSIC, id);
    }

    /**
     * Detiene la reproduccion del audio en todos los canales.
     */
    public void stopAll() {
        audioService.stopAll();
    }

    /**
     * Guarda el volumen en un archivo de configuracion.
     */
    public void save() {
        audioService.save();
    }

    /**
     * Obtiene la instancia de Audio asociada al canal MUSIC.
     *
     * @return la instancia de Audio asociada al canal MUSIC
     */
    public Audio getMusic() {
        return audioService.get(AudioChannel.MUSIC);
    }

    /**
     * Obtiene la instancia de Audio asociada al canal AMBIENT.
     *
     * @return la instancia de Audio asociada al canal AMBIENT
     */
    public Audio getAmbient() {
        return audioService.get(AudioChannel.AMBIENT);
    }

    /**
     * Obtiene la instancia de Audio asociada al canal SOUND.
     *
     * @return la instancia de Audio asociada al canal SOUND
     */
    public Audio getSound() {
        return audioService.get(AudioChannel.SOUND);
    }

}
